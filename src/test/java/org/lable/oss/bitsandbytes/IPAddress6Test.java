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

public class IPAddress6Test {
    @Test
    public void toStringTest() {
        assertThat(new IPAddress6(0, 16777216).toString(), is("::100:0"));
        assertThat(new IPAddress6(0, 0).toString(), is("::"));
        assertThat(new IPAddress6(0, 1).toString(), is("::1"));
        assertThat(new IPAddress6(1, 1).toString(), is("::1:0:0:0:1"));
        assertThat(new IPAddress6(0, 256 * 256).toString(), is("::1:0"));
        assertThat(new IPAddress6(0, -1).toString(), is("::ffff:ffff:ffff:ffff"));
        assertThat(new IPAddress6(-1, -1).toString(), is("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
    }

    @Test
    public void parseTest() {
        assertThat(IPAddress6.parse("::").high, is(0L));
        assertThat(IPAddress6.parse("::").low, is(0L));

        assertThat(IPAddress6.parse("::1").high, is(0L));
        assertThat(IPAddress6.parse("::1").low, is(1L));

        assertThat(IPAddress6.parse("1::1").high, is(281474976710656L));
        assertThat(IPAddress6.parse("1::1").low, is(1L));

        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").high, is(-1L));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").low, is(-1L));

        assertThat(IPAddress6.parse("::ffff:c000:0280").high, is(0L));
        assertThat(IPAddress6.parse("::ffff:c000:0280").low, is(281473902969472L));

        assertThat(IPAddress6.parse("::ffff:192.0.2.128").high, is(0L));
        assertThat(IPAddress6.parse("::ffff:192.0.2.128").low, is(281473902969472L));
        assertThat(IPAddress6.parse("::ffff:192.0.2.128").toString(), is("::ffff:c000:280"));

        // Round-trip.
        assertThat(IPAddress6.parse("fe80::b336:6660:c18a:6903").toString(), is("fe80::b336:6660:c18a:6903"));
    }

    @Test
    public void compareTest() {
        IPAddress6 one = IPAddress6.parse("::");
        IPAddress6 two = IPAddress6.parse("::1");
        IPAddress6 three = IPAddress6.parse("2001:db8:0:1:1:1:1:1");
        IPAddress6 four = IPAddress6.parse("fe80::b336:6660:c18a:6903");
        IPAddress6 five = IPAddress6.parse("::ffff:6660:c18a:6903");

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
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").mask(128), is(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").mask(127), is(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:fffe")));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:fffe").mask(127), is(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:fffe")));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").mask(64), is(IPAddress6.parse("ffff:ffff:ffff:ffff::")));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").mask(63), is(IPAddress6.parse("ffff:ffff:ffff:fffe::")));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").mask(63, null), is(IPAddress6.parse("ffff:ffff:ffff:fffe::")));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").mask(63, OVERWRITE_WITH_ZEROES), is(IPAddress6.parse("ffff:ffff:ffff:fffe::")));
        assertThat(IPAddress6.parse("8000::").mask(1), is(IPAddress6.parse("8000::")));
        assertThat(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff").mask(0), is(IPAddress6.parse("::")));

        assertThat(IPAddress6.parse("2001:db8:85a3::8a2e:370:7334").mask(112), is(IPAddress6.parse("2001:0db8:85a3::8a2e:370:0")));

        assertThat(IPAddress6.parse("::").mask(128, OVERWRITE_WITH_ONES), is(IPAddress6.parse("::")));
        assertThat(IPAddress6.parse("::").mask(127, OVERWRITE_WITH_ONES), is(IPAddress6.parse("::1")));
        assertThat(IPAddress6.parse("::1").mask(127, OVERWRITE_WITH_ONES), is(IPAddress6.parse("::1")));
        assertThat(IPAddress6.parse("::").mask(64, OVERWRITE_WITH_ONES), is(IPAddress6.parse("::ffff:ffff:ffff:ffff")));
        assertThat(IPAddress6.parse("::").mask(63, OVERWRITE_WITH_ONES), is(IPAddress6.parse("::1:ffff:ffff:ffff:ffff")));
        assertThat(IPAddress6.parse("8000::").mask(1, OVERWRITE_WITH_ONES), is(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));
        assertThat(IPAddress6.parse("::").mask(1, OVERWRITE_WITH_ONES), is(IPAddress6.parse("7fff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));
        assertThat(IPAddress6.parse("::").mask(0, OVERWRITE_WITH_ONES), is(IPAddress6.parse("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertThat(IPAddress6.parse("2001:db8:85a3::8a2e:370:7334").mask(112, OVERWRITE_WITH_ONES), is(IPAddress6.parse("2001:0db8:85a3::8a2e:370:ffff")));
    }
}