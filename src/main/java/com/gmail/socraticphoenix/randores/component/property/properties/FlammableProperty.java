/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
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
package com.gmail.socraticphoenix.randores.component.property.properties;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.jlsc.value.annotation.ConversionConstructor;
import com.gmail.socraticphoenix.jlsc.value.annotation.Convert;
import com.gmail.socraticphoenix.jlsc.value.annotation.Convertible;
import com.gmail.socraticphoenix.jlsc.value.annotation.Index;
import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.component.property.IntProperty;
import com.gmail.socraticphoenix.randores.component.property.MaterialProperty;
import com.gmail.socraticphoenix.randores.component.property.Properties;
import com.gmail.socraticphoenix.randores.component.property.PropertyGenerator;
import com.gmail.socraticphoenix.randores.plugin.RandoresPlugin;
import com.gmail.socraticphoenix.randores.probability.RandoresProbability;

import java.util.List;
import java.util.Random;

@Convertible
public class FlammableProperty implements IntProperty {
    @Convert(value = 0, reflect = false)
    private int burnTime;

    @ConversionConstructor
    public FlammableProperty(@Index(0) int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public String name() {
        return Properties.FLAMMABLE;
    }

    @Override
    public int value() {
        return this.burnTime;
    }

    public static class Generator implements PropertyGenerator {

        @Override
        public List<MaterialProperty> generate(Random random) {
            return Items.buildList(new FlammableProperty(RandoresProbability.clamp(random.nextInt(2000), 10, Integer.MAX_VALUE)));
        }

        @Override
        public boolean test(Random random) {
            return RandoresProbability.percentChance(15, random);
        }

        @Override
        public RandoresPlugin parent() {
            return Randores.INSTANCE;
        }

    }

}
