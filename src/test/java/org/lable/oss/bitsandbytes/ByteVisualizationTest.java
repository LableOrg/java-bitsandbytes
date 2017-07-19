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
import static org.lable.oss.bitsandbytes.ByteVisualization.BRAILLE;
import static org.lable.oss.bitsandbytes.ByteVisualization.ONES_AND_ZEROES;
import static org.lable.oss.bitsandbytes.ByteVisualization.SQUARES;

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
    public void sysOutTest() {
        System.out.println(BRAILLE.visualize("TEST here!".getBytes()));
        System.out.println(ONES_AND_ZEROES.visualize("TEST here!".getBytes()));
        System.out.println(SQUARES.visualize("TEST here!".getBytes()));
    }
}