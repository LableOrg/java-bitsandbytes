package org.lable.oss.bitsandbytes;

/**
 * Use the braille Unicode character range (U+2800–U+28FF) to visualize bytes. The braille dots correspond to the
 * bits set.
 */
public class BytesAsBraille {

    BytesAsBraille() {
        // Static utility class.
    }

    /**
     * Visualize a single byte using braille Unicode characters.
     *
     * @param input A byte.
     * @return A printable string.
     */
    public static String visualize(byte input) {
        int offset = input < 0 ? 256 - Math.abs(input) : input;
        return Character.toString((char) ('\u2800' + offset));
    }

    /**
     * Visualize an array of bytes using braille Unicode characters.
     *
     * @param input Bytes.
     * @return A printable string.
     */
    public static String visualize(byte[] input) {
        if (input == null) return "";

        StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(visualize(b));
        }
        return builder.toString();
    }
}
