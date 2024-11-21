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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IPAddressTest {
    @Test
    public void compareToTest() {
        IPAddress4 one = IPAddress4.parse("0.0.0.0");
        IPAddress4 two = IPAddress4.parse("0.0.0.1");
        IPAddress6 three = IPAddress6.parse("2001:db8:0:1:1:1:1:1");
        IPAddress6 four = IPAddress6.parse("fe80::b336:6660:c18a:6903");

        assertThat(one.compareTo(one), is(0));
        assertThat(one.compareTo(two), is(-1));
        assertThat(one.compareTo(three), is(-1));
        assertThat(one.compareTo(four), is(-1));
        assertThat(two.compareTo(one), is(1));
        assertThat(four.compareTo(three), is(1));
        assertThat(four.compareTo(one), is(1));

        assertThat(one.isBefore(three), is(true));
        assertThat(one.isAfter(three), is(false));
        assertThat(four.isAfter(one), is(true));
        assertThat(four.isBefore(one), is(false));
        assertThat(three.isBefore(three), is(false));
        assertThat(three.isAfter(three), is(false));
    }
}