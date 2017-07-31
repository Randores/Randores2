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
package com.gmail.socraticphoenix.randores.mod.component.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AbilityRegistry {
    private static Map<AbilityType, Map<AbilityStage, List<Ability>>> abilities = new HashMap<>();

    public static void register(Ability ability) {
        for (AbilityType type : AbilityType.values()) {
            for (AbilityStage stage : AbilityStage.values()) {
                if (ability.applicableStage(stage) && ability.applicableContext(type)) {
                    for (int i = 0; i < ability.weight(); i++) {
                        AbilityRegistry.put(type, stage, ability);
                    }
                }
            }
        }
    }

    public static void put(AbilityType type, AbilityStage stage, Ability ability) {
        if (!abilities.containsKey(type)) {
            abilities.put(type, new HashMap<AbilityStage, List<Ability>>());
        }

        Map<AbilityStage, List<Ability>> sub = abilities.get(type);

        if (!sub.containsKey(stage)) {
            sub.put(stage, new ArrayList<Ability>());
        }

        sub.get(stage).add(ability);
    }

    public static int size(AbilityType type, AbilityStage stage) {
        return abilities.containsKey(type) ? abilities.get(type).containsKey(stage) ? abilities.get(type).get(stage).size() : 0 : 0;
    }

    public static Ability get(AbilityType type, AbilityStage stage, int index) {
        return abilities.containsKey(type) ? abilities.get(type).containsKey(stage) ? abilities.get(type).get(stage).get(index) : null : null;

    }

    public static AbilitySeries buildSeries(Random random) {
        Set<Ability> armorPassive = new LinkedHashSet<>();
        Set<Ability> armorUpdate = new LinkedHashSet<>();
        Set<Ability> melee = new LinkedHashSet<>();
        Set<Ability> projectile = new LinkedHashSet<>();

        if (random.nextBoolean() && contains(AbilityType.ARMOR_PASSIVE, AbilityStage.FIRST))
            armorPassive.add(randomSelect(AbilityType.ARMOR_PASSIVE, AbilityStage.FIRST, random));
        if (random.nextBoolean() && contains(AbilityType.ARMOR_ACTIVE, AbilityStage.FIRST))
            armorUpdate.add(randomSelect(AbilityType.ARMOR_ACTIVE, AbilityStage.FIRST, random));
        if (random.nextBoolean() && contains(AbilityType.MELEE, AbilityStage.FIRST))
            melee.add(randomSelect(AbilityType.MELEE, AbilityStage.FIRST, random));
        if (random.nextBoolean() && contains(AbilityType.PROJECTILE, AbilityStage.FIRST))
            projectile.add(randomSelect(AbilityType.PROJECTILE, AbilityStage.FIRST, random));

        armorPassive.addAll(randomSelect(AbilityType.ARMOR_PASSIVE, AbilityStage.MIDDLE, random, random.nextInt(3) + 1));
        armorUpdate.addAll(randomSelect(AbilityType.ARMOR_ACTIVE, AbilityStage.MIDDLE, random, random.nextInt(3) + 1));
        melee.addAll(randomSelect(AbilityType.MELEE, AbilityStage.MIDDLE, random, random.nextInt(3) + 1));
        projectile.addAll(randomSelect(AbilityType.PROJECTILE, AbilityStage.MIDDLE, random, random.nextInt(3) + 1));

        if (random.nextBoolean() && contains(AbilityType.ARMOR_PASSIVE, AbilityStage.LAST))
            armorPassive.add(randomSelect(AbilityType.ARMOR_PASSIVE, AbilityStage.LAST, random));
        if (random.nextBoolean() && contains(AbilityType.ARMOR_ACTIVE, AbilityStage.LAST))
            armorUpdate.add(randomSelect(AbilityType.ARMOR_ACTIVE, AbilityStage.LAST, random));
        if (random.nextBoolean() && contains(AbilityType.MELEE, AbilityStage.LAST))
            melee.add(randomSelect(AbilityType.MELEE, AbilityStage.LAST, random));
        if (random.nextBoolean() && contains(AbilityType.PROJECTILE, AbilityStage.LAST))
            projectile.add(randomSelect(AbilityType.PROJECTILE, AbilityStage.LAST, random));

        return new AbilitySeries(list(armorPassive), list(armorUpdate), list(melee), list(projectile));
    }

    public static <T> List<T> list(Set<T> set) {
        List<T> list = new ArrayList<>();
        list.addAll(set);
        return list;
    }

    public static Ability randomSelect(AbilityType type, AbilityStage stage, Random random) {
        int size = AbilityRegistry.size(type, stage);
        int index = random.nextInt(size);
        return AbilityRegistry.get(type, stage, index);
    }

    public static List<Ability> randomSelect(AbilityType type, AbilityStage stage, Random random, int size) {
        List<Ability> abilities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int s = AbilityRegistry.size(type, stage);
            if (s != 0) {
                abilities.add(randomSelect(type, stage, random));
            } else {
                break;
            }
        }
        return abilities;
    }

    public static boolean contains(AbilityType type, AbilityStage stage) {
        return size(type, stage) != 0;
    }

}
