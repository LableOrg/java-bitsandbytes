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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Convert Java primitives to and from byte arrays for storage. These methods are useful when such operations happen
 * a lot in your code. Most of them are little more than wrappers around {@link ByteBuffer}.
 */
public class ByteConversion {
    /**
     * Convert a {@link String} to a byte array, encoding the text as UTF-8.
     *
     * @param input Input text.
     * @return Bytes.
     */
    public static byte[] fromString(String input) {
        if (input == null) return null;
        return input.getBytes(Charset.forName("UTF-8"));
    }

    /**
     * Convert a byte array to a {@link String}, on the assumption that the bytes represent UTF-8 encoded text.
     *
     * @param bytes Byte array.
     * @return A {@link String}.
     */
    public static String toString(byte[] bytes) {
        if (bytes == null) return null;
        return new String(bytes, Charset.forName("UTF-8"));
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
     * Convert an int primitive to bytes.
     *
     * @param input Input value.
     * @return Bytes.
     */
    public static byte[] fromInt(int input) throws ConversionException {
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
    public static byte[] fromFloat(float input) throws ConversionException {
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
    public static byte[] fromDouble(double input) throws ConversionException {
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
    public static Long toLong(byte[] bytes) throws ConversionException {
        assertNotNull(bytes);
        assertNumBytes(bytes, 8);
        return ByteBuffer.allocate(8).put(bytes).getLong(0);
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
        int scale = toInt(ByteMangler.shrink(4, bytes));

        return new BigDecimal(new BigInteger(ByteMangler.chomp(4, bytes)), scale);
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
    }
}
