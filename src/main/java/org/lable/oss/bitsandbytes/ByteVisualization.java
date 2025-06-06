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

        @Override
        public byte parseByte(String input) {
            if (input == null) return 0;
            for (char c : input.toCharArray()) {
                if (c >= '\u2800' && c <= '\u28FF') {
                    return (byte) (c - '\u2800');
                }
            }
            return 0;
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
                    output[byteCounter] |= (byte) (1 << (7 - bitCounter));
                }

                bitCounter++;
                if (bitCounter == 8) {
                    byteCounter++;
                    bitCounter = 0;
                }
            }
            return output;
        }

        @Override
        public byte parseByte(String input) {
            byte out = 0;
            int bitCounter = 7;
            char[] in = input.toCharArray();
            for (int i = in.length - 1; i >= 0; i--) {
                char bit = in[i];
                if (bit == '1') {
                    out |= (byte) (1 << (7 - bitCounter));
                    bitCounter--;
                } else if (bit == '0') {
                    bitCounter--;
                }
            }
            return out;
        }
    },
    /**
     * Represent a byte as a hexadecimal string, e.g., {@code C0} for {@code 192}.
     */
    HEXADECIMAL {
        @Override
        public String visualize(byte input) {
            int high = input >> 4 & 0x0F;
            int low = input & 0x0F;
            return new String(new char[]{
                    (high > 9 ? (char) (high + 0x37) : (char) (high + 0x30)),
                    (low > 9 ? (char) (low + 0x37) : (char) (low + 0x30))
            });
        }

        @Override
        public byte[] parse(String input) {
            if (input == null) return new byte[0];
            input = input.trim();

            if (input.startsWith("0x") || input.startsWith("0X")) {
                input = input.substring(2);
            }

            StringBuilder cleanInput = new StringBuilder();
            for (char c : input.toCharArray()) {
                // Strip out anything that isn't one of the sixteen hex chars.
                if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) {
                    cleanInput.append(c);
                } else if (c >= 'A' && c <= 'F') {
                    // Turn uppercase A–F into lowercase a–f.
                    cleanInput.append((char) (c + 0x20));
                }
            }

            char[] chars;
            if (cleanInput.length() % 2 == 0) {
                chars = cleanInput.toString().toCharArray();
            } else {
                // Prepend a '0' if the input string has an odd number of characters.
                chars = new char[cleanInput.length() + 1];
                chars[0] = '0';
                cleanInput.getChars(0, cleanInput.length(), chars, 1);
            }

            int targetLength = chars.length / 2;
            byte[] output = new byte[targetLength];

            for (int i = 0; i < chars.length / 2; i++) {
                output[i] = (byte) (value(chars[i * 2]) << 4 ^ value(chars[i * 2 + 1]));
            }

            return output;
        }

        @Override
        public byte parseByte(String input) {
            input = input.trim();

            if (input.startsWith("0x") || input.startsWith("0X")) {
                input = input.substring(2);
            }

            boolean doHigh = true;
            boolean setLow = false;
            int high = 0;
            int low = 0;
            for (char c : input.toCharArray()) {
                // Ignore anything that isn't one of the sixteen hex chars.
                if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) {
                    if (doHigh) {
                        high = value(c);
                        doHigh = false;
                    } else {
                        low = value(c);
                        setLow = true;
                    }
                } else if (c >= 'A' && c <= 'F') {
                    // Turn uppercase A–F into lowercase a–f.
                    c = (char) (c + 0x20);
                    if (doHigh) {
                        high = value(c);
                        doHigh = false;
                    } else {
                        low = value(c);
                        setLow = true;
                    }
                }
            }

            // If only one character was parsed, treat it as the lower four bits.
            // This makes '0x0F', '0F', and  'F', all equal.
            return setLow
                    ? (byte) (high << 4 ^ low)
                    : (byte) high;
        }

        private int value(char c) {
            return c >= '0' && c <= '9' ? c - 0x30 : c - 0x57;
        }
    },
    HEXADECIMAL_LOWER {
        @Override
        public String visualize(byte input) {
            int high = input >> 4 & 0x0F;
            int low = input & 0x0F;
            return new String(new char[]{
                    (high > 9 ? (char) (high + 0x57) : (char) (high + 0x30)),
                    (low > 9 ? (char) (low + 0x57) : (char) (low + 0x30))
            });
        }

        @Override
        public byte[] parse(String input) {
            return HEXADECIMAL.parse(input);
        }

        @Override
        public byte parseByte(String input) {
            return HEXADECIMAL.parseByte(input);
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

        @Override
        public byte parseByte(String input) {
            Integer firstOfPair = null;
            for (char c : input.toCharArray()) {
                int value = isValid(c);
                // Skip all unparsable characters.
                if (value >= 0) {
                    if (firstOfPair == null) {
                        firstOfPair = value;
                    } else {
                        return (byte) (firstOfPair << 4 | value);
                    }
                }
            }

            return 0;
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
     * Turn a visualization of a byte into the byte it represents.
     *
     * @param input A string visualization.
     * @return A byte.
     */
    public abstract byte parseByte(String input);

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
