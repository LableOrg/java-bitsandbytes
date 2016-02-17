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

/**
 * Static utility methods for printing byte sequences.
 *
 * @see #utf8Escaped(byte[])
 */
public class BytePrinter {
    static final char[] HEX_CHARS =
            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    // UTF-8.
    static final byte SINGLE_BYTE_CHAR_MASK = -128; // 10000000.
    static final byte SINGLE_BYTE_CHAR_LEADER = 0b00000000;

    static final byte DOUBLE_BYTE_CHAR_MASK = -32; // 11100000.
    static final byte DOUBLE_BYTE_VALUE_CHAR_MASK = 0b00011111;
    static final byte DOUBLE_BYTE_CHAR_LEADER = -64; // 11000000.

    static final byte TRIPLE_BYTE_CHAR_MASK = -16; // 11110000.
    static final byte TRIPLE_BYTE_VALUE_CHAR_MASK = 0b00001111;
    static final byte TRIPLE_BYTE_CHAR_LEADER = -32; //11100000.

    static final byte QUADRUPLE_BYTE_CHAR_MASK = -8; // 11111000.
    static final byte QUADRUPLE_BYTE_VALUE_CHAR_MASK = 0b00000111;
    static final byte QUADRUPLE_BYTE_CHAR_LEADER = -16; // 11110000.

    static final byte CONTINUATION_BYTE_CHAR_MASK = -64; // 11000000.
    static final byte CONTINUATION_BYTE_VALUE_CHAR_MASK = 0b00111111;
    static final byte CONTINUATION_BYTE_CHAR_LEADER = -128; // 10000000.

    BytePrinter() {
        // Static utility class.
    }

    /**
     * Convert a sequence of bytes into a printable string, where valid UTF-8 printable characters are output as-is,
     * and non-printable characters and any other bytes not representing printable text are output in an escaped
     * hexadecimal notation — e.g., {@code \xff}.
     *
     * @param input Input bytes.
     * @return Printable string.
     */
    public static String utf8Escaped(byte[] input) {
        StringBuilder builder = new StringBuilder();

        int pos = 0;
        while (pos < input.length) {
            pos += printCharacter(builder, input, pos);
        }

        return builder.toString();
    }

    /**
     * Process one or more bytes and turn them into printable characters or escaped hexadecimal sequences.
     *
     * @param builder String builder to append to.
     * @param input   Input bytes.
     * @param pos     Current position in the input bytes.
     * @return The number of bytes processed.
     */
    static int printCharacter(StringBuilder builder, byte[] input, int pos) {
        byte firstByte = input[pos];

        // The four valid byte notations for UTF-8 characters.
        if ((firstByte & SINGLE_BYTE_CHAR_MASK) == SINGLE_BYTE_CHAR_LEADER) {
            if (isPrintableCharacter(firstByte)) {
                builder.append((char) firstByte);
                return 1;
            } else {
                return writeEscaped(builder, firstByte);
            }
        } else if ((firstByte & DOUBLE_BYTE_CHAR_MASK) == DOUBLE_BYTE_CHAR_LEADER) {
            if (input.length < pos + 2) return writeEscaped(builder, firstByte);

            if (isContinuationByte(input[pos + 1])) {
                int c = firstByte & DOUBLE_BYTE_VALUE_CHAR_MASK;
                c <<= 6;
                c |= input[pos + 1] & CONTINUATION_BYTE_VALUE_CHAR_MASK;

                if (isPrintableCharacter(c)) {
                    builder.append((char) c);
                    return 2;
                }
            }
        } else if ((firstByte & TRIPLE_BYTE_CHAR_MASK) == TRIPLE_BYTE_CHAR_LEADER) {
            if (input.length < pos + 3) return writeEscaped(builder, firstByte);

            if (isContinuationByte(input[pos + 1]) &&
                    isContinuationByte(input[pos + 2])) {
                int c = firstByte & TRIPLE_BYTE_VALUE_CHAR_MASK;
                c <<= 6;
                c |= input[pos + 1] & CONTINUATION_BYTE_VALUE_CHAR_MASK;
                c <<= 6;
                c |= input[pos + 2] & CONTINUATION_BYTE_VALUE_CHAR_MASK;

                if (isPrintableCharacter(c)) {
                    builder.append((char) c);
                    return 3;
                }
            }
        } else if ((firstByte & QUADRUPLE_BYTE_CHAR_MASK) == QUADRUPLE_BYTE_CHAR_LEADER) {
            if (input.length < pos + 4) return writeEscaped(builder, firstByte);

            if (isContinuationByte(input[pos + 1]) &&
                    isContinuationByte(input[pos + 2]) &&
                    isContinuationByte(input[pos + 3])) {
                int c = firstByte & QUADRUPLE_BYTE_VALUE_CHAR_MASK;
                c <<= 6;
                c |= input[pos + 1] & CONTINUATION_BYTE_VALUE_CHAR_MASK;
                c <<= 6;
                c |= input[pos + 2] & CONTINUATION_BYTE_VALUE_CHAR_MASK;
                c <<= 6;
                c |= input[pos + 3] & CONTINUATION_BYTE_VALUE_CHAR_MASK;

                if (isPrintableCharacter(c)) {
                    // Characters outside of Unicode's BMP are composed of two chars in Java.
                    builder.append(Character.highSurrogate(c)).append(Character.lowSurrogate(c));
                    return 4;
                }
            }
        }

        // For any other byte, treat it as a raw byte value that should be printed in the escaped hexadecimal notation.
        return writeEscaped(builder, firstByte);
    }

    static boolean isContinuationByte(byte b) {
        return (b & CONTINUATION_BYTE_CHAR_MASK) == CONTINUATION_BYTE_CHAR_LEADER;
    }

    static boolean isPrintableCharacter(int c) {
        int type = Character.getType(c);
        return Character.isDefined(c) &&
                !Character.isISOControl(c) &&
                type != Character.FORMAT &&
                type != Character.SPACE_SEPARATOR &&
                type != Character.LINE_SEPARATOR &&
                type != Character.PARAGRAPH_SEPARATOR;
    }

    static int writeEscaped(StringBuilder builder, byte... bytes) {
        int i = 0;
        for (byte b : bytes) {
            builder.append(escapeByte(b));
            i++;
        }
        return i;
    }

    /**
     * Turn a byte into the corresponding escaped hexadecimal notation.
     *
     * @param b Input byte.
     * @return The escaped hexadecimal string.
     */
    public static String escapeByte(byte b) {
        return "\\x" + HEX_CHARS[b >> 4 & 15] + HEX_CHARS[b & 15];
    }
}
