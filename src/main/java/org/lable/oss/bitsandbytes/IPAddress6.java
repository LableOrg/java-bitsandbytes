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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Representation of an IPv6 address (i.e., 128-bits of data).
 * <p>
 * Use {@link #parse(String)} to instantiate using a canonical IPv4 address notation (e.g.,
 * {@code 2001:db8:0:1:1:1:1:1}.
 */
public class IPAddress6 extends IPAddress {
    long high;
    long low;

    /**
     * Instantiate an IP-address object using the primitives used internally for storing the 16 bytes. Use
     * {@link ByteConversion#toIPAddress6(byte[])} instead if you have the bytes in an array.
     *
     * @param high First 8 bytes as long.
     * @param low  Last 8 bytes as long.
     */
    public IPAddress6(long high, long low) {
        this.high = high;
        this.low = low;
    }

    /**
     * Parse the string notation of an IPv6 address.
     *
     * @param address Address string notation, e.g., {@code 2001:db8:0:1:1:1:1:1}
     * @return An IP-address object.
     * @throws IllegalArgumentException Thrown for any illegal notation and {@code null}.
     */
    public static IPAddress6 parse(String address) {
        if (address == null) throw new IllegalArgumentException("Address may not be null.");
        address = trimNonIPChars(address);

        int[] blocks = new int[8];
        int currentBlock = 0;
        int i;
        int blockStart = 0;
        int blockDepth = 0;
        for (i = 0; i < address.length(); i++) {
            char c = address.charAt(i);
            if (c == ':') {
                currentBlock++;
                blockDepth = 0;
                blockStart = i + 1;

                if (i == address.length() - 1) throw new IllegalArgumentException("Unrecognized notation: " + address);
                char next = address.charAt(i + 1);
                if (next == ':') {
                    // We've reached "::", the longest run of zero value blocks. Finish by parsing from the back.
                    i++;
                    break;
                }
            } else if (currentBlock == 6 && c == '.') {
                // Handle IPv4-compatible IPv6 address notation.
                IPAddress4 ipv4;
                try {
                    ipv4 = IPAddress4.parse(address.substring(blockStart));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unrecognized notation: " + address);
                }
                blocks[6] = (ipv4.address >> 16) & 0xFFFF;
                blocks[7] = ipv4.address & 0xFFFF;
                currentBlock = 7;
                break;
            } else {
                if (blockDepth == 4) {
                    throw new IllegalArgumentException("Unrecognized notation: " + address + ", contains too large block.");
                }
                blocks[currentBlock] <<= 4;
                if (c >= '0' && c <= '9') {
                    blocks[currentBlock] += (c - 0x30);
                } else if (c >= 'A' && c <= 'Z') {
                    blocks[currentBlock] += (c - 0x37);
                } else if (c >= 'a' && c <= 'z') {
                    blocks[currentBlock] += (c - 0x57);
                } else {
                    throw new IllegalArgumentException("Unrecognized notation: " + address + ", found illegal character '" + c + "'.");
                }
                blockDepth++;
            }
        }

        // From the back, if necessary. This loop can be entered when the address contains a '::' section, which
        // represents a sequence off '0000' blocks. We don't know how many, so we start parsing from the back.
        if (currentBlock < 7) {
            currentBlock = 7;
            blockDepth = 3;
            for (int j = address.length() - 1; j > i; j--) {
                char c = address.charAt(j);
                if (c == ':') {
                    currentBlock--;
                    blockDepth = 3;

                    char prev = address.charAt(j - 1);
                    if (prev == ':') {
                        // We've reached "::" again? Should not be possible.
                        throw new IllegalArgumentException("Unrecognized notation: " + address + ", found two :: sections.");
                    }
                } else if (currentBlock == 7 && c == '.') {
                    // Handle IPv4-compatible IPv6 address notation.
                    j--;
                    while (address.charAt(j) != ':') {
                        j--;
                        if (j == 0) throw new IllegalArgumentException("Unrecognized notation: " + address);
                    }

                    IPAddress4 ipv4;
                    try {
                        ipv4 = IPAddress4.parse(address.substring(j + 1));
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Unrecognized notation: " + address);
                    }
                    blocks[6] = (ipv4.address >> 16) & 0xFFFF;
                    blocks[7] = ipv4.address & 0xFFFF;
                    currentBlock = 5;
                    blockDepth = 3;
                } else {
                    if (blockDepth < 0) {
                        throw new IllegalArgumentException("Unrecognized notation: " + address + ", contains too large block.");
                    }

                    if (c >= '0' && c <= '9') {
                        blocks[currentBlock] += (c - 0x30) << ((3 - blockDepth) * 4);
                    } else if (c >= 'A' && c <= 'Z') {
                        blocks[currentBlock] += (c - 0x37) << ((3 - blockDepth) * 4);
                    } else if (c >= 'a' && c <= 'z') {
                        blocks[currentBlock] += (c - 0x57) << ((3 - blockDepth) * 4);
                    } else {
                        throw new IllegalArgumentException("Unrecognized notation: " + address + ", found illegal character '" + c + "'.");
                    }
                    blockDepth--;
                }
            }
        }

        long high = ((long) blocks[0] << 48) +
                ((long) blocks[1] << 32) +
                ((long) blocks[2] << 16) +
                blocks[3];
        long low = ((long) blocks[4] << 48) +
                ((long) blocks[5] << 32) +
                ((long) blocks[6] << 16) +
                blocks[7];

        return new IPAddress6(high, low);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPAddress mask(int bitCount, MaskMode maskMode) {
        if (bitCount >= 128) return this;
        if (maskMode == MaskMode.OVERWRITE_WITH_ONES) {
            if (bitCount <= 0) return new IPAddress6(0xFFFF_FFFF_FFFF_FFFFL, 0xFFFF_FFFF_FFFF_FFFFL);

            long low = bitCount > 64 ? this.low | (0xFFFF_FFFF_FFFF_FFFFL >>> (bitCount - 64)) : 0xFFFF_FFFF_FFFF_FFFFL;
            long high = bitCount < 64 ? this.high | (0xFFFF_FFFF_FFFF_FFFFL >>> (bitCount)) : this.high;

            return new IPAddress6(high, low);
        } else {
            if (bitCount <= 0) return new IPAddress6(0, 0);

            long low = bitCount > 64 ? this.low & (0xFFFF_FFFF_FFFF_FFFFL << (128 - bitCount)) : 0;
            long high = bitCount < 64 ? this.high & (0xFFFF_FFFF_FFFF_FFFFL << (64 - bitCount)) : this.high;

            return new IPAddress6(high, low);
        }
    }

    /**
     * Get the long representing the first 8 bytes of this address.
     *
     * @return A long.
     */
    public long getHigh() {
        return high;
    }

    /**
     * Get the long representing the last 8 bytes of this address.
     *
     * @return A long.
     */
    public long getLow() {
        return low;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        IPAddress6 that = (IPAddress6) other;
        return this.high == that.high && this.low == that.low;
    }

    @Override
    public int hashCode() {
        return Objects.hash(high, low);
    }

    @Override
    public String toString() {
        int longestZeroBlockStart = 0;
        int longestZeroBlockLength = 0;
        int[] blocks = new int[8];
        blocks[0] = (int) ((high >> 48) & 0xFFFF);
        blocks[1] = (int) ((high >> 32) & 0xFFFF);
        blocks[2] = (int) ((high >> 16) & 0xFFFF);
        blocks[3] = (int) (high & 0xFFFF);
        blocks[4] = (int) ((low >> 48) & 0xFFFF);
        blocks[5] = (int) ((low >> 32) & 0xFFFF);
        blocks[6] = (int) ((low >> 16) & 0xFFFF);
        blocks[7] = (int) (low & 0xFFFF);

        int zeroRun = 0;
        for (int i = 0; i < 8; i++) {
            if (blocks[i] == 0) {
                zeroRun++;
            } else {
                if (zeroRun > 1) {
                    if (zeroRun > longestZeroBlockLength) {
                        longestZeroBlockLength = zeroRun;
                        longestZeroBlockStart = i - zeroRun;
                    }
                }
                zeroRun = 0;
            }
        }

        // Handle run of zeroes at the end.
        if (zeroRun > 1) {
            if (zeroRun > longestZeroBlockLength) {
                longestZeroBlockLength = zeroRun;
                longestZeroBlockStart = 8 - zeroRun;
            }
        }

        if (longestZeroBlockLength > 0) {
            return join(blocks, 0, longestZeroBlockStart) +
                    "::" +
                    join(blocks, longestZeroBlockStart + longestZeroBlockLength, 8);
        } else {
            return join(blocks, 0, 8);
        }
    }

    static String join(int[] blocks, int startInclusive, int stopExclusive) {
        return Arrays
                .stream(blocks, startInclusive, stopExclusive)
                .mapToObj(IPAddress6::visualizeBlock)
                .collect(Collectors.joining(":"));
    }

    static String visualizeBlock(int input) {
        String hex = Hex.encode((byte) ((input >> 8) & 0xFF), true) + Hex.encode((byte) (input & 0xFF), true);
        for (int i = 0; i < 3; i++) {
            if (hex.charAt(i) != '0') return hex.substring(i);
        }
        return hex.substring(3);
    }

    static String trimNonIPChars(String ip) {
        int start = 0;
        int len = ip.length();
        int end = len;

        for (int i = 0; i < len; i++) {
            if (isIPChar(ip.charAt(i))) break;
            start++;
        }

        for (int i = len - 1; i >= start; i--) {
            if (isIPChar(ip.charAt(i))) break;
            end--;
        }

        return start > 0 && end < len ? ip.substring(start, end) : ip;
    }

    static boolean isIPChar(char c) {
        return c == ':' || c == '.'
                || (c >= '0' && c <= '9')
                || (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z');
    }
}
