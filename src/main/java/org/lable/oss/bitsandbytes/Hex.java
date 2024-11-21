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

/**
 * Convert hexadecimal string representations to byte arrays and vice versa.
 */
public class Hex {
    Hex() {
        // Static utility class.
    }

    /**
     * Convert a hexadecimal string representation of a byte array to that byte array. This method purposely ignores any
     * characters that are not part of {@code 0-9A-Fa-f}, so you can use spaces and any other punctuation deemed
     * suitable to improve readability.
     * <p>
     * The following input strings all return the same byte array:
     * <ul>
     * <li>"CAFEBABE"</li>
     * <li>"CAFE_BABE"</li>
     * <li>"[CAFE] [BABE]"</li>
     * <li>"cafebabe"</li>
     * <li>"0xCAFEBABE"</li>
     * </ul>
     *
     * @param hexadecimal Input string.
     * @return The corresponding byte array.
     */
    public static byte[] decode(String hexadecimal) {
        return ByteVisualization.HEXADECIMAL.parse(hexadecimal);
    }

    /**
     * Convert a hexadecimal string representation of a byte to that byte. This method purposely ignores any
     * characters that are not part of {@code 0-9A-Fa-f}, so you can use spaces and any other punctuation deemed
     * suitable to improve readability.
     * <p>
     * The following input strings all return the same byte:
     * <ul>
     * <li>"FF"</li>
     * <li>"ff"</li>
     * <li>"[FF]"</li>
     * <li>"0xFF"</li>
     * </ul>
     *
     * @param hexadecimal Input string.
     * @return The corresponding byte.
     */
    public static byte decodeByte(String hexadecimal) {
        return ByteVisualization.HEXADECIMAL.parseByte(hexadecimal);
    }

    /**
     * Encode a byte array as its hexadecimal string representation.
     *
     * @param input Input byte array.
     * @return A hexadecimal string, or "NULL" if the input is null.
     */
    public static String encode(byte[] input) {
        return ByteVisualization.HEXADECIMAL.visualize(input);
    }
}
