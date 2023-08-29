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

/**
 * Common byte and byte array values.
 */
public class CommonByteValues {
    /**
     * A byte with all bits set to 0 (or b00000000).
     */
    public static byte B_NULL = 0;

    /**
     * A byte with all bits set to 1 (or b11111111).
     */
    public static byte B_FULL = -1;

    /**
     * A byte array containing a single byte with all bits set to 0 (or b00000000).
     */
    public static byte[] NULL = new byte[]{B_NULL};

    /**
     * A byte array containing a single byte with all bits set to 1 (or b11111111).
     */
    public static byte[] FULL = new byte[]{B_FULL};
}
