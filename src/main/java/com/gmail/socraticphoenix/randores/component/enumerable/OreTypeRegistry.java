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
package com.gmail.socraticphoenix.randores.component.enumerable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OreTypeRegistry {
    public static final String OVERWORLD = "overworld";
    public static final String END = "end";
    public static final String NETHER = "nether";

    private static Map<String, OreType> oreTypes = new HashMap<>();
    private static List<OreType> oreTypesAsList = new ArrayList<>();

    public static void register(OreType... oreTypes) {
        for(OreType oreType : oreTypes) {
            register(oreType);
        }
    }

    public static void register(OreType oreType) {
        if(oreTypes.containsKey(oreType.getName())) {
            throw new IllegalArgumentException("Attempted to register duplicate oreType: " + oreType.getName());
        }

        oreTypes.put(oreType.getName(), oreType);
        oreTypesAsList.add(oreType);
    }

    public static OreType get(String oreType) {
        return oreTypes.get(oreType);
    }

    public static Map<String, OreType> getoreTypes() {
        return oreTypes;
    }

    public static List<OreType> values() {
        return oreTypesAsList;
    }

    private static List<OreTypeGenerator> generators = new ArrayList<>();

    public static void register(OreTypeGenerator generator) {
        generators.add(generator);
    }

    public static void register(OreTypeGenerator... generators) {
        for(OreTypeGenerator generator : generators) {
            register(generator);
        }
    }

    public static List<OreType> buildOreTypes() {
        List<OreType> oreTypes = new ArrayList<>();
        for(OreTypeGenerator generator : generators) {
            Random random = generator.parent().getRandomContainer().getRandom();
            if(generator.test(random)) {
                oreTypes.addAll(generator.generate(random));
            }
        }
        return oreTypes;
    }

}
