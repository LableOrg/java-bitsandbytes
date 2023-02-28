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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeParseException;

import static org.lable.oss.bitsandbytes.ByteMangler.*;

/**
 * Convert Java primitives to and from byte arrays for storage. These methods are useful when such operations happen
 * a lot in your code. Most of them are little more than wrappers around {@link ByteBuffer}.
 * <p>
 * For all operations the Big-Endian byte order is maintained. For strings the desired encoding is assumed to be UTF-8.
 */
public class ByteConversion {

    ByteConversion() {
        // Static utility class.
    }

    /**
     * Convert a {@link String} to a byte array, encoding the text as UTF-8.
     *
     * @param input Input text.
     * @return Bytes.
     */
    public static byte[] fromString(String input) {
        if (input == null) return null;
        return input.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Convert a byte array to a {@link String}, on the assumption that the bytes represent UTF-8 encoded text.
     *
     * @param bytes Byte array.
     * @return A {@link String}.
     */
    public static String toString(byte[] bytes) {
        if (bytes == null) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }


    /**
     * Convert an {@link Integer} to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromInt(Integer input) throws ConversionException {
        assertNotNull(input);
        return fromInt(input.intValue());
    }

    /**
     * Convert an {@link Integer} to bytes, according to the specified {@link NumberRepresentation}.
     *
     * @param input                Input value.
     * @param numberRepresentation How to represent the number in bytes.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromInt(Integer input, NumberRepresentation numberRepresentation) throws ConversionException {
        return numberRepresentation == NumberRepresentation.LEXICOGRAPHIC_SORT
                ? flipTheFirstBit(fromInt(input))
                : fromInt(input);
    }

    /**
     * Convert an int primitive to bytes, according to the specified {@link NumberRepresentation}.
     *
     * @param input                Input value.
     * @param numberRepresentation How to represent the number in bytes.
     * @return Bytes.
     */
    public static byte[] fromInt(int input, NumberRepresentation numberRepresentation) {
        return numberRepresentation == NumberRepresentation.LEXICOGRAPHIC_SORT
                ? flipTheFirstBit(fromInt(input))
                : fromInt(input);
    }

    /**
     * Convert an int primitive to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     */
    public static byte[] fromInt(int input) {
        return ByteBuffer.allocate(4).putInt(input).array();
    }

    /**
     * Convert a byte array to an int.
     *
     * @param bytes Byte array.
     * @return An int.
     */
    public static int toInt(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        assertNumBytes(bytes, 4);
        return ByteBuffer.allocate(4).put(bytes).getInt(0);
    }

    /**
     * Convert a byte array to an int, according to the specified {@link NumberRepresentation}.
     *
     * @param bytes                Byte array.
     * @param numberRepresentation How the bytes represent the number.
     * @return An int.
     */
    public static int toInt(byte[] bytes, NumberRepresentation numberRepresentation) throws ConversionException {
        return numberRepresentation == NumberRepresentation.LEXICOGRAPHIC_SORT
                ? toInt(flipTheFirstBit(bytes))
                : toInt(bytes);
    }


    /**
     * Convert a {@link Float} to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromFloat(Float input) throws ConversionException {
        assertNotNull(input);
        return fromFloat(input.floatValue());
    }

    /**
     * Convert a float primitive to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     */
    public static byte[] fromFloat(float input) {
        return ByteBuffer.allocate(4).putFloat(input).array();
    }

    /**
     * Convert a byte array to a float.
     *
     * @param bytes Byte array.
     * @return A float.
     */
    public static float toFloat(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        assertNumBytes(bytes, 4);
        return ByteBuffer.allocate(4).put(bytes).getFloat(0);
    }


    /**
     * Convert a {@link Double} to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromDouble(Double input) throws ConversionException {
        assertNotNull(input);
        return fromDouble(input.doubleValue());
    }

    /**
     * Convert a double primitive to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     */
    public static byte[] fromDouble(double input) {
        return ByteBuffer.allocate(8).putDouble(input).array();
    }

    /**
     * Convert a byte array to a double.
     *
     * @param bytes Byte array.
     * @return A double.
     */
    public static double toDouble(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        assertNumBytes(bytes, 8);
        return ByteBuffer.allocate(8).put(bytes).getDouble(0);
    }


    /**
     * Convert a {@link Long} to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromLong(Long input) throws ConversionException {
        assertNotNull(input);
        return fromLong(input.longValue());
    }

    /**
     * Convert a {@link Long} to bytes, according to the specified {@link NumberRepresentation}.
     *
     * @param input                Input value.
     * @param numberRepresentation How to represent the number in bytes.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromLong(Long input, NumberRepresentation numberRepresentation) throws ConversionException {
        return numberRepresentation == NumberRepresentation.LEXICOGRAPHIC_SORT
                ? flipTheFirstBit(fromLong(input))
                : fromLong(input);
    }

    /**
     * Convert a long primitive to bytes, according to the specified {@link NumberRepresentation}.
     *
     * @param input                Input value.
     * @param numberRepresentation How to represent the number in bytes.
     * @return Bytes.
     */
    public static byte[] fromLong(long input, NumberRepresentation numberRepresentation) {
        return numberRepresentation == NumberRepresentation.LEXICOGRAPHIC_SORT
                ? flipTheFirstBit(fromLong(input))
                : fromLong(input);
    }

    /**
     * Convert a long primitive to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     */
    public static byte[] fromLong(long input) {
        return ByteBuffer.allocate(8).putLong(input).array();
    }

    /**
     * Convert a byte array to a long.
     *
     * @param bytes Byte array.
     * @return A long.
     */
    public static long toLong(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        assertNumBytes(bytes, 8);
        return ByteBuffer.allocate(8).put(bytes).getLong(0);
    }

    /**
     * Convert a byte array to a long, according to the specified {@link NumberRepresentation}.
     *
     * @param bytes                Byte array.
     * @param numberRepresentation How the bytes represent the number.
     * @return A long.
     */
    public static long toLong(byte[] bytes, NumberRepresentation numberRepresentation) throws ConversionException {
        return numberRepresentation == NumberRepresentation.LEXICOGRAPHIC_SORT
                ? toLong(flipTheFirstBit(bytes))
                : toLong(bytes);
    }


    /**
     * Convert a {@link BigInteger} to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromBigInteger(BigInteger input) throws ConversionException {
        assertNotNull(input);
        return input.toByteArray();
    }

    /**
     * Convert a byte array to a {@link BigInteger}.
     *
     * @param bytes Byte array.
     * @return A {@link BigInteger}.
     * @throws ConversionException Thrown when the input lacks enough bytes for a successful conversion or is null.
     */
    public static BigInteger toBigInteger(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        assertAtLeastNumBytes(bytes, 1);
        return new BigInteger(bytes);
    }


    /**
     * Convert a {@link BigDecimal} to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromBigDecimal(BigDecimal input) throws ConversionException {
        assertNotNull(input);
        byte[] value = input.unscaledValue().toByteArray();
        int scale = input.scale();
        return ByteBuffer.allocate(4 + value.length).putInt(scale).put(value).array();
    }

    /**
     * Convert a byte array to a {@link BigDecimal}.
     *
     * @param bytes Byte array.
     * @return A {@link BigDecimal}.
     * @throws ConversionException Thrown when the input lacks enough bytes for a successful conversion or is null.
     */
    public static BigDecimal toBigDecimal(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        // 8 bytes for the scale (int) and at least 1 byte for the value.
        assertAtLeastNumBytes(bytes, 5);
        int scale = toInt(shrink(4, bytes));

        return new BigDecimal(new BigInteger(chomp(4, bytes)), scale);
    }


    /**
     * Convert an {@link Instant} to bytes.
     * <p>
     * The first eight bytes represent the seconds since the epoch, the remaining four bytes are the nanosecond
     * adjustment.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromInstant(Instant input) throws ConversionException {
        assertNotNull(input);
        long seconds = input.getEpochSecond();
        int nano = input.getNano();
        return ByteBuffer.allocate(12).putLong(seconds).putInt(nano).array();
    }

    /**
     * Convert a byte array to an {@link Instant}.
     *
     * @param bytes Byte array.
     * @return An {@link Instant}.
     * @throws ConversionException Thrown when the input is not 12 bytes or is null.
     */
    public static Instant toInstant(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        // 8 bytes for seconds since the Unix epoch, and 4 bytes for the nano-seconds part.
        assertNumBytes(bytes, 12);
        long seconds = toLong(shrink(8, bytes));
        int nano = toInt(chomp(8, bytes));

        return Instant.ofEpochSecond(seconds, nano);
    }


    /**
     * Convert an {@link OffsetDateTime} to bytes.
     * <p>
     * The first eight bytes represent the seconds since the epoch, the next four bytes are the nanosecond
     * adjustment. The remaining bytes are a string representing the time-zone offset ({@link ZoneOffset}).
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromOffsetDateTime(OffsetDateTime input) throws ConversionException {
        assertNotNull(input);
        Instant instant = input.toInstant();
        String id = input.getOffset().getId();
        return ByteMangler.add(fromInstant(instant), fromString(id));
    }

    /**
     * Convert a byte array to an {@link OffsetDateTime}.
     * <p>
     * The first eight bytes represent the seconds since the epoch, the next four bytes are the nanosecond
     * adjustment. The remaining bytes are a string representing the time-zone offset ({@link ZoneOffset}).
     *
     * @param bytes Byte array.
     * @return An {@link OffsetDateTime}.
     * @throws ConversionException Thrown when the input is not at least 13 bytes or is null, and when the time-zone
     *                             offset found is invalid.
     */
    public static OffsetDateTime toOffsetDateTime(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        // 12 bytes for the instant, at least 1 byte (i.e., 'Z') for the time-zone.
        assertAtLeastNumBytes(bytes, 13);
        Instant instant = toInstant(shrink(12, bytes));
        String id = toString(chomp(12, bytes));

        ZoneOffset zoneOffset;
        try {
            zoneOffset = ZoneOffset.of(id);
        } catch (DateTimeException e) {
            throw new ConversionException("Time-zone offset identifier could not be parsed or found: " + id);
        }

        return OffsetDateTime.ofInstant(instant, zoneOffset);
    }


    /**
     * Convert a {@link ZonedDateTime} to bytes.
     * <p>
     * The first eight bytes represent the seconds since the epoch, the next four bytes are the nanosecond
     * adjustment. The remaining bytes are a string representing the time-zone identifier ({@link ZoneId}).
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromZonedDateTime(ZonedDateTime input) throws ConversionException {
        assertNotNull(input);
        Instant instant = input.toInstant();
        String id = input.getZone().getId();
        return ByteMangler.add(fromInstant(instant), fromString(id));
    }

    /**
     * Convert a byte array to a {@link ZonedDateTime}.
     * <p>
     * The first eight bytes represent the seconds since the epoch, the next four bytes are the nanosecond
     * adjustment. The remaining bytes are a string representing the time-zone identifier ({@link ZoneId}).
     *
     * @param bytes Byte array.
     * @return A {@link ZonedDateTime}.
     * @throws ConversionException Thrown when the input is not at least 13 bytes or is null, and when the time-zone
     *                             identifier found is invalid.
     */
    public static ZonedDateTime toZonedDateTime(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        // 12 bytes for the instant, at least 1 byte (i.e., 'Z') for the time-zone.
        assertAtLeastNumBytes(bytes, 13);
        Instant instant = toInstant(shrink(12, bytes));
        String id = toString(chomp(12, bytes));

        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(id);
        } catch (DateTimeException e) {
            throw new ConversionException("Time-zone identifier could not be parsed or found: " + id);
        }

        return ZonedDateTime.ofInstant(instant, zoneId);
    }


    /**
     * Convert a {@link LocalDate} to bytes.
     * <p>
     * This simply turns the ISO-8601 string representation of the date (e.g., "1999-12-31") into bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromLocalDate(LocalDate input) throws ConversionException {
        return fromLocalDate(input, ISO8601DateFormat.EXTENDED);
    }

    /**
     * Convert a {@link LocalDate} to bytes, using the basic or extended ISO-8601 notation.
     * <p>
     * This simply turns the ISO-8601 string representation of the date (e.g., "1999-12-31" or "19991231") into bytes.
     *
     * @param input Input value.
     * @return Bytes.
     * @throws ConversionException Thrown when the input is null.
     */
    public static byte[] fromLocalDate(LocalDate input, ISO8601DateFormat iso8601DateFormat)
            throws ConversionException {
        assertNotNull(input);
        String iso8601 = input.toString();
        switch (iso8601DateFormat) {
            case BASIC:
                String basic = iso8601.substring(0, 4) + iso8601.substring(5, 7) + iso8601.substring(8, 10);
                return basic.getBytes();
            default:
                // Extended notation.
                return iso8601.getBytes();
        }
    }

    /**
     * Convert a byte array to a {@link LocalDate}.
     * <p>
     * The input should represent a string in the ISO-8601 notation. If the input is 8 bytes long, the basic compact
     * notation is assumed (19991231), otherwise, the extended notation (1999-12-31).
     *
     * @param bytes Byte array.
     * @return A {@link LocalDate}.
     * @throws ConversionException Thrown when the input is not 8 or 10 bytes long or is null, and when the date
     *                             could not be parsed.
     */
    public static LocalDate toLocalDate(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);

        String iso8601;
        if (bytes.length == 8) {
            // Add the dashes to get YYYY-MM-DD.
            iso8601 = String.format("%s-%s-%s",
                    new String(bytes, 0, 4), new String(bytes, 4, 2), new String(bytes, 6, 2));
        } else if (bytes.length == 10) {
            iso8601 = new String(bytes);
        } else {
            throw new ConversionException("Expected 8 or 10 bytes of input, got " + bytes.length + ".");
        }

        try {
            return LocalDate.parse(iso8601);
        } catch (DateTimeParseException e) {
            throw new ConversionException("Failed to parse date.", e);
        }
    }


    /* Input validation helpers. */

    static void assertNumBytes(byte[] bytes, int n) throws ConversionException {
        if (bytes.length != n) {
            throw new ConversionException("Expected " + n + " bytes as input for int conversion, got " + bytes.length);
        }
    }

    static void assertAtLeastNumBytes(byte[] bytes, int n) throws ConversionException {
        if (bytes.length < n) {
            throw new ConversionException("Expected at least " + n +
                    " bytes as input for int conversion, got " + bytes.length);
        }
    }

    static void assertNotNull(Object input) throws ConversionException {
        if (input == null) {
            throw new ConversionException("Got null value where none is allowed.");
        }
    }

    /**
     * Thrown when conversion from or to bytes fails due to invalid input.
     */
    public static class ConversionException extends Exception {
        public ConversionException(String message) {
            super(message);
        }

        public ConversionException(String message, Throwable e) {
            super(message, e);
        }
    }

    /**
     * Specify how numbers are converted to and from bytes.
     */
    public enum NumberRepresentation {
        /**
         * Java's default; two's complement.
         */
        TWOS_COMPLEMENT,
        /**
         * The byte representation of both negative and positive numbers sorts naturally.
         */
        LEXICOGRAPHIC_SORT
    }

    /**
     * Specify how dates are converted to and from their ISO-8601 notation in bytes.
     */
    public enum ISO8601DateFormat {
        /**
         * The compact notation without dashes.
         */
        BASIC,
        /**
         * The extended human-friendly notation, including dashes.
         */
        EXTENDED
    }
}
