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
import static org.lable.oss.bitsandbytes.IPAddress.MaskMode.OVERWRITE_WITH_ONES;
import static org.lable.oss.bitsandbytes.IPAddress.MaskMode.OVERWRITE_WITH_ZEROES;

public class IPAddress4Test {
    @Test
    public void toStringTest() {
        assertThat(new IPAddress4(16777216).toString(), is("1.0.0.0"));
        assertThat(new IPAddress4(0).toString(), is("0.0.0.0"));
        assertThat(new IPAddress4(1).toString(), is("0.0.0.1"));
        assertThat(new IPAddress4(256).toString(), is("0.0.1.0"));
        assertThat(new IPAddress4(256 * 256).toString(), is("0.1.0.0"));
        assertThat(new IPAddress4(-1).toString(), is("255.255.255.255"));
    }

    @Test
    public void parseTest() {
        assertThat(IPAddress4.parse("1.0.0.0").address, is(16777216));
        assertThat(IPAddress4.parse("0.0.0.1").address, is(1));

        assertThat(IPAddress4.parse("1.0.0.0").address, is(16777216));
        assertThat(IPAddress4.parse("127.0.255.250").address, is(2130771962));
        assertThat(IPAddress4.parse("127.65530"), is(IPAddress4.parse("127.0.255.250")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseNullTest() {
        IPAddress4.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseBogus1Test() {
        IPAddress4.parse("10.0.hamster.2");
    }

    @Test
    public void compareTest() {
        IPAddress4 one = IPAddress4.parse("0.0.0.0");
        IPAddress4 two = IPAddress4.parse("0.0.0.1");
        IPAddress4 three = IPAddress4.parse("127.0.0.1");
        IPAddress4 four = IPAddress4.parse("192.168.172.3");
        IPAddress4 five = IPAddress4.parse("192.168.172.4");

        assertThat(one.compareTo(one), is(0));
        assertThat(one.compareTo(two), is(-1));
        assertThat(one.compareTo(three), is(-1));
        assertThat(one.compareTo(four), is(-1));
        assertThat(two.compareTo(one), is(1));
        assertThat(four.compareTo(three), is(1));
        assertThat(one.compareTo(five), is(-1));
    }

    @Test
    public void maskTest() {
        assertThat(IPAddress4.parse("255.255.255.255").mask(32), is(IPAddress4.parse("255.255.255.255")));
        assertThat(IPAddress4.parse("255.255.255.255").mask(31), is(IPAddress4.parse("255.255.255.254")));
        assertThat(IPAddress4.parse("255.255.255.254").mask(31), is(IPAddress4.parse("255.255.255.254")));
        assertThat(IPAddress4.parse("255.255.255.255").mask(1), is(IPAddress4.parse("128.0.0.0")));
        assertThat(IPAddress4.parse("128.0.0.0").mask(1), is(IPAddress4.parse("128.0.0.0")));
        assertThat(IPAddress4.parse("255.255.255.255").mask(0), is(IPAddress4.parse("0.0.0.0")));

        assertThat(IPAddress4.parse("192.168.77.12").mask(24), is(IPAddress4.parse("192.168.77.0")));
        assertThat(IPAddress4.parse("192.168.77.12").mask(24, null), is(IPAddress4.parse("192.168.77.0")));
        assertThat(IPAddress4.parse("192.168.77.12").mask(24, OVERWRITE_WITH_ZEROES), is(IPAddress4.parse("192.168.77.0")));

        assertThat(IPAddress4.parse("0.0.0.0").mask(32, OVERWRITE_WITH_ONES), is(IPAddress4.parse("0.0.0.0")));
        assertThat(IPAddress4.parse("0.0.0.0").mask(31, OVERWRITE_WITH_ONES), is(IPAddress4.parse("0.0.0.1")));
        assertThat(IPAddress4.parse("0.0.0.1").mask(31, OVERWRITE_WITH_ONES), is(IPAddress4.parse("0.0.0.1")));
        assertThat(IPAddress4.parse("0.0.0.0").mask(1, OVERWRITE_WITH_ONES), is(IPAddress4.parse("127.255.255.255")));
        assertThat(IPAddress4.parse("0.0.0.0").mask(0, OVERWRITE_WITH_ONES), is(IPAddress4.parse("255.255.255.255")));

        assertThat(IPAddress4.parse("192.168.77.12").mask(24, OVERWRITE_WITH_ONES), is(IPAddress4.parse("192.168.77.255")));
    }
}