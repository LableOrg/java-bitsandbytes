/*
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

import java.util.Arrays;

/**
 * Compare {@code byte[]} instances.
 */
public class ByteComparison {

    ByteComparison() {
        // Static utility class.
    }

    /**
     * Find out if a byte array starts with another byte array.
     *
     * @param input  Input byte array.
     * @param prefix Byte array that the input may start with.
     * @return True if {@code input} starts with the exact same bytes as {@code prefix}.
     */
    public static boolean startsWith(byte[] input, byte[] prefix) {
        if (input == null || prefix == null || prefix.length > input.length) {
            return false;
        }

        if (input == prefix) return true;

        for (int i = 0; i < prefix.length; i++) {
            if (input[i] != prefix[i]) return false;
        }

        return true;
    }

    /**
     * Find out if a byte array ends with another byte array.
     *
     * @param input  Input byte array.
     * @param suffix Byte array that the input may end in.
     * @return True if {@code input} ends with the exact same bytes as {@code suffix}.
     */
    public static boolean endsWith(byte[] input, byte[] suffix) {
        if (input == null || suffix == null || suffix.length > input.length) {
            return false;
        }

        if (input == suffix) return true;

        int offset = input.length - suffix.length;
        for (int i = suffix.length - 1; i >= 0; i--) {
            if (input[offset + i] != suffix[i]) return false;
        }

        return true;
    }

    /**
     * Find out if a byte array contains another byte array.
     *
     * @param input Input byte array.
     * @param sub   Byte array that the input may contain.
     * @return True if {@code sub} is present in its entirety within {@code input}.
     */
    public static boolean contains(byte[] input, byte[] sub) {
        if (input == null || sub == null || sub.length > input.length) {
            return false;
        }

        if (input == sub || input.length == 0) return true;

        for (int i = 0; i <= input.length - sub.length; i++) {
            test:
            {
                for (int j = 0; j < sub.length; j++) {
                    if (input[i + j] != sub[j]) {
                        break test;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Compare two byte arrays for equality. This is just an alias for {@link Arrays#equals(byte[], byte[])}.
     *
     * @param a One byte array.
     * @param b Another byte array.
     * @return True if the two arrays are equal (same length, same bytes), or if both are {@code null}.
     */
    public static boolean equals(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
}
