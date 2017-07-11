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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Byte and bitwise operations.
 */
public class ByteMangler {

    ByteMangler() {
        // Static utility class.
    }

    /**
     * Reverse the bit-order.
     * <p>
     * So if the input is:
     * <ul>
     *   <li>11111111 00000001</li>
     * </ul>
     * <p>
     * Then the output will be:
     * <ul>
     *   <li>10000000 11111111</li>
     * </ul>
     *
     * @param input Input.
     * @return A new byte array, with the bits reversed.
     */
    public static byte[] reverse(byte[] input) {
        if (input == null) {
            return null;
        }
        byte[] output = new byte[input.length];

        int i = 0;
        while (i < input.length) {
            byte orig = input[i];
            byte out = 0;
            for (int position = 7; position >= 0; position--) {
                out += ((orig & 1) << position);
                orig >>= 1;
            }
            output[input.length - 1 - i] = out;
            i++;
        }

        return output;
    }

    /**
     * Flip all bits.
     * <p>
     * So if the input is:
     * <ul>
     *   <li>11111111 10101010</li>
     * </ul>
     * <p>
     * Then the output will be:
     * <ul>
     *   <li>00000000 01010101</li>
     * </ul>
     *
     * @param input Input.
     * @return A new byte array, with the bits flipped.
     */
    public static byte[] flip(byte[] input) {
        if (input == null) {
            return null;
        }

        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) ~input[i];
        }

        return output;
    }

    /**
     * Reduce a byte array to a certain length, discarding the rest.
     *
     * @param length Target length for the new byte array.
     * @param orig   Input byte array.
     * @return A copy of the original byte array, probably with fewer bytes than before.
     */
    public static byte[] shrink(int length, byte[] orig) {
        if (orig == null) {
            return null;
        }
        if (orig.length <= length) {
            return orig.clone();
        }

        byte[] newBytes = new byte[length];
        System.arraycopy(orig, 0, newBytes, 0, length);
        return newBytes;
    }

    /**
     * Remove a number of bytes from a byte array, starting at the beginning.
     *
     * @param amount Amount of bytes to remove.
     * @param orig   Input byte array.
     * @return A copy of the original byte array, probably with fewer bytes than before.
     */
    public static byte[] chomp(int amount, byte[] orig) {
        if (orig == null) {
            return null;
        }
        if (orig.length <= amount) {
            return new byte[]{};
        }
        byte[] newBytes = new byte[orig.length - amount];
        System.arraycopy(orig, amount, newBytes, 0, orig.length - amount);
        return newBytes;
    }

    /**
     * Flip the first bit of the first byte of a byte array.
     *
     * @param orig Input byte array.
     * @return The original input, with the first bit flipped.
     */
    public static byte[] flipTheFirstBit(byte[] orig) {
        if (orig != null && orig.length > 0) {
            orig[0] = (byte) (orig[0] ^ 1 << 7);
        }
        return orig;
    }

    /**
     * Increment the binary value represented by the byte array by one. If the resulting value causes the array to
     * overflow, a longer array is returned.
     *
     * @param orig Input byte array.
     * @return A copy of the original byte array, possibly one byte longer than it was.
     */
    public static byte[] plusOne(byte[] orig) {
        if (orig == null) {
            return null;
        }

        byte[] out = orig.clone();

        plus:
        {
            for (int i = orig.length - 1; i >= 0; i--) {
                out[i] = (byte) (orig[i] + 1);
                if (out[i] != 0) {
                    break plus;
                }
            }

            // We need an extra byte to facilitate the higher number.
            byte[] withOverflowByte = new byte[out.length + 1];
            withOverflowByte[0] = 0x01;
            System.arraycopy(out, 0, withOverflowByte, 1, out.length);
            out = withOverflowByte;
        }

        return out;
    }

    /**
     * Combine an arbitrary number of byte arrays into a single byte array.
     *
     * @param input Byte arrays to concatenate. Nulls are treated as empty byte arrays.
     * @return A single concatenated byte array.
     */
    public static byte[] add(byte[]... input) {
        if (input == null) return new byte[0];

        int length = 0;
        for (byte[] bytes : input) {
            if (bytes == null) continue;
            length += bytes.length;
        }

        byte [] result = new byte[length];

        int pos = 0;
        for (byte[] bytes : input) {
            if (bytes == null) continue;
            System.arraycopy(bytes, 0, result, pos, bytes.length);
            pos += bytes.length;
        }

        return result;
    }

    /**
     * Split a byte array into parts, splitting at each occurrence of the delimiter. The returned parts do not contain
     * the delimiter itself.
     *
     * @param input     Input.
     * @param delimiter Delimiter to split on.
     * @return The result of the splitting operation.
     * @see #split(byte[], byte[], int)
     */
    public static List<byte[]> split(byte[] input, byte[] delimiter) {
        return split(input, delimiter, 0);
    }

    /**
     * Split a byte array into parts, splitting at each occurrence of the delimiter. The returned parts do not contain
     * the delimiter itself.
     *
     * @param input     Input.
     * @param delimiter Delimiter to split on.
     * @param limit     Limit the number of parts returned to this amount. A limit of 0 implies no limit. A negative
     *                  limit will cause this method to start splitting from the back and limit the number of parts to
     *                  the absolute value of limit.
     * @return The result of the splitting operation.
     * @see #split(byte[], byte[])
     */
    public static List<byte[]> split(byte[] input, byte[] delimiter, int limit) {
        if (input == null) return null;
        // No point in splitting if at most one part should be returned.
        if (limit == 1 || limit == -1) return Collections.singletonList(input);

        if (delimiter == null || delimiter.length == 0 || delimiter.length > input.length) {
            // Without a delimiter the whole of the input byte array is returned, because there is nothing to split.
            return Collections.singletonList(input);
        }

        List<byte[]> parts = new ArrayList<>();
        if (limit >= 0) {
            int rangeStart = 0;
            int i = 0;
            // Don't loop over every byte in the array, but stop at the last possible starting byte of the delimiter.
            while (i < input.length - (delimiter.length - 1)) {
                if (matchesDelimiter(delimiter, input, i)) {
                    parts.add(Arrays.copyOfRange(input, rangeStart, i));
                    rangeStart = i + delimiter.length;

                    // Stop one part before the limit, because the last part is whatever remains.
                    if (limit > 0 && parts.size() >= limit - 1) break;

                    i += delimiter.length;
                } else {
                    i++;
                }
            }

            // Add whatever remains as the last part.
            parts.add(chomp(rangeStart, input));
        } else {
            // Reverse split.

            limit = -limit;
            int rangeEnd = input.length;
            // Don't loop over every byte in the array, but start at the first possible starting byte of the delimiter.
            int i = input.length - delimiter.length;
            while (i >= 0) {
                if (matchesDelimiter(delimiter, input, i)) {
                    parts.add(0, Arrays.copyOfRange(input, i + delimiter.length, rangeEnd));
                    rangeEnd = i;

                    // Stop one part before the limit, because the last part is whatever remains.
                    if (parts.size() >= limit - 1) break;

                    i -= delimiter.length;
                } else {
                    i--;
                }
            }

            // Add whatever remains as the first part.
            parts.add(0, shrink(rangeEnd, input));
        }

        return parts;
    }

    // Private helper method.
    static boolean matchesDelimiter(byte[] delimiter, byte[] rawKey, int offset) {
        for (int j = 0; j < delimiter.length; j++) {
            if (rawKey[offset + j] != delimiter[j]) {
                return false;
            }
            if (j == delimiter.length - 1) {
                // Matched!
                return true;
            }
        }
        return false;
    }

    /**
     * Remove all occurrences of {@code target} from the input.
     *
     * @param input  Input.
     * @param target Removal target.
     * @return The input byte array, minus any occurrence of {@code target}.
     */
    public static byte[] remove(byte[] input, byte[] target) {
        return replace(input, target, null);
    }

    /**
     * Replace occurrences of a byte sequence with another byte sequence.
     *
     * @param input       Input.
     * @param target      Replacement target.
     * @param replacement Replacement.
     * @return The input byte array, with any occurrence of {@code target} replaced by @{code replacement}.
     */
    public static byte[] replace(byte[] input, byte[] target, byte[] replacement) {
        if (target == null || input == null) return input;
        if (replacement == null) replacement = new byte[0];

        List<byte[]> parts = split(input, target);

        // Target not found in input string.
        if (parts.size() < 2) return input;

        int length = input.length + ((parts.size() - 1) * (replacement.length - target.length));
        byte[] output = new byte[length];
        int current = 0;
        for (byte[] part : parts) {
            System.arraycopy(part, 0, output, current, part.length);
            current += part.length;

            if (current >= output.length) break;

            System.arraycopy(replacement, 0, output, current, replacement.length);
            current += replacement.length;
        }

        return output;
    }
}
