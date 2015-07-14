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

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.lable.oss.bitsandbytes.ByteMangler.*;

public class ByteManglerTest {
    @Test
    public void reverseTestZero() {
        assertThat(reverse(Binary.decode("00000000")), is(Binary.decode("00000000")));
    }

    @Test
    public void reverseTestOnes() {
        assertThat(reverse(Binary.decode("11111111")), is(Binary.decode("11111111")));
    }

    @Test
    public void reverseTestSimpleA() {
        assertThat(reverse(Binary.decode("10000000")), is(Binary.decode("00000001")));
    }

    @Test
    public void reverseTestSimpleB() {
        assertThat(reverse(Binary.decode("00000001")), is(Binary.decode("10000000")));
    }

    @Test
    public void reverseTestMulti() {
        assertThat(
                reverse(Binary.decode("10000000 11111111 01010101")),
                is(Binary.decode("10101010 11111111 00000001")));
    }

    @Test
    public void reverseTestNull() {
        assertThat(reverse(null), is(nullValue()));
    }

    @Test
    public void reverseTestEmpty() {
        assertThat(reverse(new byte[] {}), is(new byte[] {}));
    }


    @Test
    public void flipTestZero() {
        assertThat(flip(Binary.decode("00000000")), is(Binary.decode("11111111")));
    }

    @Test
    public void flipTestOnes() {
        assertThat(flip(Binary.decode("11111111")), is(Binary.decode("00000000")));
    }

    @Test
    public void flipTestSimpleA() {
        assertThat(flip(Binary.decode("10000000")), is(Binary.decode("01111111")));
    }


    @Test
    public void flipTestSimpleB() {
        assertThat(flip(Binary.decode("00000001")), is(Binary.decode("11111110")));
    }

    @Test
    public void flipTestMulti() {
        assertThat(
                flip(Binary.decode("10000000 11111111 01010101")),
                is(Binary.decode("01111111 00000000 10101010")));
    }

    @Test
    public void flipTestNull() {
        assertThat(flip(null), is(nullValue()));
    }

    @Test
    public void flipTestEmpty() {
        assertThat(flip(new byte[]{}), is(new byte[]{}));
    }



    @Test
    public void shrinkTestNull() {
        assertThat(shrink(10, null), is(nullValue()));
    }

    @Test
    public void shrinkTestEmpty() {
        assertThat(shrink(10, new byte[]{}), is(new byte[]{}));
    }

    @Test
    public void shrinkTestBasic() {
        final byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        final byte[] expected = new byte[]{0x01, 0x02};

        assertThat(shrink(2, input), is(expected));
    }

    @Test
    public void shrinkTestMoreThanAvailable() {
        final byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};

        assertThat(shrink(7, input), is(input));
    }


    @Test
    public void chompTestNull() {
        assertThat(chomp(10, null), is(nullValue()));
    }

    @Test
    public void chompTestEmpty() {
        assertThat(chomp(10, new byte[]{}), is(new byte[]{}));
    }

    @Test
    public void chompTestBasic() {
        final byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        final byte[] expected = new byte[]{0x03, 0x04, 0x05, 0x06};

        assertThat(chomp(2, input), is(expected));
    }

    @Test
    public void chompTestZero() {
        final byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};

        assertThat(chomp(0, input), is(input));
    }

    @Test
    public void chompTestMoreThanAvailable() {
        final byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        final byte[] expected = new byte[]{};

        assertThat(chomp(7, input), is(expected));
    }


    @Test
    public void flipTheFirstBitBasicTest() {
        byte[] input = Binary.decode("00000001");
        flipTheFirstBit(input);
        assertThat(input, is(Binary.decode("10000001")));

        input = Binary.decode("11110001");
        flipTheFirstBit(input);
        assertThat(input, is(Binary.decode("01110001")));

        input = Binary.decode("11110001 00000000");
        flipTheFirstBit(input);
        assertThat(input, is(Binary.decode("01110001 00000000")));
    }

    @Test
    public void flipTheFirstBitNullTest() {
        assertThat(flipTheFirstBit(null), is(nullValue()));
    }

    @Test
    public void flipTheFirstBitEmptyTest() {
        assertThat(flipTheFirstBit(new byte[]{}), is(new byte[]{}));
    }


    @Test
    public void plusOneTest() {
        byte[] input = Binary.decode("11110001 00000000");
        byte[] output = plusOne(input);
        assertThat(output, is(Binary.decode("11110001 00000001")));

        input = Binary.decode("11111111 11111111");
        output = plusOne(input);
        assertThat(output, is(Binary.decode("00000001 00000000 00000000")));

        output = plusOne(new byte[] {});
        assertThat(output, is(Binary.decode("00000001")));
    }

    @Test
    public void plusOneNullTest() {
        assertThat(plusOne(null), is(nullValue()));
    }


    @Test
    public void splitNullTest() {
        assertThat(split(null, null), is(nullValue()));
    }

    @Test
    public void splitEmptyTest() {
        SplitResult result = split(new byte[]{}, new byte[]{});
        assertThat(result.getLeft(), is(new byte[]{}));
        assertThat(result.getRight(), is(new byte[]{}));
    }

    @Test
    public void splitNullDelimiterTest() {
        final byte[] input = new byte[]{0x01, 0x00};

        SplitResult result = split(null, input);
        assertThat(result.getLeft(), is(input));
        assertThat(result.getRight(), is(new byte[0]));

        result = split(new byte[0], input);
        assertThat(result.getLeft(), is(input));
        assertThat(result.getRight(), is(new byte[0]));
    }

    @Test
    public void splitOverlongDelimiterTest() {
        final byte[] input = new byte[]{0x01, 0x00};
        final byte[] delimiter = new byte[]{0x02, 0x02, 0x02};

        SplitResult result = split(delimiter, input);
        assertThat(result.getLeft(), is(input));
        assertThat(result.getRight(), is(new byte[0]));
    }

    @Test
    public void matchesDelimiterEdgeCase() {
        assertThat(matchesDelimiter(new byte[0], new byte[0], 0), is(false));
    }


    @Test
    public void splitBasicTest() {
        final byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        final byte[] delimiter = new byte[]{0x03, 0x04};

        SplitResult result = split(delimiter, input);
        assertThat(result.getLeft(), is(new byte[]{0x01, 0x02}));
        assertThat(result.getRight(), is(new byte[]{0x05, 0x06}));
    }

    @Test
    public void splitBasicMultipleTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07};
        final byte[] delimiter = new byte[]{0x00};

        SplitResult result = split(delimiter, input);
        assertThat(result.getLeft(), is(new byte[]{0x01}));
        assertThat(result.getRight(), is(new byte[]{0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07}));
    }

    @Test
    public void splitUnmatchedDelimiterTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07};
        final byte[] delimiter = new byte[]{0x08};

        SplitResult result = split(delimiter, input);
        assertThat(result.getLeft(), is(input));
        assertThat(result.getRight(), is(new byte[0]));
    }


    @Test
    public void oneHundredPercentCodeCoverageObsession() {
        // Because 99% code coverage is unbearable.
        new ByteMangler();
    }
}