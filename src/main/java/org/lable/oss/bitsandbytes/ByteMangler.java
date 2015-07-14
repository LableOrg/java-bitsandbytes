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
 * Byte and bitwise operations.
 */
public class ByteMangler {

    ByteMangler() {
        // Static utility class.
    }

    /**
     * Reverse the bit-order.
     * <p/>
     * So if the input is:
     * <ul>
     * <li>11111111 00000001</li>
     * </ul>
     * <p/>
     * Then the output will be:
     * <ul>
     * <li>10000000 11111111</li>
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
     * <p/>
     * So if the input is:
     * <ul>
     * <li>11111111 10101010</li>
     * </ul>
     * <p/>
     * Then the output will be:
     * <ul>
     * <li>00000000 01010101</li>
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
     * Split a byte array into two parts, at the first occurrence of the delimiter. The resulting two parts do not
     * contain the delimiter at which the split took place.
     * <p/>
     * If the delimiter is not encountered, the left-hand side of the return value will be the input array.
     *
     * @param delimiter Delimiter to split on.
     * @param rawKey    Input.
     * @return The result of the splitting operation.
     */
    public static SplitResult split(byte[] delimiter, byte[] rawKey) {
        if (rawKey == null) {
            return null;
        }
        if (delimiter == null || delimiter.length == 0 || delimiter.length > rawKey.length) {
            // Without a delimiter the whole of the input byte array is left of the (nonexistent) delimiter.
            return new SplitResult(rawKey, new byte[]{});
        }

        // Don't loop over every byte in the array, but stop at the last possible starting byte of the delimiter.
        for (int i = 0; i < rawKey.length - (delimiter.length - 1); i++) {
            if (matchesDelimiter(delimiter, rawKey, i)) {
                return new SplitResult(shrink(i, rawKey), chomp(i + delimiter.length, rawKey));
            }
        }

        return new SplitResult(rawKey, new byte[]{});
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
     * Result of a byte array splitting operation. A tuple, essentially.
     */
    public static class SplitResult {
        private final byte[] left;
        private final byte[] right;

        SplitResult(byte[] left, byte[] right) {
            this.left = left;
            this.right = right;
        }

        /**
         * @return Left part of the split.
         */
        public byte[] getLeft() {
            return left;
        }

        /**
         * @return Right part of the split.
         */
        public byte[] getRight() {
            return right;
        }
    }
}
