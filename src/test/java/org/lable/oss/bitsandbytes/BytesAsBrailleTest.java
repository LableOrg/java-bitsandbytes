package org.lable.oss.bitsandbytes;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.lable.oss.bitsandbytes.BytesAsBraille.visualize;

public class BytesAsBrailleTest {
    @Test
    public void testBrailleAsByte() {
        assertThat(visualize((byte) 0), is("\u2800"));
        assertThat(visualize((byte) 127), is("\u287F"));
        assertThat(visualize((byte) 128), is("\u2880"));
        assertThat(visualize((byte) 255), is("\u28FF"));
    }

    @Test
    public void testBrailleAsByteBytes() {
        assertThat(visualize(new byte[]{(byte) 0}), is("\u2800"));
        assertThat(visualize(new byte[]{(byte) 127}), is("\u287F"));
        assertThat(visualize(new byte[]{(byte) 128}), is("\u2880"));
        assertThat(visualize(new byte[]{(byte) 255}), is("\u28FF"));

        assertThat(visualize(new byte[]{0, 1, 2}), is("\u2800\u2801\u2802"));
    }

    @Test
    public void sysOutTest() {
        System.out.println(visualize("TEST here!".getBytes()));
    }
}