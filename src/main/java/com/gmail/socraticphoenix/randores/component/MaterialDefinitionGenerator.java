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
package com.gmail.socraticphoenix.randores.component;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.RandoresPluginRegistry;
import com.gmail.socraticphoenix.randores.component.ability.AbilityRegistry;
import com.gmail.socraticphoenix.randores.component.craftable.CraftableRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import com.gmail.socraticphoenix.randores.component.enumerable.OreTypeRegistry;
import com.gmail.socraticphoenix.randores.component.post.MaterialDefinitionEditorRegistry;
import com.gmail.socraticphoenix.randores.component.property.PropertyRegistry;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import com.gmail.socraticphoenix.randores.item.RandoresItems;
import com.gmail.socraticphoenix.randores.item.constructable.ConstructableAxe;
import com.gmail.socraticphoenix.randores.item.constructable.ConstructablePickaxe;
import com.gmail.socraticphoenix.randores.plugin.RandoresPlugin;
import com.gmail.socraticphoenix.randores.probability.RandomNumberBuilder;
import com.gmail.socraticphoenix.randores.probability.RandoresProbability;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MaterialDefinitionGenerator {

    public static void logStatistics(List<MaterialDefinition> definitions) {
        Map<OreType, Integer> dimCount = new LinkedHashMap<>();
        for (OreType oreType : OreTypeRegistry.values()) {
            dimCount.put(oreType, 0);
        }
        Map<MaterialType, Integer> mCount = new LinkedHashMap<>();
        for (MaterialType type : MaterialTypeRegistry.values()) {
            mCount.put(type, 0);
        }

        for (MaterialDefinition def : definitions) {
            OreType dim = def.getOre().getOreType();
            MaterialType mat = def.getMaterial().getType();
            dimCount.put(dim, dimCount.get(dim) + 1);
            mCount.put(mat, mCount.get(mat) + 1);
        }
        Randores.info("Definition Count: " + definitions.size());
        Randores.info("Ores per OreType: ");
        for (Map.Entry<OreType, Integer> entry : dimCount.entrySet()) {
            Randores.info("    " + entry.getKey().getName() + ": " + entry.getValue() + " ore(s)");
        }
        Randores.info("Material Types: ");
        for (Map.Entry<MaterialType, Integer> entry : mCount.entrySet()) {
            Randores.info("    " + entry.getKey().getName() + ": " + entry.getValue() + " ore(s)");
        }
    }

    public static Set<Color> generateColors(Random random, int count) {
        Set<Color> set = new LinkedHashSet<>();
        while (set.size() < count) {
            set.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
        return set;
    }

    public static void removeBackers(List<MaterialDefinition> definitions) {
        for (MaterialDefinition definition : definitions) {
            RandoresItemData itemData = definition.getData();

            RandoresItems.hoe.removeBacker(itemData);
            RandoresItems.sword.removeBacker(itemData);
            RandoresItems.axe.removeBacker(itemData);
            RandoresItems.shovel.removeBacker(itemData);
            RandoresItems.pickaxe.removeBacker(itemData);
            RandoresItems.battleaxe.removeBacker(itemData);
            RandoresItems.helmet.removeBacker(itemData);
            RandoresItems.chestplate.removeBacker(itemData);
            RandoresItems.leggings.removeBacker(itemData);
            RandoresItems.boots.removeBacker(itemData);
        }
    }

    public static void applyBackers(List<MaterialDefinition> definitions) {
        for (MaterialDefinition definition : definitions) {
            ToolMaterial toolMaterial = definition.getToolMaterial();
            ArmorMaterial armorMaterial = definition.getArmorMaterial();

            RandoresItemData itemData = definition.getData();

            RandoresItems.hoe.registerBacker(itemData, new ItemHoe(toolMaterial));
            RandoresItems.sword.registerBacker(itemData, new ItemSword(toolMaterial));
            RandoresItems.axe.registerBacker(itemData, new ConstructableAxe(toolMaterial, toolMaterial.getDamageVsEntity() + 5f, -3f));
            RandoresItems.shovel.registerBacker(itemData, new ItemSpade(toolMaterial));
            RandoresItems.pickaxe.registerBacker(itemData, new ConstructablePickaxe(toolMaterial));
            RandoresItems.battleaxe.registerBacker(itemData, new ConstructableAxe(toolMaterial, toolMaterial.getDamageVsEntity() + 8f, -3.5f));
            RandoresItems.helmet.registerBacker(itemData, armorMaterial);
            RandoresItems.chestplate.registerBacker(itemData, armorMaterial);
            RandoresItems.leggings.registerBacker(itemData, armorMaterial);
            RandoresItems.boots.registerBacker(itemData, armorMaterial);
        }
    }

    public static List<MaterialDefinition> makeDefinitions(Set<Color> colors, RandoresWorldData data) {
        for(RandoresPlugin plugin : RandoresPluginRegistry.getPlugins()) {
            plugin.getRandomContainer().setRandom(new Random(data.getPluginSeeds().getOrDefault(plugin.id(), 0L)));
        }

        List<MaterialDefinition> definitions = new ArrayList<>();
        int index = 0;
        for (Color color : colors) {
            Random random = new Random(color.getRGB());
            RandomNumberBuilder b = new RandomNumberBuilder(random);
            b
                    .oneSidedInflectedNormalRand("uses", 20,    10_000, 2000)
                    .op(input -> input.intValue() - input.intValue() % 10)
                    .inflectedNormalRand("commonality", 10, 70, 40, 25)
                    .copy("commonality", "commonalityInt")
                    .op(Number::intValue)
                    .copy("commonality", "rarity")
                    .op(input -> 80 - input.doubleValue())
                    .copy("commonalityInt", "rarityInt")
                    .op(input -> 80 - input.intValue())
                    .oneSidedInflectedNormalRand("efficiencyVar", 0, 10, 2)
                    .oneSidedInflectedNormalRand("damageVar", 0, 10, 3)
                    .oneSidedInflectedNormalRand("enchantVar", 0, 20, 5)
                    .rand("toughness", 3)
                    .oneSidedInflectedNormalRand("maxYVar", 20, 100, 20)
                    .expRand("minYVar", 2, 0, b.getDouble("commonality"))
                    .op(input -> Math.floor(input.doubleValue()))
                    .put("maxOccurrences", b.getInt("commonalityInt") / 20 + 1)
                    .rand("occurrencesVar", b.getInt("maxOccurrences"))
                    .oneSidedInflectedNormalRand("maxDrops", 1, 6, 3)
                    .op(input -> Math.floor(input.doubleValue()))
                    .oneSidedInflectedNormalRand("minDrops", 0, b.getInt("maxDrops"), 1)
                    .clamp(1, Integer.MAX_VALUE)
                    .inflectedNormalRand("smeltingXp", 0, 1, 0.2, 0.25)
                    .rand("oreHarvest", 3)
                    .op(i -> i.intValue() + 1)
                    .randLong("selectorSeed")
            ;

            Random selector = new Random(b.getLong("selectorSeed"));

            List<MaterialType> candidates = MaterialTypeRegistry.buildTypes();
            MaterialType type = RandoresProbability.randomElement(candidates, selector);

            List<OreType> oreCandidates = OreTypeRegistry.buildOreTypes();
            OreType oreType = RandoresProbability.randomElement(oreCandidates, selector);


            int commonalityInt = b.getInt("commonalityInt");
            double rarity = b.getDouble("rarity");
            int rarityInt = b.getInt("rarityInt");

            int maxY = RandoresProbability.clamp((int) (commonalityInt + b.getDouble("maxYVar")), 10, 90);
            int maxOccurrences = b.getInt("maxOccurrences");

            MaterialComponent material = new MaterialComponent(type,
                    new int[]{rarityInt / 20, rarityInt / 8, rarityInt / 6, rarityInt / 20},
                    RandoresProbability.clamp(rarityInt / 22 + 1, Item.ToolMaterial.WOOD.getHarvestLevel(), Item.ToolMaterial.DIAMOND.getHarvestLevel()),
                    b.getInt("uses"),
                    (int) (rarity / 6 + b.getDouble("efficiencyVar")),
                    (int) (rarity / 13 + b.getDouble("damageVar")),
                    b.getInt("toughness"),
                    (int) (rarity / 3 + b.getDouble("enchantVar")));

            OreComponent ore = new OreComponent(material,
                    oreType,
                    b.getInt("maxDrops"),
                    b.getInt("minDrops"),
                    commonalityInt / 8 + 4,
                    commonalityInt / 9 + 2,
                    maxY,
                    RandoresProbability.clamp(commonalityInt - (commonalityInt - b.getInt("minYVar")) + 1, 0, maxY - 5),
                    maxOccurrences - b.getInt("occurrencesVar"),
                    maxOccurrences,
                    type.requiresSmelting(),
                    (float) b.getDouble("smeltingXp"),
                    (float) Math.round(rarity / 14),
                    (float) Math.round(rarity / 7),
                    b.getInt("oreHarvest"));

            MaterialDefinition definition = new MaterialDefinition(color, RandoresNameAlgorithm.name(color), ore,
                    CraftableRegistry.buildCraftables(),
                    PropertyRegistry.buildProperties(),
                    AbilityRegistry.buildSeries(selector));
            definition.provideData(data.getId(), index);

            MaterialDefinitionEditorRegistry.edit(definition);

            definitions.add(definition);
            index++;
        }
        return definitions;
    }

}
