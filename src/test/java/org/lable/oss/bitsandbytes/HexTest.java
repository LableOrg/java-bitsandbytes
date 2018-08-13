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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.lable.oss.bitsandbytes.Hex.decode;
import static org.lable.oss.bitsandbytes.Hex.encode;

public class HexTest {
    @Test
    public void decodeTest() {
        assertThat(decode("0"), is(new byte[]{0}));
        assertThat(decode("c"), is(new byte[]{12}));

        assertThat(decode("00"), is(new byte[]{0}));
        assertThat(decode("01"), is(new byte[]{1}));
        assertThat(decode("09"), is(new byte[]{9}));
        assertThat(decode("0a"), is(new byte[]{10}));
        assertThat(decode("10"), is(new byte[]{16}));
        assertThat(decode("ff"), is(new byte[]{-1}));
        assertThat(decode("FF"), is(new byte[]{-1}));

        assertThat(decode("f00f"), is(Binary.decode("11110000 00001111")));
    }

    @Test
    public void decodePrefixedTest() {
        assertThat(decode("0xff"), is(new byte[]{-1}));
        assertThat(decode("0xFF"), is(new byte[]{-1}));
        assertThat(decode("0XFF"), is(new byte[]{-1}));
    }

    @Test
    public void decodePrettyInputTest() {
        assertThat(decode("0xCAFE BABE"), is(new byte[]{-54, -2, -70, -66}));
        assertThat(decode("CAFE-BABE"), is(new byte[]{-54, -2, -70, -66}));
        assertThat(decode(" CAFE BABE "), is(new byte[]{-54, -2, -70, -66}));
        assertThat(decode("[cafe][babe]"), is(new byte[]{-54, -2, -70, -66}));
    }

    @Test
    public void encodeTest() {
        assertThat(encode(new byte[]{0}), is("00"));
        assertThat(encode(new byte[]{0, 1}), is("0001"));
        assertThat(encode(new byte[]{-1}), is("FF"));
        assertThat(encode(new byte[]{18, 52, 86, 120, (byte) 144}), is("1234567890"));
        assertThat(encode(new byte[]{-54, -2, -70, -66}), is("CAFEBABE"));
    }
}