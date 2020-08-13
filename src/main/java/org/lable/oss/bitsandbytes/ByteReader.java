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

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * Static utility methods for reading string representations of byte sequences.
 *
 * @see #unescape(String)
 */
public class ByteReader {
    ByteReader() {
        // Static utility class.
    }

    /**
     * Convert a string consisting of characters and byte escape sequences representing bytes to the byte array it
     * represents. The byte array returned encodes characters as UTF-8.
     * <p>
     * Escape sequences look like {@code \xf4}.
     * <p>
     * This method reverses the results of {@link BytePrinter#allEscaped(byte[])},
     * {@link BytePrinter#nonBasicsEscaped(byte[])}, and {@link BytePrinter#utf8Escaped(byte[])}.
     *
     * @param input Input string with byte escape sequences.
     * @return A byte array.
     */
    public static byte[] unescape(String input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\\' && i + 3 < input.length() && input.charAt(i + 1) == 'x') {
                int high = asHexValue(input.charAt(i + 2));
                int low = asHexValue(input.charAt(i + 3));

                if (high > -1 && low > -1) {
                    // Only if both characters are within [0-9A-Fa-f] range is this a valid escape sequence.
                    // If not, just treat the characters as-is.
                    high <<= 4;
                    high |= low;
                    baos.write((byte) high & 0xFF);

                    i += 3;
                    continue;
                }
            }
            byte[] utf8 = Character.toString(c).getBytes(Charset.forName("UTF-8"));
            for (byte b : utf8) {
                baos.write(b);
            }
        }

        return baos.toByteArray();
    }

    static int asHexValue(char c) {
        if (c >= 'A' && c <= 'F') {
            return 10 + (c - 'A');
        } else if (c >= 'a' && c <= 'f') {
            return 10 + (c - 'a');
        } else if (c >= '0' && c <= '9') {
            return c - '0';
        } else {
            return -1;
        }
    }

}
