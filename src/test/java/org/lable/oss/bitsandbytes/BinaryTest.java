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
import static org.lable.oss.bitsandbytes.Binary.encode;
import static org.lable.oss.bitsandbytes.Binary.decode;

public class BinaryTest {
    @Test
    public void decodeBasicTest() {
        assertThat(decode("00000000"), is(new byte[]{0}));
        assertThat(decode("11111111"), is(new byte[]{-1}));
        assertThat(decode("1"), is(new byte[]{1}));
        assertThat(decode("11"), is(new byte[]{3}));
        assertThat(decode("11110000"), is(new byte[]{-16}));
        assertThat(decode("1010101001010101"), is(new byte[]{-86, 85}));
    }

    @Test
    public void encodeWithDecorationTest() {
        assertThat(decode("0000 0000"), is(new byte[]{0}));
        assertThat(decode("[11111111] [00000000]"), is(new byte[]{-1, 0}));
        assertThat(decode("100 00000000"), is(new byte[]{4, 0}));
        assertThat(decode("00000100_00000000"), is(new byte[]{4, 0}));
    }

    @Test
    public void decodeNullTest() {
        assertThat(decode(null), is(new byte[]{}));
    }

    @Test
    public void decodeEmptyTest() {
        assertThat(decode(""), is(new byte[]{}));
        assertThat(decode(" "), is(new byte[]{}));
    }


    @Test
    public void encodeNullTest() {
        assertThat(encode(null), is("NULL"));
    }

    @Test
    public void encodeEmptyTest() {
        assertThat(encode(new byte[]{}), is(""));
    }

    @Test
    public void encodeBasicTest() {
        assertThat(encode(new byte[]{0}), is("00000000"));
        assertThat(encode(new byte[]{-1}), is("11111111"));
        assertThat(encode(new byte[]{(byte) 0b11111111}), is("11111111"));
        assertThat(encode(new byte[]{1}), is("00000001"));
        assertThat(encode(new byte[]{3}), is("00000011"));
        assertThat(encode(new byte[]{-16}), is("11110000"));
        assertThat(encode(new byte[]{-86, 85}), is("1010101001010101"));
    }

    @Test
    public void encodeDecoratedTest() {
        assertThat(encode(new byte[]{0}, true, false), is("[00000000]"));
        assertThat(encode(new byte[]{0}, true, true), is("[00000000]"));
        assertThat(encode(new byte[]{0}, false, false), is("00000000"));
        assertThat(encode(new byte[]{0}, false, true), is("00000000"));
        assertThat(encode(new byte[]{64, 0}, false, true), is("01000000 00000000"));
        assertThat(encode(new byte[]{64, 0}, true, true), is("[01000000] [00000000]"));
        assertThat(encode(new byte[]{64, 0}, false, false), is("0100000000000000"));
        assertThat(encode(new byte[]{64, 0}, true, false), is("[01000000][00000000]"));
    }

    @Test
    public void oneHundredPercentCodeCoverageObsession() {
        // Because 99% code coverage is vexing.
        new ByteConversion();
    }
}