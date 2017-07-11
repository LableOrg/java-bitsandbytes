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

import java.util.List;

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
    public void addNullTest() {
        // Not very strange.
        assertThat(add((byte[]) null), is(new byte[0]));
        // Why would anyone do this?
        assertThat(add((byte[][]) null), is(new byte[0]));
    }

    @Test
    public void addNothingTest() {
        assertThat(add(), is(new byte[0]));
    }

    @Test
    public void addNullArgumentTest() {
        assertThat(add("XXX".getBytes(), null, "YYY".getBytes()), is("XXXYYY".getBytes()));
    }

    @Test
    public void addTest() {
        assertThat(
                add("XXX".getBytes(), "YYY".getBytes(), "ZZZ".getBytes(), "əəə".getBytes()),
                is("XXXYYYZZZəəə".getBytes()));
    }


    @Test
    public void splitNullTest() {
        assertThat(split(null, null), is(nullValue()));
    }

    @Test
    public void splitEmptyTest() {
        List<byte[]> result = split(new byte[]{}, new byte[]{});

        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(new byte[]{}));
    }

    @Test
    public void splitNullDelimiterTest() {
        final byte[] input = new byte[]{0x01, 0x00};

        List<byte[]> result = split(input, null);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(input));

        result = split(input, new byte[0]);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(input));
    }

    @Test
    public void splitOverlongDelimiterTest() {
        final byte[] input = new byte[]{0x01, 0x00};
        final byte[] delimiter = new byte[]{0x02, 0x02, 0x02};

        List<byte[]> result = split(input, delimiter);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(input));
    }

    @Test
    public void splitLimitOfOneTest() {
        final byte[] input = new byte[]{0x01, 0x00};
        final byte[] delimiter = new byte[]{0x02, 0x02, 0x02};

        List<byte[]> result = split(input, delimiter, 1);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(input));

        result = split(input, delimiter, -1);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(input));
    }

    @Test
    public void matchesDelimiterEdgeCase() {
        assertThat(matchesDelimiter(new byte[0], new byte[0], 0), is(false));
    }


    @Test
    public void splitBasicTest() {
        final byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        final byte[] delimiter = new byte[]{0x03, 0x04};

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(new byte[]{0x01, 0x02}));
        assertThat(result.get(1), is(new byte[]{0x05, 0x06}));
    }

    @Test
    public void splitBasicMultipleTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07};
        final byte[] delimiter = new byte[]{0x00};

        List<byte[]> result = split(input, delimiter, 10);

        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(new byte[]{0x01}));
        assertThat(result.get(1), is(new byte[]{0x02, 0x03, 0x04, 0x05, 0x06}));
        assertThat(result.get(2), is(new byte[]{0x07}));
    }

    @Test
    public void splitUnmatchedDelimiterTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07};
        final byte[] delimiter = new byte[]{0x08};

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(input));
    }

    @Test
    public void splitLimitTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07};
        final byte[] delimiter = new byte[]{0x00};

        List<byte[]> result = split(input, delimiter, 2);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(new byte[]{0x01}));
        assertThat(result.get(1), is(new byte[]{0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07}));
    }

    @Test
    public void splitReverseLimitTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x07};
        final byte[] delimiter = new byte[]{0x00};

        List<byte[]> result = split(input, delimiter, -2);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(new byte[]{0x01, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06}));
        assertThat(result.get(1), is(new byte[]{0x07}));
    }

    @Test
    public void splitReverseLimitLongerTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00 ,0x05, 0x00, 0x06};
        final byte[] delimiter = new byte[]{0x00};

        List<byte[]> result = split(input, delimiter, -3);

        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(new byte[]{0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04}));
        assertThat(result.get(1), is(new byte[]{0x05}));
        assertThat(result.get(2), is(new byte[]{0x06}));
    }

    @Test
    public void splitReverseLimitLongerDelimiterTest() {
        final byte[] input = new byte[]{0x01, 0x00, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x00, 0x07};
        final byte[] delimiter = new byte[]{0x00, 0x00};

        List<byte[]> result = split(input, delimiter, -2);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(new byte[]{0x01, 0x00, 0x00, 0x02, 0x03, 0x04, 0x05, 0x06}));
        assertThat(result.get(1), is(new byte[]{0x07}));
    }

    @Test
    public void splitDelimiterSurroundedTest() {
        final byte[] input = new byte[]{0x3A, 0x00, 0x3A, 0x01, 0x02, 0x03, 0x3A, 0x04, 0x3A};
        final byte[] delimiter = new byte[]{0x3A};

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(5));
        assertThat(result.get(0), is(new byte[]{}));
        assertThat(result.get(1), is(new byte[]{0x00}));
        assertThat(result.get(2), is(new byte[]{0x01, 0x02, 0x03}));
        assertThat(result.get(3), is(new byte[]{0x04}));
        assertThat(result.get(4), is(new byte[]{}));
    }

    @Test
    public void splitAllDelimiterSimpleTest() {
        final byte[] input = new byte[]{0x7F};
        final byte[] delimiter = new byte[]{0x7F};

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(new byte[]{}));
        assertThat(result.get(1), is(new byte[]{}));
    }

    @Test
    public void splitAllDelimiterReverseSimpleTest() {
        final byte[] input = new byte[]{0x7F};
        final byte[] delimiter = new byte[]{0x7F};

        List<byte[]> result = split(input, delimiter, -10);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(new byte[]{}));
        assertThat(result.get(1), is(new byte[]{}));
    }

    @Test
    public void splitAllDelimiterMoreTest() {
        final byte[] input = new byte[]{0x5B, 0x5B, 0x5B, 0x5B};
        final byte[] delimiter = new byte[]{0x5B};

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(5));
        assertThat(result.get(0), is(new byte[]{}));
        assertThat(result.get(1), is(new byte[]{}));
        assertThat(result.get(2), is(new byte[]{}));
        assertThat(result.get(3), is(new byte[]{}));
        assertThat(result.get(4), is(new byte[]{}));
    }

    @Test
    public void splitAllDelimiterLongerTest() {
        final byte[] input = new byte[]{0x22, 0x22, 0x22, 0x22};
        final byte[] delimiter = new byte[]{0x22, 0x22};

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(new byte[]{}));
        assertThat(result.get(1), is(new byte[]{}));
        assertThat(result.get(2), is(new byte[]{}));
    }

    @Test
    public void splitAllDelimiterRemainingTest() {
        final byte[] input = new byte[]{0x1A, 0x1A, 0x1A, 0x1A, 0x1A};
        final byte[] delimiter = new byte[]{0x1A, 0x1A};

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(new byte[]{}));
        assertThat(result.get(1), is(new byte[]{}));
        assertThat(result.get(2), is(new byte[]{0x1A}));
    }

    @Test
    public void splitStringsTest() {
        final byte[] input = "test string with spaces.".getBytes();
        final byte[] delimiter = " ".getBytes();

        List<byte[]> result = split(input, delimiter);

        assertThat(result.size(), is(4));
        assertThat(result.get(0), is("test".getBytes()));
        assertThat(result.get(1), is("string".getBytes()));
        assertThat(result.get(2), is("with".getBytes()));
        assertThat(result.get(3), is("spaces.".getBytes()));
    }


    @Test
    public void replaceSimpleTest() {
        byte[] input = "abcdefghijklmnopqrstuvwxyz".getBytes();
        byte[] expected = "abcdefgXXXXXXXXlmnopqrstuvwxyz".getBytes();

        byte[] output = replace(input, "hijk".getBytes(), "XXXXXXXX".getBytes());

        assertThat(output, is(expected));
    }

    @Test
    public void replaceSimpleLargetTargetTest() {
        byte[] input = "abcdefghijklmnopqrstuvwxyz".getBytes();
        byte[] expected = ByteMangler.add("abcdefg".getBytes(), new byte[]{0}, "lmnopqrstuvwxyz".getBytes());

        byte[] output = replace(input, "hijk".getBytes(), new byte[]{0});

        assertThat(output, is(expected));
    }

    @Test
    public void replaceMultipleTest() {
        byte[] input = "abc--def--ghi--jkl--mno--pqr--stu--vwx--yz".getBytes();
        byte[] expected = "abc#def#ghi#jkl#mno#pqr#stu#vwx#yz".getBytes();

        byte[] output = replace(input, "--".getBytes(), "#".getBytes());

        assertThat(output, is(expected));
    }

    @Test
    public void replaceMinimalReplacementTest() {
        byte[] input = "#".getBytes();
        byte[] expected = "".getBytes();

        assertThat(replace(input, "#".getBytes(), "".getBytes()), is(expected));
        assertThat(replace(input, "#".getBytes(), null), is(expected));
    }

    @Test
    public void replaceEmptyReplacementTest() {
        byte[] input = "abcdefghijklmnopqrstuvwxyz".getBytes();
        byte[] expected = "abcdefglmnopqrstuvwxyz".getBytes();

        assertThat(replace(input, "hijk".getBytes(), null), is(expected));
        assertThat(remove(input, "hijk".getBytes()), is(expected));
    }

    @Test
    public void replaceNoActionTest() {
        byte[] input = "abcdefghijklmnopqrstuvwxyz".getBytes();
        byte[] expected = "abcdefghijklmnopqrstuvwxyz".getBytes();

        assertThat(replace(input, "XXX".getBytes(), "ZZZ".getBytes()), is(expected));
    }

    @Test
    public void replaceNullInputTest() {
        assertThat(replace(null, "XXX".getBytes(), "ZZZ".getBytes()), is(nullValue()));
        assertThat(replace("XXX".getBytes(), null, "ZZZ".getBytes()), is("XXX".getBytes()));
    }
}