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
 * Convert binary string representations to byte arrays and vice versa.
 */
public class Binary {

    Binary() {
        // Static utility class.
    }

    /**
     * Convert a binary string representation of a byte array to that byte array. This method purposely ignores any
     * characters that are not 1 or 0, so you can use spaces and any other punctuation deemed suitable to improve
     * readability.
     * <p>
     * The following input strings all return the same byte array:
     * <ul>
     * <li>"0000001000001111"</li>
     * <li>"00000010_00001111"</li>
     * <li>"[00000010] [00001111]"</li>
     * <li>"10 00001111"</li>
     * </ul>
     *
     * @param binary Input string.
     * @return The corresponding byte array.
     */
    public static byte[] decode(String binary) {
        return ByteVisualization.ONES_AND_ZEROES.parse(binary);
    }

    /**
     * Encode a byte array as its binary string representation.
     *
     * @param input Input byte array.
     * @return A string of ones and zeroes, or "NULL" if the input is null.
     */
    public static String encode(byte[] input) {
        return ByteVisualization.ONES_AND_ZEROES.visualize(input);
    }

    /**
     * Encode a byte array as its binary string representation.
     *
     * @param input             Input byte array.
     * @param wrapBytesInBraces If true, every byte will be surrounded by braces.
     * @param separateBytes     If true, every byte will be separated by a single space.
     * @return A string of ones and zeroes, or "NULL" if the input is null.
     */
    public static String encode(byte[] input, boolean wrapBytesInBraces, boolean separateBytes) {
        String binary = encode(input);

        if (!(wrapBytesInBraces || separateBytes)) {
            // Why even call this method if you don't want any pretty formatting?
            return binary;
        }

        int byteCount = binary.length() / 8;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < byteCount; i++) {
            if (i != 0 && separateBytes) {
                output.append(' ');
            }
            if (wrapBytesInBraces) {
                output.append('[');
            }
            output.append(binary, 8 * i, (8 * i) + 8);
            if (wrapBytesInBraces) {
                output.append(']');
            }
        }

        return output.toString();
    }
}
