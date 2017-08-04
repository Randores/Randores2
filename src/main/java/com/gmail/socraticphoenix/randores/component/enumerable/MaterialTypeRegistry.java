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
    private static final MaterialTypeRegistry instance = new MaterialTypeRegistry();

    private Map<String, MaterialType> materialTypes = new HashMap<>();
    private List<MaterialType> materialsAsList = new ArrayList<>();

    public static MaterialTypeRegistry instance() {
        return instance;
    }

    public void register(MaterialType... MaterialTypes) {
        for(MaterialType MaterialType : MaterialTypes) {
            register(MaterialType);
        }
    }

    public void register(MaterialType MaterialType) {
        if(materialTypes.containsKey(MaterialType.getName())) {
            throw new IllegalArgumentException("Attempted to register duplicate MaterialType: " + MaterialType.getName());
        }

        materialTypes.put(MaterialType.getName(), MaterialType);
        materialsAsList.add(MaterialType);
    }

    public MaterialType get(String MaterialType) {
        return materialTypes.get(MaterialType);
    }

    public Map<String, MaterialType> getMaterialTypes() {
        return materialTypes;
    }

    public List<MaterialType> values() {
        return materialsAsList;
    }

    private List<MaterialTypeGenerator> generators = new ArrayList<>();

    public void register(MaterialTypeGenerator generator) {
        generators.add(generator);
    }

    public void register(MaterialTypeGenerator... generators) {
        for(MaterialTypeGenerator generator : generators) {
            register(generator);
        }
    }

    public List<MaterialType> buildTypes() {
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
