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
 * Create bit or byte masks, where respectively each bit or the bytes 0x0 and 0x1 represent the mask, from a pattern
 * specification.
 */
public class BitMask {

    BitMask() {
        // Static utility class.
    }

    /**
     * Convert a pattern description into a byte array where each byte (not bit) is either a 0 or a 1. This format is
     * used by the {@code FuzzyRowFilter} in HBase.
     * <p>
     * Through the pattern passed as argument to this method you specify the alternating groups of ones and zeroes,
     * so {@code byteMask(2, 4, 1)} returns a byte array containing {@code 0x00 0x00 0x01 0x01 0x01 0x01 0x00}.
     * <p>
     * To start the mask with ones, pass 0 as the first number in the pattern.
     *
     * @param pattern The mask pattern, alternately specifying the length of the groups of zeroes and ones.
     * @return A byte array.
     */
    public static byte[] byteMask(int... pattern) {
        if (pattern == null) {
            return new byte[0];
        }

        int length = 0;
        for (int blockLength : pattern) {
            length += blockLength;
        }
        byte[] mask = new byte[length];

        int blockOffset = 0;
        boolean writeZero = true;
        for (int blockLength : pattern) {
            for (int i = 0; i < blockLength; i++) {
                mask[blockOffset + i] = (byte) (writeZero ? 0x00 : 0x01);
            }
            blockOffset += blockLength;
            writeZero = !writeZero;
        }

        return mask;
    }

    /**
     * Convert a pattern description into a byte array where the pattern is represented by its bits.
     * <p>
     * Through the pattern passed as argument to this method you specify the alternating groups of ones and zeroes,
     * so {@code byteMask(8, 8)} returns a byte arrays containing {@code 0x00 0xFF}. If the pattern is not cleanly
     * divisible by eight, the bitmask returned will be padded with zeroes. So {@code byteMask(0, 4)} returns
     * {@code 0x0F}.
     * <p>
     * To start the mask with ones, pass 0 as the first number in the pattern.
     *
     * @param pattern The mask pattern, alternately specifying the length of the groups of zeroes and ones.
     * @return A byte array.
     */
    public static byte[] bitMask(int... pattern) {
        if (pattern == null) {
            return new byte[0];
        }

        int length = 0;
        for (int blockLength : pattern) {
            length += blockLength;
        }

        boolean cleanlyDivisible = length % 8 == 0;

        // Start at an offset when the pattern is not exactly divisible by 8.
        int blockOffset = cleanlyDivisible ? 0 : 8 - (length % 8);
        byte[] mask = new byte[(length / 8) + (cleanlyDivisible ? 0 : 1)];

        boolean writeZero = true;
        for (int blockLength : pattern) {
            if (!writeZero) {
                for (int i = 0; i < blockLength; i++) {
                    int bytePosition = (blockOffset + i) / 8;
                    int bitPosition = (blockOffset + i) % 8;
                    mask[bytePosition] = (byte) (mask[bytePosition] ^ 1 << (7 - bitPosition));
                }
            }
            blockOffset += blockLength;
            writeZero = !writeZero;
        }

        return mask;
    }
}
