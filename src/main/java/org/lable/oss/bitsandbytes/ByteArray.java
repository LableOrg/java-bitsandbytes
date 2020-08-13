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