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
package com.gmail.socraticphoenix.randores.component.ability;

import com.gmail.socraticphoenix.randores.probability.IntRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AbilityRegistry {
    private static Map<AbilityType, Map<AbilityStage, List<AbilityGenerator>>> abilities = new HashMap<>();

    public static void register(AbilityGenerator... factories) {
        for (AbilityGenerator factory : factories) {
            register(factory);
        }
    }

    public static void register(AbilityGenerator factory) {
        for (AbilityType type : AbilityType.values()) {
            for (AbilityStage stage : AbilityStage.values()) {
                if (factory.applicableStage(stage) && factory.applicableContext(type)) {
                    AbilityRegistry.put(type, stage, factory);
                }
            }
        }
    }

    public static void put(AbilityType type, AbilityStage stage, AbilityGenerator factory) {
        abilities.computeIfAbsent(type, k -> new HashMap<>()).computeIfAbsent(stage, k -> new ArrayList<>()).add(factory);
    }

    public static int size(AbilityType type, AbilityStage stage) {
        return abilities.containsKey(type) ? abilities.get(type).containsKey(stage) ? abilities.get(type).get(stage).size() : 0 : 0;
    }

    public static AbilityGenerator get(AbilityType type, AbilityStage stage, int index) {
        return abilities.containsKey(type) ? abilities.get(type).containsKey(stage) ? abilities.get(type).get(stage).get(index) : null : null;

    }

    public static boolean contains(AbilityType type, AbilityStage stage) {
        return size(type, stage) > 0;
    }

    private static IntRange count = new IntRange(1, 4);

    public static AbilitySeries buildSeries(Random countRand) {
        List<Ability>[] series = new List[]{new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()};
        AbilityType[] types = {AbilityType.ARMOR_PASSIVE, AbilityType.ARMOR_ACTIVE, AbilityType.MELEE, AbilityType.PROJECTILE};
        for (int i = 0; i < types.length; i++) {
            List<Ability> list = series[i];
            AbilityType type = types[i];
            addAbilities(list, type, AbilityStage.FIRST);
            int count = AbilityRegistry.count.randomElement(countRand);
            for (int j = 0; j < count; j++) {
                addAbilities(list, type, AbilityStage.MIDDLE);
            }
            addAbilities(list, type, AbilityStage.LAST);
        }
        return new AbilitySeries(series[0], series[1], series[2], series[3]);
    }

    private static void addAbilities(List<Ability> list, AbilityType type, AbilityStage stage) {
        if(contains(type, stage)) {
            List<AbilityGenerator> generators = abilities.get(type).get(stage);
            for(AbilityGenerator generator : generators) {
                Random random = generator.parent().getRandomContainer().getRandom();
                if(generator.test(random)) {
                    list.addAll(generator.generate(random, stage, type));
                }
            }
        }
    }


}
