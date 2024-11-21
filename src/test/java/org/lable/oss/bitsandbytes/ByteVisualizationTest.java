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
import static org.lable.oss.bitsandbytes.ByteVisualization.*;

public class ByteVisualizationTest {
    @Test
    public void brailleVisualizeTest() {
        assertThat(BRAILLE.visualize((byte) 0), is("\u2800"));
        assertThat(BRAILLE.visualize((byte) 1), is("\u2801"));
        assertThat(BRAILLE.visualize((byte) 2), is("\u2802"));
        assertThat(BRAILLE.visualize((byte) 4), is("\u2804"));
        assertThat(BRAILLE.visualize((byte) 8), is("\u2808"));
        assertThat(BRAILLE.visualize((byte) 16), is("\u2810"));
        assertThat(BRAILLE.visualize((byte) 32), is("\u2820"));
        assertThat(BRAILLE.visualize((byte) 64), is("\u2840"));
        assertThat(BRAILLE.visualize((byte) 127), is("\u287F"));
        assertThat(BRAILLE.visualize((byte) 128), is("\u2880"));
        assertThat(BRAILLE.visualize((byte) 255), is("\u28FF"));

        assertThat(BRAILLE.visualize(new byte[]{0, 1, 2}), is("\u2800\u2801\u2802"));
    }

    @Test
    public void brailleParseTest() {
        assertThat(BRAILLE.parse("\u2800"), is(new byte[]{(byte) 0}));
        assertThat(BRAILLE.parse("\u2801"), is(new byte[]{(byte) 1}));
        assertThat(BRAILLE.parse("\u2802"), is(new byte[]{(byte) 2}));
        assertThat(BRAILLE.parse("\u2804"), is(new byte[]{(byte) 4}));
        assertThat(BRAILLE.parse("\u2808"), is(new byte[]{(byte) 8}));
        assertThat(BRAILLE.parse("\u2810"), is(new byte[]{(byte) 16}));
        assertThat(BRAILLE.parse("\u2820"), is(new byte[]{(byte) 32}));
        assertThat(BRAILLE.parse("\u2840"), is(new byte[]{(byte) 64}));
        assertThat(BRAILLE.parse("\u287F"), is(new byte[]{(byte) 127}));
        assertThat(BRAILLE.parse("\u2800\u287F"), is(new byte[]{0, 127}));
        assertThat(BRAILLE.parse("\u2880"), is(new byte[]{(byte) 128}));
        assertThat(BRAILLE.parse("\u28FF"), is(new byte[]{(byte) 255}));
        assertThat(BRAILLE.parse("\u28FEbogus\u2880"), is(new byte[]{(byte) 254, (byte) 128}));
        assertThat(BRAILLE.parse("bogus"), is(new byte[0]));
        assertThat(BRAILLE.parse(""), is(new byte[0]));
        assertThat(BRAILLE.parse(null), is(new byte[0]));
    }

    @Test
    public void brailleParseByteTest() {
        assertThat(BRAILLE.parseByte("\u2800"), is((byte) 0));
        assertThat(BRAILLE.parseByte("\u2840"), is((byte) 64));
        assertThat(BRAILLE.parseByte(""), is((byte) 0));
        assertThat(BRAILLE.parseByte(null), is((byte) 0));
    }

    @Test
    public void onesAndZeroesVisualizeTest() {
        // Limited. See BinaryTest for the full suite.
        assertThat(ONES_AND_ZEROES.visualize(new byte[]{0}), is("00000000"));
    }

    @Test
    public void onesAndZeroesParseTest() {
        // Limited. See BinaryTest for the full suite.
        assertThat(ONES_AND_ZEROES.parse("11110000"), is(new byte[]{-16}));
    }

    @Test
    public void onesAndZeroesParseByteTest() {
        assertThat(ONES_AND_ZEROES.parseByte("11110000"), is((byte) -16));
        assertThat(ONES_AND_ZEROES.parseByte("00000001"), is((byte) 1));
        assertThat(ONES_AND_ZEROES.parseByte(" [00000001] "), is((byte) 1));
        assertThat(ONES_AND_ZEROES.parseByte("01"), is((byte) 1));
    }

    @Test
    public void hexadecimalVisualizeTest() {
        // Limited. See BinaryTest for the full suite.
        assertThat(HEXADECIMAL.visualize(new byte[]{0}), is("00"));
    }

    @Test
    public void hexadecimalParseTest() {
        // Limited. See BinaryTest for the full suite.
        assertThat(HEXADECIMAL.parse("F0"), is(new byte[]{-16}));
    }

    @Test
    public void hexadecimalByteParseTest() {
        assertThat(HEXADECIMAL.parseByte("0"), is((byte) 0));
        assertThat(HEXADECIMAL.parseByte("00"), is((byte) 0));
        assertThat(HEXADECIMAL.parseByte("0x00"), is((byte) 0));

        assertThat(HEXADECIMAL.parseByte("F0"), is((byte) -16));
        assertThat(HEXADECIMAL.parseByte("0F"), is((byte) 15));
        assertThat(HEXADECIMAL.parseByte("F"), is((byte) 15));

        assertThat(HEXADECIMAL.parseByte("0"), is((byte) 0));
        assertThat(HEXADECIMAL.parseByte("1"), is((byte) 1));
        assertThat(HEXADECIMAL.parseByte("2"), is((byte) 2));
        assertThat(HEXADECIMAL.parseByte("3"), is((byte) 3));
        assertThat(HEXADECIMAL.parseByte("4"), is((byte) 4));
        assertThat(HEXADECIMAL.parseByte("5"), is((byte) 5));
        assertThat(HEXADECIMAL.parseByte("6"), is((byte) 6));
        assertThat(HEXADECIMAL.parseByte("7"), is((byte) 7));
        assertThat(HEXADECIMAL.parseByte("8"), is((byte) 8));
        assertThat(HEXADECIMAL.parseByte("9"), is((byte) 9));
        assertThat(HEXADECIMAL.parseByte("A"), is((byte) 10));
        assertThat(HEXADECIMAL.parseByte("B"), is((byte) 11));
        assertThat(HEXADECIMAL.parseByte("C"), is((byte) 12));
        assertThat(HEXADECIMAL.parseByte("D"), is((byte) 13));
        assertThat(HEXADECIMAL.parseByte("E"), is((byte) 14));
        assertThat(HEXADECIMAL.parseByte("F"), is((byte) 15));

        assertThat(HEXADECIMAL.parseByte("10"), is((byte) 16));
        assertThat(HEXADECIMAL.parseByte("FF"), is((byte) 255));
        assertThat(HEXADECIMAL.parseByte("fF"), is((byte) 255));
        assertThat(HEXADECIMAL.parseByte("-FF-"), is((byte) 255));
    }

    @Test
    public void squaresVisualizeTest() {
        assertThat(SQUARES.visualize((byte) 0), is("  "));
        assertThat(SQUARES.visualize((byte) 1), is(" \u2598"));
        assertThat(SQUARES.visualize((byte) 2), is(" \u259D"));
        assertThat(SQUARES.visualize((byte) 4), is(" \u2596"));
        assertThat(SQUARES.visualize((byte) 8), is(" \u2597"));
        assertThat(SQUARES.visualize((byte) 16), is("\u2598 "));
        assertThat(SQUARES.visualize((byte) 32), is("\u259D "));
        assertThat(SQUARES.visualize((byte) 64), is("\u2596 "));
        assertThat(SQUARES.visualize((byte) 127), is("\u259b\u2588"));
        assertThat(SQUARES.visualize((byte) 128), is("\u2597 "));
        assertThat(SQUARES.visualize((byte) 255), is("\u2588\u2588"));

        assertThat(SQUARES.visualize(new byte[]{0, 1, 2}), is("   \u2598 \u259D"));
    }

    @Test
    public void squaresParseTest() {
        assertThat(SQUARES.parse("  "), is(new byte[]{(byte) 0}));
        assertThat(SQUARES.parse(" \u2598"), is(new byte[]{(byte) 1}));
        assertThat(SQUARES.parse(" \u259D"), is(new byte[]{(byte) 2}));
        assertThat(SQUARES.parse(" \u2596"), is(new byte[]{(byte) 4}));
        assertThat(SQUARES.parse(" \u2597"), is(new byte[]{(byte) 8}));
        assertThat(SQUARES.parse("\u2598 "), is(new byte[]{(byte) 16}));
        assertThat(SQUARES.parse("\u259D "), is(new byte[]{(byte) 32}));
        assertThat(SQUARES.parse("\u2596 "), is(new byte[]{(byte) 64}));
        assertThat(SQUARES.parse("\u259b\u2588"), is(new byte[]{(byte) 127}));
        assertThat(SQUARES.parse("  \u259b\u2588"), is(new byte[]{0, 127}));
        assertThat(SQUARES.parse("\u2597 "), is(new byte[]{(byte) 128}));
        assertThat(SQUARES.parse("\u2588\u2588"), is(new byte[]{(byte) 255}));
        assertThat(SQUARES.parse("\u2588\u259Fbogus\u2588"), is(new byte[]{(byte) 254}));
        assertThat(SQUARES.parse("bogus"), is(new byte[0]));
        assertThat(SQUARES.parse(""), is(new byte[0]));
        assertThat(SQUARES.parse(null), is(new byte[0]));
    }

    @Test
    public void squaresParseByteTest() {
        assertThat(SQUARES.parseByte(" "), is((byte) 0));
        assertThat(SQUARES.parseByte("\u2597 "), is((byte) 128));
        assertThat(SQUARES.parseByte(" \u2597"), is((byte) 8));
        assertThat(SQUARES.parseByte("\u259b\u2588"), is((byte) 127));
    }

    @Test
    public void sysOutTest() {
        byte[] input = Hex.decode("CAFE BABE 0001 0203 9933 FF");

        for (ByteVisualization visualization : ByteVisualization.values()) {
            System.out.println(visualization.visualize(input));
        }
    }
}