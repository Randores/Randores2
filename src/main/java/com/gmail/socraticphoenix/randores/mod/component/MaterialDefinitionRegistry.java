/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 socraticphoenix@gmail.com
 * Copyright (c) 2017 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.randores.mod.component;

import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.mod.data.RandoresSeed;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MaterialDefinitionRegistry {
    private static Map<RandoresItemData, MaterialDefinition> definitions = new LinkedHashMap<>();

    public static MaterialDefinition get(int index, long seed) {
        return definitions.get(new RandoresItemData(index, seed));
    }

    public static void delegateVoid(RandoresItemData data, Consumer<MaterialDefinition> consumer, Runnable def) {
        if(contains(data)) {
            consumer.accept(get(data));
        } else {
            def.run();
        }
    }

    public static <T> T delegate(RandoresItemData data, Function<MaterialDefinition, T> function, Supplier<T> def) {
        if(contains(data)) {
            return function.apply(get(data));
        } else {
            return def.get();
        }
    }

    public static MaterialDefinition get(RandoresItemData data) {
        return get(data.getIndex(), data.getSeed());
    }

    public static MaterialDefinition get(int index, World world) {
        return get(index, RandoresSeed.getSeed(world));
    }

    public static boolean contains(int index, long seed) {
        return definitions.containsKey(new RandoresItemData(index, seed));
    }

    public static boolean contains(RandoresItemData data) {
        return contains(data.getIndex(), data.getSeed());
    }

    public static boolean contains(int index, World world) {
        return contains(index, RandoresSeed.getSeed(world));
    }

    public static boolean contains(long seed) {
        return definitions.keySet().stream().anyMatch(i -> i.getSeed() == seed);
    }

    public static void register(MaterialDefinition definition) {
        RandoresItemData data = new RandoresItemData(definition.getIndex(), definition.getSeed());
        definitions.put(data, definition);
    }

    public static void remove(RandoresItemData data) {
        definitions.remove(data);
    }

    public static void removeAll(long seed) {
        definitions.entrySet().removeIf(e -> e.getKey().getSeed() == seed);
    }

    public static void removeAllExcept(long seed) {
        definitions.entrySet().removeIf(e -> e.getKey().getSeed() != seed);
    }

    public static List<MaterialDefinition> getAll(long seed) {
        return definitions.entrySet().stream().filter(e -> e.getKey().getSeed() == seed).map(Entry::getValue).collect(Collectors.toList());
    }

    public static List<MaterialDefinition> getAllExcept(long seed) {
        return definitions.entrySet().stream().filter(e -> e.getKey().getSeed() != seed).map(Entry::getValue).collect(Collectors.toList());
    }

}
