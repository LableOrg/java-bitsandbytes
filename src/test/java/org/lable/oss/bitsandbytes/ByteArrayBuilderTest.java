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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ByteArrayBuilderTest {
    @Test
    public void nullTest() {
        assertThat(ByteArray.startWithEmpty()
                        .append((byte[]) null)
                        .append((String) null)
                        .append((Integer) null)
                        .append((Integer) null, null)
                        .append((Long) null)
                        .append((Long) null, null)
                        .append((Float) null)
                        .append((Double) null)
                        .append((BigInteger) null)
                        .append((BigDecimal) null)
                        .append((Instant) null)
                        .append((ZonedDateTime) null)
                        .append((LocalDate) null)
                        .append((LocalDate) null, null)
                        .toArray().length
                ,
                is(0)
        );

        assertThat(ByteArray.startWithEmpty().toArray().length, is(0));
    }

    @Test
    public void basicTest() {
        assertThat(BytePrinter.nonBasicsEscaped(ByteArray.startWithEmpty()
                        .append("XXX")
                        .appendZeroByte()
                        .append("ZZZ")
                        .appendFullByte()
                        .append("€")
                        .appendHex("00")
                        .toArray())
                ,
                is("XXX\\x00ZZZ\\xff\\xe2\\x82\\xac\\x00"));

        assertThat(BytePrinter.nonBasicsEscaped(ByteArray
                        .of(256L)
                        .append(256L, ByteConversion.NumberRepresentation.LEXICOGRAPHIC_SORT)
                        .toArray())
                ,
                is(
                        "\\x00\\x00\\x00\\x00\\x00\\x00\\x01\\x00" +
                                "\\x80\\x00\\x00\\x00\\x00\\x00\\x01\\x00"
                ));
    }

    @Test
    public void toByteArrayTest() {
        assertThat(
                ByteArray.of("AA").append(0).toByteArray(),
                is(ByteArray.of("AA").append(0).toByteArray())
        );

        // Test equality of output.
        assertThat(
                ByteArray.of("AA").appendByte((byte) 0).toByteArray(),
                is(ByteArray.ofByte((byte) 65).append('A').appendEncoded("\\x00").toByteArray())
        );
    }
}