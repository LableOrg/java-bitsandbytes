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

import java.io.ByteArrayOutputStream;

/**
 * Visualize byte arrays in a variety of ways.
 */
public enum ByteVisualization {
    /**
     * Use the braille Unicode character range (U+2800–U+28FF) to visualize a byte. The braille dots correspond to the
     * bits set.
     */
    BRAILLE {
        @Override
        public String visualize(byte input) {
            int offset = input < 0 ? 256 - Math.abs(input) : input;
            return Character.toString((char) ('\u2800' + offset));
        }

        @Override
        public byte[] parse(String input) {
            if (input == null) return new byte[0];
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            input.chars()
                    // Skip any unparsable characters.
                    .filter(c -> c >= '\u2800' && c <= '\u28FF')
                    .map(c -> (byte) c - '\u2800')
                    .forEach(baos::write);
            return baos.toByteArray();
        }
    },
    /**
     * Represent a byte as a string of ones and zeroes, e.g., {@code 11000000} for {@code 192}.
     */
    ONES_AND_ZEROES {
        @Override
        public String visualize(byte input) {
            char[] out = new char[8];
            for (int i = 7; i > -1; i--) {
                out[7 - i] = (input >> i & 0x1) == 1 ? '1' : '0';
            }
            return new String(out);
        }

        @Override
        public byte[] parse(String input) {
            if (input == null) return new byte[0];

            StringBuilder cleanInput = new StringBuilder();
            for (char c : input.toCharArray()) {
                // Strip out anything that isn't a 1 or 0.
                if (c == '0' || c == '1') {
                    cleanInput.append(c);
                }
            }

            char[] chars = cleanInput.toString().toCharArray();

            int targetLength = chars.length / 8;
            int remainder = chars.length % 8;
            if (remainder != 0) {
                targetLength += 1;
            }

            byte[] output = new byte[targetLength];
            // If the input was not neatly divisible by 8, pad out the first byte with 0s by starting later in the loop.
            int bitCounter = remainder == 0 ? 0 : 8 - remainder;
            int byteCounter = 0;
            for (char bit : chars) {
                if (bit == '1') {
                    output[byteCounter] |= 1 << (7 - bitCounter);
                }

                bitCounter++;
                if (bitCounter == 8) {
                    byteCounter++;
                    bitCounter = 0;
                }
            }
            return output;
        }
    },
    /**
     * Use Unicode Block Elements to represent each byte using two glyphs.
     */
    SQUARES {
        // 0..F mapped to a character from the Block Elements range.
        final char[] GLYPHS = new char[]{' ', '▘', '▝', '▀', '▖', '▌', '▞', '▛', '▗', '▚', '▐', '▜', '▄', '▙', '▟', '█'};

        // Map each character in the Block Elements range to its byte value (if applicable).
        // Invalid characters are mapped to -1. The 0 byte is represented by space (U+0020),
        // which lies outside of this block.
        final byte[] VALUES = new byte[]{
                3, -1, -1, -1, 0xC, -1, -1, -1, 0xF, -1, -1, -1, 5, -1, -1, -1,
                0xA, -1, -1, -1, -1, -1, 4, 8, 1, 0xD, 9, 7, 0xB, 2, 6, 0xE
        };

        @Override
        public String visualize(byte input) {
            return Character.toString(GLYPHS[(input & 0xff) >> 4]) + Character.toString(GLYPHS[input & 0xf]);
        }

        @Override
        public byte[] parse(String input) {
            if (input == null) return new byte[0];

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Integer firstOfPair = null;
            for (char c : input.toCharArray()) {
                int value = isValid(c);
                // Skip all unparsable characters.
                if (value >= 0) {
                    if (firstOfPair == null) {
                        firstOfPair = value;
                    } else {
                        baos.write(firstOfPair << 4 | value);
                        firstOfPair = null;
                    }
                }
            }

            return baos.toByteArray();
        }

        int isValid(char c) {
            if (c == ' ') return 0;
            if (c >= '\u2580' && c <= '\u259F') return VALUES[c - '\u2580'];
            return -1;
        }
    };

    /**
     * Visualize a byte with a printable string.
     *
     * @param input A byte.
     * @return A printable string.
     */
    public abstract String visualize(byte input);

    /**
     * Turn a visualization of a byte array into the byte array it represents.
     *
     * @param input A string visualization.
     * @return A byte array.
     */
    public abstract byte[] parse(String input);

    /**
     * Visualize a byte array with a printable string.
     *
     * @param input A byte array.
     * @return A printable string.
     */
    public String visualize(byte[] input) {
        if (input == null) {
            return "NULL";
        }

        // This method could be overridden if needed, but currently all visualization
        // techniques can work on a single byte.
        StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(visualize(b));
        }
        return builder.toString();
    }
}
