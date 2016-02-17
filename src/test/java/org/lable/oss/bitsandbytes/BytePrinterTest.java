/**
 * Copyright (C) 2015 Lable (info@lable.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lable.oss.bitsandbytes;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.lable.oss.bitsandbytes.BytePrinter.*;

public class BytePrinterTest {
    @Test
    public void utf8EscapedEncodingTest() {
        // Single byte in UTF-8.
        assertThat(utf8Escaped("a".getBytes()), is("a"));
        // Two bytes in UTF-8.
        assertThat(utf8Escaped("¢".getBytes()), is("¢"));
        // Three bytes in UTF-8.
        assertThat(utf8Escaped("€".getBytes()), is("€"));
        // Four bytes in UTF-8.
        assertThat(utf8Escaped("\uD83D\uDE00".getBytes()), is("\uD83D\uDE00"));

        assertThat(utf8Escaped("Test test".getBytes()), is("Test\\x20test"));
        assertThat(utf8Escaped("Test\ntest".getBytes()), is("Test\\x0atest"));
    }

    @Test
    public void utf8EscapedMixedTest() {
        byte[] bytes = ByteMangler.add("ab".getBytes(), new byte[]{0}, "cd".getBytes(), new byte[]{1}, "€".getBytes());
        assertThat(utf8Escaped(bytes), is("ab\\x00cd\\x01€"));

        bytes = ByteMangler.add(ByteConversion.fromLong(1455705031000L), "☺".getBytes());
        assertThat(utf8Escaped(bytes), is("\\x00\\x00\\x01R\\xee\\xc8\\x01X☺"));
    }

    @Test
    public void utf8EscapedNonPrintableTest() {
        // Control-characters U+0000..U+001F (C0 controls):
        assertThat(utf8Escaped(new byte[]{0x13}), is("\\x13"));

        // Delete U+007F:
        assertThat(utf8Escaped(new byte[]{0x7F}), is("\\x7f"));

        // Control-characters U+0080..U+009F (C1 controls):
        assertThat(utf8Escaped(Binary.decode("10000000")), is("\\x80"));
    }

    @Test
    public void utf8EscapedInvalidSequenceTest() {
        // Second byte is not a continuation byte.
        assertThat(utf8Escaped(Binary.decode("11011111 00000000")), is("\\xdf\\x00"));
        assertThat(utf8Escaped(Binary.decode("11101111 00000000 10111111")), is("\\xef\\x00\\xbf"));
        assertThat(utf8Escaped(Binary.decode("11110111 00000000 10111111 10111111")), is("\\xf7\\x00\\xbf\\xbf"));

        // Third byte is not a continuation byte.
        assertThat(utf8Escaped(Binary.decode("11101111 10111111 00000000")), is("\\xef\\xbf\\x00"));
        assertThat(utf8Escaped(Binary.decode("11110111 10111111 00000000 10111111")), is("\\xf7\\xbf\\x00\\xbf"));

        // Fourth byte is not a continuation byte.
        assertThat(utf8Escaped(Binary.decode("11110111 10111111 10111111 00000000")), is("\\xf7\\xbf\\xbf\\x00"));
    }

    @Test
    public void utf8EscapedNotEnoughCharactersLeftTest() {
        // Two byte sequence marker.
        assertThat(utf8Escaped(Binary.decode("11011111")), is("\\xdf"));

        // Three byte sequence marker.
        assertThat(utf8Escaped(Binary.decode("11101111 10111111")), is("\\xef\\xbf"));

        // Four byte sequence marker.
        assertThat(utf8Escaped(Binary.decode("11110111 10111111 10111111")), is("\\xf7\\xbf\\xbf"));
    }

    @Test
    public void utf8EscapedNotPrintableTest() throws ByteConversion.ConversionException {
        // These formatting characters should not be printed as-is.

        // Arabic number sign. 0xD8 0x80 (
        assertThat(utf8Escaped(Binary.decode("11011000 10000000")), is("\\xd8\\x80"));

        // Zero-width space.
        assertThat(utf8Escaped(Binary.decode("11100010 10000000 10000111")), is("\\xe2\\x80\\x87"));

        // Language tag.
        assertThat(utf8Escaped(Binary.decode("11110011 10100000 10000000 10000001")), is("\\xf3\\xa0\\x80\\x81"));
    }

    @Test
    public void isPrintableCharacterTest() {
        // Control-character.
        assertThat(isPrintableCharacter(0), is(false));
        // Formatting-character (soft hyphen).
        assertThat(isPrintableCharacter(0xAD), is(false));
        // Line separator.
        assertThat(isPrintableCharacter('\u2028'), is(false));
        // Paragraph separator.
        assertThat(isPrintableCharacter('\u2029'), is(false));
        // Space separator.
        assertThat(isPrintableCharacter(' '), is(false));
        // Outside of Unicode range.
        assertThat(isPrintableCharacter(Integer.MAX_VALUE), is(false));
    }

    @Test
    public void escapeByteTest() {
        assertThat(escapeByte((byte) 0), is("\\x00"));
        assertThat(escapeByte((byte) 2), is("\\x02"));
        assertThat(escapeByte((byte) 10), is("\\x0a"));
        assertThat(escapeByte((byte) 15), is("\\x0f"));
        assertThat(escapeByte((byte) -16), is("\\xf0"));
        assertThat(escapeByte((byte) -1), is("\\xff"));
    }


    @Test
    public void oneHundredPercentCodeCoverageObsession() {
        // Because 99% code coverage is atrocious.
        new BytePrinter();
    }
}