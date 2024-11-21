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

import java.util.Comparator;

/**
 * Representation of an IP address. {@link #parse(String)} may be used to instantiate either an {@link IPAddress4} or an
 * {@link IPAddress6}.
 */
public abstract class IPAddress implements Comparable<IPAddress> {
    /**
     * Parse the string notation of an IPv4 or IPv6 address.
     *
     * @param address Address string notation, e.g., {@code 192.168.0.12} or {@code 2001:db8:0:1:1:1:1:1}.
     * @return An IP-address object.
     * @throws IllegalArgumentException Thrown for any illegal notation and {@code null}.
     */
    public static IPAddress parse(String address) {
        if (address == null) throw new IllegalArgumentException("Address may not be null.");

        return address.contains(":")
                ? IPAddress6.parse(address)
                : IPAddress4.parse(address);
    }

    /**
     * Apply a mask to an IP address and return the base network this expresses.
     * <p>
     * For example, an IPv4 address of {@code 192.168.77.23} with {@code bitCount} {@code 24} results in
     * {@code 192.168.77.0}.
     *
     * @param bitCount Number of bits kept, starting from the highest bit.
     * @return The IP address with the appropriate number of bits zeroed out from the end.
     */
    public IPAddress mask(int bitCount) {
        return mask(bitCount, MaskMode.OVERWRITE_WITH_ZEROES);
    }

    /**
     * Apply a mask to an IP address and return the resulting address this expresses.
     * <p>
     * For example, an IPv4 address of {@code 192.168.77.23} with {@code bitCount} {@code 24}, and {@code maskMode}
     * {@code OVERWRITE_WITH_ONES} results in {@code 192.168.77.255}.
     *
     * @param bitCount Number of bits kept, starting from the highest bit.
     * @param maskMode Whether to overwrite the masked section with ones or zeroes.
     * @return The IP address with the appropriate number of bits zeroed out from the end.
     */
    public abstract IPAddress mask(int bitCount, MaskMode maskMode);

    /**
     * Convenience method for checking if this is an instance of {@link IPAddress4}.
     *
     * @return True, if this is an IPv4 address.
     */
    public boolean isIPV4() {
        return this instanceof IPAddress4;
    }

    /**
     * Convenience method for checking if this is an instance of {@link IPAddress6}.
     *
     * @return True, if this is an IPv6 address.
     */
    public boolean isIPV6() {
        return this instanceof IPAddress6;
    }

    @Override
    public int compareTo(IPAddress other) {
        if (other == null) return -1;

        return Comparator
                // Sort IPv4 before IPv6.
                .comparing(IPAddress::isIPV6)
                .thenComparing((a, b) -> {
                    if (a.isIPV4()) {
                        IPAddress4 ip4a = (IPAddress4) a;
                        IPAddress4 ip4b = (IPAddress4) b;

                        // Flip the first bit, because of two's complement.
                        return Integer.compare(
                                ip4a.address ^ 0x8000_0000,
                                ip4b.address ^ 0x8000_0000
                        );
                    } else {
                        IPAddress6 ip6a = (IPAddress6) a;
                        IPAddress6 ip6b = (IPAddress6) b;
                        // Flip the first bit, because of two's complement.
                        int upperCmp = Long.compare(
                                ip6a.high ^ 0x8000_0000_0000_0000L,
                                ip6b.high ^ 0x8000_0000_0000_0000L
                        );
                        return upperCmp == 0
                                ? Long.compare(
                                ip6a.low ^ 0x8000_0000_0000_0000L,
                                ip6b.low ^ 0x8000_0000_0000_0000L
                        )
                                : upperCmp;
                    }
                })
                .compare(this, other);
    }

    /**
     * Check if this address sorts before another. IPv4 addresses sort before IPv6 addresses.
     *
     * @param other Other address.
     * @return True, if this address sorts before the other.
     */
    public boolean isBefore(IPAddress other) {
        return compareTo(other) < 0;
    }

    /**
     * Check if this address sorts after another. IPv6 addresses sort after IPv4 addresses.
     *
     * @param other Other address.
     * @return True, if this address sorts after the other.
     */
    public boolean isAfter(IPAddress other) {
        return compareTo(other) > 0;
    }

    /**
     * Specify how a bitmask is applied.
     */
    public enum MaskMode {
        /**
         * All masked bits will be overwritten with zeroes.
         */
        OVERWRITE_WITH_ZEROES,
        /**
         * All masked bits will be overwritten with ones.
         */
        OVERWRITE_WITH_ONES,
        ;
    }
}
