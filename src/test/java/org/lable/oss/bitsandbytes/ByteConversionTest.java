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
import org.lable.oss.bitsandbytes.ByteConversion.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.lable.oss.bitsandbytes.ByteConversion.NumberRepresentation.LEXICOGRAPHIC_SORT;
import static org.lable.oss.bitsandbytes.ByteConversion.NumberRepresentation.TWOS_COMPLEMENT;
import static org.lable.oss.bitsandbytes.ByteConversion.*;

public class ByteConversionTest {
    @Test
    public void fromStringTest() {
        // Null.
        assertThat(fromString(null), is(nullValue()));
        // Text.
        assertThat(fromString("TEST"), is(Hex.decode("54" + "45" + "53" + "54")));
        // BMP Unicode.
        assertThat(fromString("T€ẞT"), is(Hex.decode("54" + "E282AC" + "E1BA9E" + "54")));
        // Outside of BMP Unicode. This is a single character (U+1F79C DIAMOND TARGET).
        assertThat(fromString("\uD83D\uDF9C"), is(Hex.decode("F09F9E9C")));
    }

    @Test
    public void toStringTest() {
        // Null.
        assertThat(ByteConversion.toString(null), is(nullValue()));
        // Text.
        assertThat(ByteConversion.toString(Hex.decode("54" + "45" + "53" + "54")), is("TEST"));
        // BMP Unicode.
        assertThat(ByteConversion.toString(Hex.decode("54" + "E282AC" + "E1BA9E" + "54")), is("T€ẞT"));
        // Outside of BMP Unicode. This is a single character (U+1F79C DIAMOND TARGET).
        assertThat(ByteConversion.toString(Hex.decode("F09F9E9C")), is("\uD83D\uDF9C"));
    }


    @Test
    public void fromIntTest() throws ConversionException {
        assertThat(fromInt(0), is(Hex.decode("00000000")));

        assertThat(fromInt(Integer.MAX_VALUE), is(Hex.decode("7fffffff")));
        assertThat(fromInt(Integer.MAX_VALUE, TWOS_COMPLEMENT), is(Hex.decode("7fffffff")));
        assertThat(fromInt(Integer.MAX_VALUE, LEXICOGRAPHIC_SORT), is(Hex.decode("ffffffff")));

        assertThat(fromInt(Integer.MIN_VALUE), is(Hex.decode("80000000")));
        assertThat(fromInt(Integer.MIN_VALUE, TWOS_COMPLEMENT), is(Hex.decode("80000000")));
        assertThat(fromInt(Integer.MIN_VALUE, LEXICOGRAPHIC_SORT), is(Hex.decode("00000000")));

        assertThat(fromInt(Integer.valueOf(256), TWOS_COMPLEMENT), is(Hex.decode("00000100")));
        assertThat(fromInt(Integer.valueOf(256), LEXICOGRAPHIC_SORT), is(Hex.decode("80000100")));
    }

    @Test(expected = ConversionException.class)
    public void fromIntNoNull() throws ConversionException {
        fromInt(null);
    }

    @Test
    public void toIntTest() throws ConversionException {
        assertThat(toInt(Hex.decode("80000000")), is(Integer.MIN_VALUE));
        assertThat(toInt(Hex.decode("80000000"), TWOS_COMPLEMENT), is(Integer.MIN_VALUE));
        assertThat(toInt(Hex.decode("00000000"), LEXICOGRAPHIC_SORT), is(Integer.MIN_VALUE));

        assertThat(toInt(Hex.decode("7fffffff")), is(Integer.MAX_VALUE));
        assertThat(toInt(Hex.decode("7fffffff"), TWOS_COMPLEMENT), is(Integer.MAX_VALUE));
        assertThat(toInt(Hex.decode("ffffffff"), LEXICOGRAPHIC_SORT), is(Integer.MAX_VALUE));
    }

    @Test(expected = ConversionException.class)
    public void toIntInvalidInputTest() throws ConversionException {
        toInt(new byte[3]);
    }


    @Test
    public void fromFloatTest() throws ConversionException {
        assertThat(fromFloat(0.0f), is(Hex.decode("00000000")));
        assertThat(fromFloat(Float.valueOf(1.0f)), is(Hex.decode("3f800000")));
        assertThat(fromFloat(Float.MIN_VALUE), is(Hex.decode("00000001")));
        assertThat(fromFloat(Float.MAX_VALUE), is(Hex.decode("7f7fffff")));
    }

    @Test(expected = ConversionException.class)
    public void fromFloatNoNull() throws ConversionException {
        fromFloat(null);
    }

    @Test(expected = ConversionException.class)
    public void toFloatInvalidInputTest() throws ConversionException {
        toFloat(new byte[5]);
    }

    @Test
    public void toFloatTest() throws ConversionException {
        assertThat(toFloat(Hex.decode("00000000")), is(0.0f));
        assertThat(toFloat(Hex.decode("3f800000")), is(1.0f));
        assertThat(toFloat(Hex.decode("00000001")), is(Float.MIN_VALUE));
        assertThat(toFloat(Hex.decode("7f7fffff")), is(Float.MAX_VALUE));
    }


    @Test
    public void fromDoubleTest() throws ConversionException {
        assertThat(fromDouble(0.0d), is(Hex.decode("0000000000000000")));
        assertThat(fromDouble(Double.valueOf(1.0d)), is(Hex.decode("3ff0000000000000")));
        assertThat(fromDouble(Double.MIN_VALUE), is(Hex.decode("0000000000000001")));
        assertThat(fromDouble(Double.MAX_VALUE), is(Hex.decode("7fefffffffffffff")));
    }

    @Test(expected = ConversionException.class)
    public void fromDoubleNoNull() throws ConversionException {
        fromDouble(null);
    }

    @Test(expected = ConversionException.class)
    public void toDoubleInvalidInputTest() throws ConversionException {
        toDouble(new byte[7]);
    }

    @Test
    public void toDoubleTest() throws ConversionException {
        assertThat(toDouble(Hex.decode("0000000000000000")), is(0.0d));
        assertThat(toDouble(Hex.decode("3ff0000000000000")), is(1.0d));
        assertThat(toDouble(Hex.decode("0000000000000001")), is(Double.MIN_VALUE));
        assertThat(toDouble(Hex.decode("7fefffffffffffff")), is(Double.MAX_VALUE));
    }


    @Test
    public void fromLongTest() throws ConversionException {
        assertThat(fromLong(0L), is(Hex.decode("0000000000000000")));
        assertThat(fromLong(Long.valueOf(1L)), is(Hex.decode("0000000000000001")));
        assertThat(fromLong(Long.valueOf(1L), TWOS_COMPLEMENT), is(Hex.decode("0000000000000001")));
        assertThat(fromLong(Long.valueOf(1L), LEXICOGRAPHIC_SORT), is(Hex.decode("8000000000000001")));

        assertThat(fromLong(Long.MIN_VALUE), is(Hex.decode("8000000000000000")));
        assertThat(fromLong(Long.MIN_VALUE, TWOS_COMPLEMENT), is(Hex.decode("8000000000000000")));
        assertThat(fromLong(Long.MIN_VALUE, LEXICOGRAPHIC_SORT), is(Hex.decode("0000000000000000")));

        assertThat(fromLong(Long.MAX_VALUE), is(Hex.decode("7fffffffffffffff")));
        assertThat(fromLong(Long.MAX_VALUE, TWOS_COMPLEMENT), is(Hex.decode("7fffffffffffffff")));
        assertThat(fromLong(Long.MAX_VALUE, LEXICOGRAPHIC_SORT), is(Hex.decode("ffffffffffffffff")));
    }

    @Test(expected = ConversionException.class)
    public void fromLongNoNull() throws ConversionException {
        fromLong(null);
    }

    @Test(expected = ConversionException.class)
    public void toLongInvalidInputTest() throws ConversionException {
        toLong(new byte[9]);
    }

    @Test
    public void toLongTest() throws ConversionException {
        assertThat(toLong(Hex.decode("0000000000000000")), is(0L));
        assertThat(toLong(Hex.decode("0000000000000001")), is(1L));

        assertThat(toLong(Hex.decode("8000000000000000")), is(Long.MIN_VALUE));
        assertThat(toLong(Hex.decode("8000000000000000"), TWOS_COMPLEMENT), is(Long.MIN_VALUE));
        assertThat(toLong(Hex.decode("0000000000000000"), LEXICOGRAPHIC_SORT), is(Long.MIN_VALUE));

        assertThat(toLong(Hex.decode("7fffffffffffffff")), is(Long.MAX_VALUE));
        assertThat(toLong(Hex.decode("7fffffffffffffff"), TWOS_COMPLEMENT), is(Long.MAX_VALUE));
        assertThat(toLong(Hex.decode("ffffffffffffffff"), LEXICOGRAPHIC_SORT), is(Long.MAX_VALUE));
    }


    @Test
    public void fromBigIntegerTest() throws ConversionException {
        assertThat(fromBigInteger(BigInteger.ONE), is(Hex.decode("01")));
        assertThat(
                fromBigInteger(new BigInteger("10000000000000000000000000000000000")),
                is(Hex.decode("01ED09BEAD87C0378D8E6400000000")));
    }

    @Test(expected = ConversionException.class)
    public void fromBigIntegerNoNull() throws ConversionException {
        fromBigInteger(null);
    }

    @Test
    public void toBigIntegerTest() throws ConversionException {
        assertThat(toBigInteger(Hex.decode("00")), is(BigInteger.ZERO));
        assertThat(
                toBigInteger(Hex.decode("01ED09BEAD87C0378D8E6400000000")).toString(),
                is("10000000000000000000000000000000000"));
    }

    @Test
    public void fromBigDecimalTest() throws ConversionException {
        assertThat(fromBigDecimal(BigDecimal.ONE), is(Hex.decode("0000000001")));
        assertThat(
                fromBigDecimal(new BigDecimal(100).divide(new BigDecimal(Long.MAX_VALUE), -100, RoundingMode.HALF_UP)),
                is(Hex.decode("ffffff9c00")));
    }


    @Test(expected = ConversionException.class)
    public void fromBigDecimalNoNull() throws ConversionException {
        fromBigDecimal(null);
    }

    @Test(expected = ConversionException.class)
    public void toBigDecimalInvalidInput() throws ConversionException {
        toBigDecimal(Hex.decode("000001"));
    }

    @Test
    public void toBigDecimalTest() throws ConversionException {
        assertThat(toBigDecimal(Hex.decode("0000000000")), is(BigDecimal.ZERO));
        assertThat(toBigDecimal(Hex.decode("ffffff9c00")).toString(), is("0E+100"));
    }


    @Test
    public void fromInstantTest() throws ConversionException {
        assertThat(fromInstant(Instant.EPOCH), is(Hex.decode("000000000000000000000000")));

        // No nano part.
        assertThat(fromInstant(Instant.ofEpochMilli(1456759960000L)), is(Hex.decode("0000000056d4649800000000")));

        // No seconds, only nano.
        assertThat(fromInstant(Instant.ofEpochSecond(0L, 16)), is(Hex.decode("000000000000000000000010")));
    }

    @Test
    public void toInstantTest() throws ConversionException {
        assertThat(toInstant(Hex.decode("000000000000000000000000")), is(Instant.EPOCH));

        // No nano part.
        assertThat(toInstant(Hex.decode("0000000056d4649800000000")), is(Instant.ofEpochMilli(1456759960000L)));

        // No seconds, only nano.
        assertThat(toInstant(Hex.decode("000000000000000000000010")), is(Instant.ofEpochSecond(0L, 16)));
    }


    @Test
    public void fromOffsetDateTimeTest() throws ConversionException {
        OffsetDateTime odt = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.of("Z"));

        // All zeroes for the instant and 'Z' in UTF-8.
        assertThat(fromOffsetDateTime(odt), is(Hex.decode("0000000000000000000000005a")));

        odt = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.of("+03:00"));
        assertThat(fromOffsetDateTime(odt), is(Hex.decode("0000000000000000000000002b30333a3030")));

        odt = OffsetDateTime.of(LocalDate.of(1999, Month.AUGUST, 3), LocalTime.NOON, ZoneOffset.of("-04:00"));
        assertThat(fromOffsetDateTime(odt), is(Hex.decode("0000000037a71200000000002d30343a3030")));
    }

    @Test
    public void toOffsetDateTimeTest() throws ConversionException {
        OffsetDateTime odt = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.of("Z"));

        // All zeroes for the instant and 'Z' in UTF-8.
        assertThat(toOffsetDateTime(Hex.decode("0000000000000000000000005a")), is(odt));

        odt = OffsetDateTime.of(LocalDate.of(1999, Month.AUGUST, 3), LocalTime.NOON, ZoneOffset.of("+09:00"));
        assertThat(toOffsetDateTime(Hex.decode("0000000037a65b30000000002b30393a3030")), is(odt));
    }

    @Test(expected = ConversionException.class)
    public void toOffsetDateTimeInvalidOffsetTest() throws ConversionException {
        // 'ZZ' is not a valid offset.
        toOffsetDateTime(Hex.decode("0000000000000000000000005a5a"));
    }


    @Test
    public void fromZonedDateTimeTest() throws ConversionException {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Z"));

        // All zeroes for the instant and 'Z' in UTF-8.
        assertThat(fromZonedDateTime(zdt), is(Hex.decode("0000000000000000000000005a")));

        zdt = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Japan"));
        assertThat(fromZonedDateTime(zdt), is(Hex.decode("0000000000000000000000004a6170616e")));

        zdt = ZonedDateTime.of(LocalDate.of(1999, Month.AUGUST, 3), LocalTime.NOON, ZoneId.of("Europe/Amsterdam"));

        assertThat(fromZonedDateTime(zdt),
                is(Hex.decode("0000000037a6bda0000000004575726f70652f416d7374657264616d")));
    }

    @Test
    public void toZonedDateTimeTest() throws ConversionException {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Z"));

        // All zeroes for the instant and 'Z' in UTF-8.
        assertThat(toZonedDateTime(Hex.decode("0000000000000000000000005a")), is(zdt));

        zdt = ZonedDateTime.of(LocalDate.of(1999, Month.AUGUST, 3), LocalTime.NOON, ZoneId.of("Japan"));
        assertThat(toZonedDateTime(Hex.decode("0000000037a65b30000000004a6170616e")), is(zdt));
    }

    @Test(expected = ConversionException.class)
    public void toZonedDateTimeInvalidZoneTest() throws ConversionException {
        // 'ZZ' is not a valid zone id.
        toZonedDateTime(Hex.decode("0000000000000000000000005a5a"));
    }

    @Test
    public void fromLocalDateTest() throws ConversionException {
        LocalDate ldt = LocalDate.ofEpochDay(0);

        assertThat(fromLocalDate(ldt), is("1970-01-01".getBytes()));
        assertThat(fromLocalDate(ldt, ISO8601DateFormat.EXTENDED), is("1970-01-01".getBytes()));
        assertThat(fromLocalDate(ldt, ISO8601DateFormat.BASIC), is("19700101".getBytes()));
    }

    @Test
    public void toLocalDateTest() throws ConversionException {
        LocalDate ldt = LocalDate.ofEpochDay(0);

        assertThat(toLocalDate("19700101".getBytes()), is(ldt));
        assertThat(toLocalDate("1970-01-01".getBytes()), is(ldt));
    }

    @Test(expected = ConversionException.class)
    public void toLocalDateInvalidInputTest() throws ConversionException {
        toLocalDate("123456789".getBytes());
    }

    @Test(expected = ConversionException.class)
    public void toLocalDateInvalidDateTest() throws ConversionException {
        toLocalDate("1999-13-50".getBytes());
    }


    @Test
    public void numberRepresentationTest() {
        assertThat(NumberRepresentation.values().length, is(2));
        assertThat(NumberRepresentation.valueOf("TWOS_COMPLEMENT"), is(TWOS_COMPLEMENT));
        assertThat(NumberRepresentation.valueOf("LEXICOGRAPHIC_SORT"), is(LEXICOGRAPHIC_SORT));
    }

    @Test
    public void oneHundredPercentCodeCoverageObsession() {
        // Because 99% code coverage is unbearable.
        new Binary();
    }
}