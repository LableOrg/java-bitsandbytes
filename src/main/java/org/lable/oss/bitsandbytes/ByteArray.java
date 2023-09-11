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
import java.util.Arrays;

/**
 * Wrapper around {@code byte[]} for use in {@link java.util.Set} and {@link java.util.Map}.
 */
public class ByteArray {
    transient Integer hash;
    final byte[] bytes;

    /**
     * Construct a new {@link ByteArray} with a specific payload.
     *
     * @param bytes Byte array to wrap, must not be {@code null}.
     */
    public ByteArray(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("Byte array cannot be null.");

        this.bytes = bytes;
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder startWithEmpty() {
        return new ByteArrayBuilder();
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param b Start with this byte.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder ofByte(byte b) {
        // This method does not overload #of because to act as byte the input must be cast to (byte). With
        // overloading both append(65) and append((byte) 65) work, with different results. To avoid this error-prone
        // invocation, this method is named separately forcing the cast to be added.
        return new ByteArrayBuilder().appendByte(b);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param byteArray Start with this byte array.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(byte[] byteArray) {
        return new ByteArrayBuilder().append(byteArray);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param text Start with this string.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(String text) {
        return new ByteArrayBuilder().append(text);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param i Start with this integer.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(Integer i) {
        return new ByteArrayBuilder().append(i);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param i Start with this integer.
     * @param numberRepresentation Byte representation of the integer.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(Integer i, ByteConversion.NumberRepresentation numberRepresentation) {
        return new ByteArrayBuilder().append(i, numberRepresentation);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param f Start with this float.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(Float f) {
        return new ByteArrayBuilder().append(f);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param d Start with this double.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(Double d) {
        return new ByteArrayBuilder().append(d);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param l Start with this long.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(Long l) {
        return new ByteArrayBuilder().append(l);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param l Start with this long.
     * @param numberRepresentation Byte representation of the long.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(Long l, ByteConversion.NumberRepresentation numberRepresentation) {
        return new ByteArrayBuilder().append(l, numberRepresentation);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param bi Start with this {@link BigInteger}.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(BigInteger bi) {
        return new ByteArrayBuilder().append(bi);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param bd Start with this {@link BigDecimal}.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(BigDecimal bd) {
        return new ByteArrayBuilder().append(bd);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param instant Start with this {@link Instant}.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(Instant instant) {
        return new ByteArrayBuilder().append(instant);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param zonedDateTime Start with this {@link ZonedDateTime}.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(ZonedDateTime zonedDateTime) {
        return new ByteArrayBuilder().append(zonedDateTime);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param localDate Start with this {@link LocalDate}.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(LocalDate localDate) {
        return new ByteArrayBuilder().append(localDate);
    }

    /**
     * Start defining a byte array (either {@code byte[]} or {@link ByteArray}) using a builder.
     *
     * @param localDate Start with this {@link LocalDate}.
     * @param iso8601DateFormat Byte representation of the {@link LocalDate}.
     * @return A builder for a byte array.
     */
    public static ByteArrayBuilder of(LocalDate localDate, ByteConversion.ISO8601DateFormat iso8601DateFormat) {
        return new ByteArrayBuilder().append(localDate, iso8601DateFormat);
    }

    /**
     * Create an empty {@link ByteArray}.
     *
     * @return A new {@link ByteArray} instance.
     */
    public static ByteArray empty() {
        return new ByteArray(new byte[0]);
    }

    /**
     * Get the wrapped byte array.
     *
     * @return A {@code byte[]}.
     */
    public byte[] get() {
        return this.bytes;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ByteArray that = (ByteArray) other;
        return Arrays.equals(this.bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        if (hash == null) {
            // Cache the hashCode. For larger byte arrays this can safe some time when used repeatedly.
            hash = Arrays.hashCode(bytes);
        }
        return hash;
    }
}