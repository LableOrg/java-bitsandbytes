/**
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
import static org.lable.oss.bitsandbytes.BitMask.bitMask;
import static org.lable.oss.bitsandbytes.BitMask.byteMask;

public class BitMaskTest {
    // BitMask#byteMask

    @Test
    public void byteMaskSimpleTest() {
        byte[] expected = new byte[]{0x0, 0x0, 0x1, 0x0, 0x1, 0x1};
        byte[] result = byteMask(2, 1, 1, 2);

        assertThat(result, is(expected));
    }

    @Test
    public void byteMaskSimpleTestStartWithOnes() {
        byte[] expected = new byte[]{0x1, 0x1, 0x1, 0x0, 0x1, 0x1};
        byte[] result = byteMask(0, 3, 1, 2);

        assertThat(result, is(expected));
    }

    @Test
    public void byteMaskNullTest() {
        assertThat(byteMask(null), is(new byte[0]));
        assertThat(byteMask(), is(new byte[0]));
    }

    // BitMask#bitMask

    @Test
    public void bitMaskSimpleTest() {
        byte[] expected = Binary.decode("00001011");
        byte[] result = bitMask(2, 1, 1, 2);

        assertThat(Binary.encode(result), is(Binary.encode(expected)));
    }

    @Test
    public void bitMaskSimpleTestStartWithOnes() {
        byte[] expected = Binary.decode("11101111");
        byte[] result = bitMask(0, 3, 1, 4);

        assertThat(Binary.encode(result), is(Binary.encode(expected)));
    }

    @Test
    public void bitMaskMultiByteTest() {
        byte[] expected = Binary.decode("11000000 11101111");
        byte[] result = bitMask(0, 2, 6, 3, 1, 4);

        assertThat(Binary.encode(result), is(Binary.encode(expected)));
    }

    @Test
    public void bitMaskNullTest() {
        assertThat(bitMask(null), is(new byte[0]));
        assertThat(bitMask(), is(new byte[0]));
    }

    // For code coverage.

    @Test
    public void oneHundredPercentCodeCoverageObsession() {
        // Because 99% code coverage is unbearable.
        new BitMask();
    }
}