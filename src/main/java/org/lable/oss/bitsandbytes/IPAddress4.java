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

import java.util.Objects;

/**
 * Representation of an IPv4 address (i.e., 32-bits of data).
 * <p>
 * Use {@link #parse(String)} to instantiate using a canonical IPv4 address notation (e.g., {@code 192.168.0.12}.
 */
public class IPAddress4 extends IPAddress {
    int address;

    /**
     * Instantiate an IP-address object using the primitive used internally for storing the 4 bytes. Use
     * {@link ByteConversion#toIPAddress4(byte[])} instead if you have the bytes in an array.
     *
     * @param address Address value as integer.
     */
    public IPAddress4(int address) {
        this.address = address;
    }

    /**
     * Parse the string notation of an IPv4 address.
     *
     * @param address Address string notation, e.g., {@code 192.168.0.12}
     * @return An IP-address object.
     * @throws IllegalArgumentException Thrown for any illegal notation and {@code null}.
     */
    public static IPAddress4 parse(String address) {
        if (address == null) throw new IllegalArgumentException("Address may not be null.");
        address = trimNonIPChars(address);

        String[] parts = address.split("\\.");
        if (parts.length > 4) throw new IllegalArgumentException("Unrecognized notation: " + address);

        int addressInt = 0;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            int partInt;
            try {
                partInt = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unrecognized notation: " + address + ", failed to parse " + part + "as number.");
            }

            if (i == parts.length - 1) {
                // Final part. This approach allows for the obsolete notation of 127.65530 for 127.0.255.250 to work
                // as well.
                addressInt += partInt;
            } else {
                addressInt += (partInt << ((3 - i) * 8));
            }
        }

        return new IPAddress4(addressInt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPAddress mask(int bitCount, MaskMode maskMode) {
        if (bitCount >= 32) return this;
        if (maskMode == MaskMode.OVERWRITE_WITH_ONES) {
            if (bitCount <= 0) return new IPAddress4(0xFF_FF_FF_FF);

            int value = address | (0xFF_FF_FF_FF >>> bitCount);
            return new IPAddress4(value);
        } else {
            if (bitCount <= 0) return new IPAddress4(0);

            int value = address & (0xFF_FF_FF_FF << (32 - bitCount));
            return new IPAddress4(value);
        }
    }

    /**
     * Get the int representing the 4 bytes of this address.
     *
     * @return An int.
     */
    public int getInt() {
        return address;
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
        return c == '.' || (c >= '0' && c <= '9');
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        IPAddress4 that = (IPAddress4) other;
        return this.address == that.address;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }

    @Override
    public String toString() {
        return String.valueOf((address >> 24) & 0xFF) +
                '.' +
                ((address >> 16) & 0xFF) +
                '.' +
                ((address >> 8) & 0xFF) +
                '.' +
                (address & 0xFF);
    }
}
