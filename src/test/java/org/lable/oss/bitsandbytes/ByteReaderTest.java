/*
 * Copyright © 2015 Lable (info@lable.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lable.oss.bitsandbytes;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.lable.oss.bitsandbytes.BytePrinter.escapeByte;
import static org.lable.oss.bitsandbytes.ByteReader.asHexValue;
import static org.lable.oss.bitsandbytes.ByteReader.unescape;

public class ByteReaderTest {
    @Test
    public void unescapeTest() {
        assertThat(unescape("A"), is(new byte[]{65}));

        assertThat(unescape("\\x00"), is(new byte[]{0}));
        assertThat(unescape("\\x0"), is(new byte[]{92, 120, 48}));
        assertThat(unescape("\\x"), is(new byte[]{92, 120}));
        assertThat(unescape("\\"), is(new byte[]{92}));
        assertThat(unescape(""), is(new byte[]{}));

        assertThat(unescape("\\x01"), is(new byte[]{1}));
        assertThat(unescape("\\x10"), is(new byte[]{16}));

        for (int i = 0; i < 256; i++) {
            String escape = escapeByte((byte) i);
            assertThat(unescape("A" + escape + "A"), is(new byte[]{65, (byte) i, 65}));
            assertThat(unescape(escape + escape), is(new byte[]{(byte) i, (byte) i}));
        }
    }

    @Test
    public void roundTripTest() {
        byte[] output = unescape("€");

        assertThat(output, is(new byte[]{(byte) 0xE2, (byte) 0x82, (byte) 0xAC}));
        assertThat(BytePrinter.utf8Escaped(output), is("€"));
    }

    @Test
    public void asHexValueTest() {
        assertThat(asHexValue('A'), is(10));
        assertThat(asHexValue('B'), is(11));
        assertThat(asHexValue('C'), is(12));
        assertThat(asHexValue('D'), is(13));
        assertThat(asHexValue('E'), is(14));
        assertThat(asHexValue('F'), is(15));

        assertThat(asHexValue('a'), is(10));
        assertThat(asHexValue('b'), is(11));
        assertThat(asHexValue('c'), is(12));
        assertThat(asHexValue('d'), is(13));
        assertThat(asHexValue('e'), is(14));
        assertThat(asHexValue('f'), is(15));

        assertThat(asHexValue('0'), is(0));
        assertThat(asHexValue('1'), is(1));
        assertThat(asHexValue('2'), is(2));
        assertThat(asHexValue('3'), is(3));
        assertThat(asHexValue('4'), is(4));
        assertThat(asHexValue('5'), is(5));
        assertThat(asHexValue('6'), is(6));
        assertThat(asHexValue('7'), is(7));
        assertThat(asHexValue('8'), is(8));
        assertThat(asHexValue('9'), is(9));

        // Bogus.
        assertThat(asHexValue('Z'), is(-1));
        assertThat(asHexValue(' '), is(-1));
        assertThat(asHexValue('-'), is(-1));
        assertThat(asHexValue('@'), is(-1));
    }

}