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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for byte arrays, either {@code byte[]} or {@link ByteArray}.
 * <p>
 * Use {@code ByteArray.of(…)} to initiate a new builder.
 */
public class ByteArrayBuilder {
    List<Byte> bytes;

    ByteArrayBuilder() {
        bytes = new ArrayList<>();
    }

    /**
     * Append a value to the byte array.
     *
     * @param b Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder appendByte(byte b) {
        // This method does not overload #append because to act as byte the input must be cast to (byte). With
        // overloading both append(65) and append((byte) 65) work, with different results. To avoid this error-prone
        // invocation, this method is named separately forcing the cast to be added.
        bytes.add(b);
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param byteArray Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(byte[] byteArray) {
        if (byteArray == null) return this;
        for (byte b : byteArray) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param c Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(char c) {
        bytes.add((byte) c);
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param text Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(String text) {
        if (text == null) return this;
        for (byte b : ByteConversion.fromString(text)) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param i Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(Integer i) {
        if (i == null) return this;
        for (byte b : ByteConversion.fromInt(i.intValue())) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param i Value to append.
     * @param numberRepresentation Byte representation of the integer.
     * @return This builder.
     */
    public ByteArrayBuilder append(Integer i, ByteConversion.NumberRepresentation numberRepresentation) {
        if (i == null) return this;
        for (byte b : ByteConversion.fromInt(i.intValue(), numberRepresentation)) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param f Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(Float f) {
        if (f == null) return this;
        for (byte b : ByteConversion.fromFloat(f.floatValue())) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param d Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(Double d) {
        if (d == null) return this;
        for (byte b : ByteConversion.fromDouble(d.doubleValue())) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param l Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(Long l) {
        if (l == null) return this;
        for (byte b : ByteConversion.fromLong(l.longValue())) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param l Value to append.
     * @param numberRepresentation Byte representation of the long.
     * @return This builder.
     */
    public ByteArrayBuilder append(Long l, ByteConversion.NumberRepresentation numberRepresentation) {
        if (l == null) return this;
        for (byte b : ByteConversion.fromLong(l.longValue(), numberRepresentation)) {
            bytes.add(b);
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param bi Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(BigInteger bi) {
        if (bi == null) return this;
        try {
            for (byte b : ByteConversion.fromBigInteger(bi)) {
                bytes.add(b);
            }
        } catch (ByteConversion.ConversionException e) {
            // Ignore. This is only thrown for null inputs, which we guard against.
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param bd Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(BigDecimal bd) {
        if (bd == null) return this;
        try {
            for (byte b : ByteConversion.fromBigDecimal(bd)) {
                bytes.add(b);
            }
        } catch (ByteConversion.ConversionException e) {
            // Ignore. This is only thrown for null inputs, which we guard against.
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param instant Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(Instant instant) {
        if (instant == null) return this;
        try {
            for (byte b : ByteConversion.fromInstant(instant)) {
                bytes.add(b);
            }
        } catch (ByteConversion.ConversionException e) {
            // Ignore. This is only thrown for null inputs, which we guard against.
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param zonedDateTime Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return this;
        try {
            for (byte b : ByteConversion.fromZonedDateTime(zonedDateTime)) {
                bytes.add(b);
            }
        } catch (ByteConversion.ConversionException e) {
            // Ignore. This is only thrown for null inputs, which we guard against.
        }
        return this;
    }

    /**
     * Append a value to the byte array.
     *
     * @param localDate Value to append.
     * @return This builder.
     */
    public ByteArrayBuilder append(LocalDate localDate) {
        return this.append(localDate, null);
    }

    /**
     * Append a value to the byte array.
     *
     * @param localDate Value to append.
     * @param iso8601DateFormat Byte representation of the {@link LocalDate}.
     * @return This builder.
     */
    public ByteArrayBuilder append(LocalDate localDate, ByteConversion.ISO8601DateFormat iso8601DateFormat) {
        if (localDate == null) return this;
        try {
            for (byte b : ByteConversion.fromLocalDate(localDate, iso8601DateFormat)) {
                bytes.add(b);
            }
        } catch (ByteConversion.ConversionException e) {
            // Ignore. This is only thrown for null inputs, which we guard against.
        }
        return this;
    }

    /**
     * Append the value represented by the encoded input string to the byte array. Encoded strings consist of
     * characters and byte escape sequences representing bytes. Escape sequences look like {@code \xf4}.
     * <p>
     * This is a convenience method equivalent to {@code append(ByteReader.unescape(input))}.
     *
     * @param input Encoded input string.
     * @return This builder.
     */
    public ByteArrayBuilder appendEncoded(String input) {
        if (input == null) return this;

        return append(ByteReader.unescape(input));
    }

    /**
     * Append the value represented by the hexadecimal input string to the byte array. See {@link Hex#decode(String)}
     * for the notation of the input string.
     * <p>
     * This is a convenience method equivalent to {@code append(Hex.decode(input))}.
     *
     * @param input Hex encoded input string.
     * @return This builder.
     */
    public ByteArrayBuilder appendHex(String input) {
        if (input == null) return this;

        return append(Hex.decode(input));
    }

    /**
     * Append a single byte with value {@code 0}.
     *
     * @return This builder.
     */
    public ByteArrayBuilder appendZeroByte() {
        return this.append(CommonByteValues.NULL);
    }

    /**
     * Append a single byte with value {@code -1}, or, effectively in twos-complement, {@code 0b11111111}.
     *
     * @return This builder.
     */
    public ByteArrayBuilder appendFullByte() {
        return this.append(CommonByteValues.FULL);
    }

    /**
     * Terminate this builder and return its value as a primitive byte array.
     *
     * @return A byte array.
     */
    public byte[] toArray() {
        byte[] out = new byte[bytes.size()];
        int i = 0;
        for (Byte aByte : bytes) {
            out[i] = aByte;
            i++;
        }
        return out;
    }

    /**
     * Terminate this builder and return its value as a wrapped byte array.
     *
     * @return A {@link ByteArray}.
     */
    public ByteArray toByteArray() {
        return new ByteArray(toArray());
    }
}
