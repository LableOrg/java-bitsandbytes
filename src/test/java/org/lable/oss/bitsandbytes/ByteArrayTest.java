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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ByteArrayTest {
    @Test
    public void setTest() {
        Set<ByteArray> set = new HashSet<>();
        set.add(new ByteArray(ByteConversion.fromString("test")));
        set.add(new ByteArray(ByteConversion.fromString("test")));

        assertThat(set.size(), is(1));
    }

    @Test
    public void mapKeyTest() {
        Map<ByteArray, String> map = new HashMap<>();
        map.put(new ByteArray(ByteConversion.fromString("test")), "test");
        map.put(new ByteArray(ByteConversion.fromString("toast")), "toast");

        assertThat(map.size(), is(2));
        assertThat(map.get(new ByteArray(ByteConversion.fromString("test"))), is("test"));
        assertThat(map.get(new ByteArray(ByteConversion.fromString("toast"))), is("toast"));
    }

    @Test
    public void getTest() {
        ByteArray ba = new ByteArray(ByteConversion.fromString("test"));
        
        assertThat(ba.get(), is("test".getBytes()));
    }

    @Test
    public void hashTest() {
        ByteArray ba = new ByteArray(ByteConversion.fromString("test"));

        assertThat(ba.hash, is(nullValue()));

        int hash = ba.hashCode();

        assertThat(ba.hash, is(hash));
    }

    @Test
    public void emptyTest() {
        ByteArray ba = ByteArray.empty();

        assertThat(ba.get().length, is(0));
    }
}