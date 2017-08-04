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

public class MaterialTypeRegistry {
    private static Map<String, MaterialType> materialTypes = new HashMap<>();
    private static List<MaterialType> materialsAsList = new ArrayList<>();

    public static void register(MaterialType... MaterialTypes) {
        for(MaterialType MaterialType : MaterialTypes) {
            register(MaterialType);
        }
    }

    public static void register(MaterialType MaterialType) {
        if(materialTypes.containsKey(MaterialType.getName())) {
            throw new IllegalArgumentException("Attempted to register duplicate MaterialType: " + MaterialType.getName());
        }

        materialTypes.put(MaterialType.getName(), MaterialType);
        materialsAsList.add(MaterialType);
    }

    public static MaterialType get(String MaterialType) {
        return materialTypes.get(MaterialType);
    }

    public static Map<String, MaterialType> getMaterialTypes() {
        return materialTypes;
    }

    public static List<MaterialType> values() {
        return materialsAsList;
    }

    private static List<MaterialTypeGenerator> generators = new ArrayList<>();

    public static void register(MaterialTypeGenerator generator) {
        generators.add(generator);
    }

    public static void register(MaterialTypeGenerator... generators) {
        for(MaterialTypeGenerator generator : generators) {
            register(generator);
        }
    }

    public static List<MaterialType> buildTypes() {
        List<MaterialType> types = new ArrayList<>();
        for(MaterialTypeGenerator generator : generators) {
            Random random = generator.parent().getRandomContainer().getRandom();
            if(generator.test(random)) {
                types.addAll(generator.generate(random));
            }
        }
        return types;
    }

}
