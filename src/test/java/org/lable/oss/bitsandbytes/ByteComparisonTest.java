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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.lable.oss.bitsandbytes.ByteComparison.contains;
import static org.lable.oss.bitsandbytes.ByteComparison.endsWith;
import static org.lable.oss.bitsandbytes.ByteComparison.startsWith;

public class ByteComparisonTest {
    @Test
    public void startsWithTest() {
        assertThat(startsWith(null, null), is(false));
        assertThat(startsWith(new byte[0], null), is(false));
        assertThat(startsWith(null, new byte[0]), is(false));
        assertThat(startsWith(new byte[0], new byte[0]), is(true));

        assertThat(startsWith(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,5,6}), is(false));
        assertThat(startsWith(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,5}), is(true));
        assertThat(startsWith(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4}), is(true));
        assertThat(startsWith(new byte[]{1,2,3,4,5}, new byte[]{1,2,3}), is(true));
        assertThat(startsWith(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,3}), is(false));
        assertThat(startsWith(new byte[]{1,2,3,4,5}, new byte[0]), is(true));

        byte[] in = new byte[]{0,1,2};
        assertThat(startsWith(in, in), is(true));
    }

    @Test
    public void endsWithTest() {
        assertThat(endsWith(null, null), is(false));
        assertThat(endsWith(new byte[0], null), is(false));
        assertThat(endsWith(null, new byte[0]), is(false));
        assertThat(endsWith(new byte[0], new byte[0]), is(true));

        assertThat(endsWith(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,5,6}), is(false));
        assertThat(endsWith(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,5}), is(true));
        assertThat(endsWith(new byte[]{1,2,3,4,5}, new byte[]{2,3,4,5}), is(true));
        assertThat(endsWith(new byte[]{1,2,3,4,5}, new byte[]{3,4,5}), is(true));
        assertThat(endsWith(new byte[]{1,2,3,4,5}, new byte[]{3,4,5,5}), is(false));
        assertThat(endsWith(new byte[]{1,2,3,4,5}, new byte[0]), is(true));

        byte[] in = new byte[]{0,1,2};
        assertThat(endsWith(in, in), is(true));
    }

    @Test
    public void containsTest() {
        assertThat(contains(null, null), is(false));
        assertThat(contains(new byte[0], null), is(false));
        assertThat(contains(null, new byte[0]), is(false));
        assertThat(contains(new byte[0], new byte[0]), is(true));

        assertThat(contains(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,5,6}), is(false));
        assertThat(contains(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,5}), is(true));
        assertThat(contains(new byte[]{1,2,3,4,5}, new byte[]{2,3,4,5}), is(true));
        assertThat(contains(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4}), is(true));
        assertThat(contains(new byte[]{1,2,3,4,5}, new byte[]{3,4,5}), is(true));
        assertThat(contains(new byte[]{1,2,3,4,5}, new byte[]{3,4,5,5}), is(false));
        assertThat(contains(new byte[]{1,2,3,4,5}, new byte[0]), is(true));

        byte[] in = new byte[]{0,1,2};
        assertThat(contains(in, in), is(true));
    }

    @Test
    public void equalsTest() {
        assertThat(ByteComparison.equals(null, null), is(true));
        assertThat(ByteComparison.equals(new byte[]{1,2,3,4,5}, null), is(false));
        assertThat(ByteComparison.equals(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,5}), is(true));
        assertThat(ByteComparison.equals(new byte[]{1,2,3,4,5}, new byte[]{1,2,3,4,}), is(false));
    }
}