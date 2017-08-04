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
package com.gmail.socraticphoenix.randores.component.property;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PropertyRegistry {
    private static final PropertyRegistry instance = new PropertyRegistry();

    private List<PropertyGenerator> registry = new ArrayList<>();

    public static PropertyRegistry instance() {
        return instance;
    }

    public void register(PropertyGenerator... factories) {
        for(PropertyGenerator factory : factories) {
            register(factory);
        }
    }

    public void register(PropertyGenerator factory) {
        registry.add(factory);
    }

    public List<MaterialProperty> buildProperties() {
        List<MaterialProperty> properties = new ArrayList<>();
        for(PropertyGenerator generator : registry) {
            Random random = generator.parent().getRandomContainer().getRandom();
            if(generator.test(random)) {
                properties.addAll(generator.generate(random));
            }
        }
        return properties;
    }

}
