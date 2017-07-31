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

import com.gmail.socraticphoenix.jlsc.JLSCArray;
import com.gmail.socraticphoenix.jlsc.JLSCCompound;
import com.gmail.socraticphoenix.jlsc.JLSCConfiguration;
import com.gmail.socraticphoenix.jlsc.JLSCException;
import com.gmail.socraticphoenix.jlsc.JLSCFormat;
import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.game.item.RandoresItems;
import com.gmail.socraticphoenix.randores.game.item.constructable.ConstructableAxe;
import com.gmail.socraticphoenix.randores.game.item.constructable.ConstructablePickaxe;
import com.gmail.socraticphoenix.randores.mod.component.ability.AbilityRegistry;
import com.gmail.socraticphoenix.randores.mod.component.property.MaterialProperty;
import com.gmail.socraticphoenix.randores.mod.component.property.properties.FlammableProperty;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.util.probability.RandomNumberBuilder;
import com.gmail.socraticphoenix.randores.util.probability.RandoresProbability;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MaterialDefinitionGenerator {

    public static void logStatistics(List<MaterialDefinition> definitions) {
        Map<Dimension, Integer> dimCount = new LinkedHashMap<>();
        for (Dimension dimension : Dimension.values()) {
            dimCount.put(dimension, 0);
        }
        Map<MaterialType, Integer> mCount = new LinkedHashMap<>();
        for (MaterialType type : MaterialType.values()) {
            mCount.put(type, 0);
        }

        for (MaterialDefinition def : definitions) {
            Dimension dim = def.getOre().getDimension();
            MaterialType mat = def.getMaterial().getType();
            dimCount.put(dim, dimCount.get(dim) + 1);
            mCount.put(mat, mCount.get(mat) + 1);
        }
        Randores.info("Definition Count: " + definitions.size());
        Randores.info("Ores per Dimension: ");
        for (Map.Entry<Dimension, Integer> entry : dimCount.entrySet()) {
            Randores.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
        }
        Randores.info("Material Types: ");
        for (Map.Entry<MaterialType, Integer> entry : mCount.entrySet()) {
            Randores.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
        }
    }

    public static Set<Color> generateColors(Random random, int count) {
        Set<Color> set = new LinkedHashSet<>();
        while (set.size() < count) {
            set.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
        return set;
    }

    public static void registerDefinitionsIfNeeded(long seed, int count, World world) {
        if (!MaterialDefinitionRegistry.contains(seed)) {
            List<MaterialDefinition> definitions = makeDefinitions(generateColors(new Random(seed), count), seed);
            if(Randores.getConfigObj().isConvert() && world != null && !world.isRemote) {
                Randores.info("Beginning conversion to custom definitions...");
                JLSCArray array = new JLSCArray();
                definitions.forEach(array::add);
                JLSCConfiguration conf = new JLSCConfiguration(new JLSCCompound().toConcurrent(), new File(world.getSaveHandler().getWorldDirectory(), "randores_custom.jlsc"), JLSCFormat.TEXT, true);
                conf.put("definitions", array);
                try {
                    conf.save();
                    Randores.info("Finished converting to custom definitions.");
                } catch (IOException | JLSCException e) {
                    Randores.getLogger().error("FATAL ERROR: Failed to convert to custom definitions", e);
                }
            }
            definitions.forEach(MaterialDefinitionRegistry::register);
            applyBackers(definitions);
        }
    }

    public static void removeDefinitions(long seed) {
        if (MaterialDefinitionRegistry.contains(seed)) {
            List<MaterialDefinition> definitions = MaterialDefinitionRegistry.getAll(seed);
            removeBackers(definitions);
            MaterialDefinitionRegistry.removeAll(seed);
        }
    }

    public static void removeAllExcept(long seed) {
        List<MaterialDefinition> other = MaterialDefinitionRegistry.getAllExcept(seed);
        removeBackers(other);
        MaterialDefinitionRegistry.removeAllExcept(seed);
    }

    public static void removeBackers(List<MaterialDefinition> definitions) {
        for (MaterialDefinition definition : definitions) {
            long seed = definition.getSeed();
            RandoresItemData itemData = new RandoresItemData(definition.getIndex(), seed);

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
            long seed = definition.getSeed();

            ToolMaterial toolMaterial = definition.getToolMaterial();
            ArmorMaterial armorMaterial = definition.getArmorMaterial();

            RandoresItemData itemData = new RandoresItemData(definition.getIndex(), seed);

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

    public static List<MaterialDefinition> makeDefinitions(Set<Color> colors, long seed) {
        List<MaterialDefinition> definitions = new ArrayList<>();
        int c = 0;
        for (Color color : colors) {
            Random random = new Random(color.getRGB());
            RandomNumberBuilder b = new RandomNumberBuilder(random);
            b.expRand("material", 2.5, 0, MaterialType.values().length)
                    .rand("dimension", Dimension.values().length)
                    .oneSidedInflectedNormalRand("uses", 20, 6000, 2000)
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
                    .clamp(1, b.getInt("maxDrops"))
                    .put("maxVein", (int) (b.getDouble("commonality") / 8) + 2)
                    .rand("minVein", b.getInt("maxVein"))
                    .clamp(1, b.getInt("maxVein") - 1)
                    .inflectedNormalRand("smeltingXp", 0, 1.25, 0.75, 0.25)
                    .percentChance("stick", 30)
                    .percentChance("armor", 60)
                    .percentChance("tools", 60)
                    .percentChance("sword", 60)
                    .percentChance("bow", 30)
                    .percentChance("flammable", 15)
                    .rand("flammability", 1200)
                    .clamp(200, 1200)
                    .percentChance("battleaxe", 30)
                    .percentChance("sledgehammer", 30)
                    .randLong("abilitySeed")
                    .rand("oreHarvest", 3)
                    .op(i -> i.intValue() + 1)
            ;

            MaterialType type = MaterialType.values()[b.getInt("material")];

            int commonalityInt = b.getInt("commonalityInt");
            double rarity = b.getDouble("rarity");
            int rarityInt = b.getInt("rarityInt");

            int maxY = RandoresProbability.clamp((int) (commonalityInt + b.getDouble("maxYVar")), 10, 65);
            int maxOccurrences = b.getInt("maxOccurrences");

            MaterialComponent material = new MaterialComponent(type,
                    MaterialDefinitionGenerator.clampArmor(new int[]{rarityInt / 20, rarityInt / 8, rarityInt / 6, rarityInt / 20}),
                    RandoresProbability.clamp(rarityInt / 22 + 1, Item.ToolMaterial.WOOD.getHarvestLevel(), Item.ToolMaterial.DIAMOND.getHarvestLevel()),
                    b.getInt("uses"),
                    (int) (rarity / 6 + b.getDouble("efficiencyVar")),
                    (int) (rarity / 13 + b.getDouble("damageVar")),
                    b.getInt("toughness"),
                    (int) (rarity / 3 + b.getDouble("enchantVar")));

            Dimension dimension = Dimension.values()[b.getInt("dimension")];
            OreComponent ore = new OreComponent(material,
                    dimension,
                    b.getInt("maxDrops"),
                    b.getInt("minDrops"),
                    b.getInt("maxVein"),
                    b.getInt("minVein"),
                    maxY,
                    RandoresProbability.clamp(commonalityInt - (commonalityInt - b.getInt("minYVar")) + 1, 1, maxY - 5),
                    maxOccurrences - b.getInt("occurrencesVar"),
                    maxOccurrences,
                    type == MaterialType.INGOT,
                    (float) b.getInt("smeltingXp"),
                    (float) Math.round(rarity / 14),
                    (float) Math.round(rarity / 7),
                    b.getInt("oreHarvest"));

            List<CraftableComponent> components = new ArrayList<CraftableComponent>();
            components.add(new CraftableComponent(CraftableType.BRICKS, 4));
            components.add(new CraftableComponent(CraftableType.TORCH, 4));

            if (b.getBoolean("stick")) {
                components.add(new CraftableComponent(CraftableType.STICK, 2));
            }
            if (b.getBoolean("armor")) {
                components.add(new CraftableComponent(CraftableType.HELMET, 1));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, 1));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, 1));
                components.add(new CraftableComponent(CraftableType.BOOTS, 1));
            }
            if (b.getBoolean("tools")) {
                components.add(new CraftableComponent(CraftableType.PICKAXE, 1));
                components.add(new CraftableComponent(CraftableType.AXE, 1));
                components.add(new CraftableComponent(CraftableType.HOE, 1));
                components.add(new CraftableComponent(CraftableType.SHOVEL, 1));
            }
            if (b.getBoolean("bow")) {
                components.add(new CraftableComponent(CraftableType.BOW, 1));
            }
            if (b.getBoolean("sword")) {
                components.add(new CraftableComponent(CraftableType.SWORD, 1));
            }
            if (b.getBoolean("battleaxe")) {
                components.add(new CraftableComponent(CraftableType.BATTLEAXE, 1));
            }
            if (b.getBoolean("sledgehammer")) {
                components.add(new CraftableComponent(CraftableType.SLEDGEHAMMER, 1));
            }

            List<MaterialProperty> properties = new ArrayList<>();
            if (b.getBoolean("flammable")) {
                properties.add(new FlammableProperty(b.getInt("flammability")));
            }


            MaterialDefinition definition = new MaterialDefinition(color, RandoresNameAlgorithm.name(color), ore, components, properties, AbilityRegistry.buildSeries(new Random(b.getLong("abilitySeed"))));
            definition.provideData(seed, c);
            definitions.add(definition);
            c++;
        }
        return definitions;
    }

    private static int[] clampArmor(int[] reduc) {
        for (int i = 0; i < reduc.length; i++) {
            if (reduc[i] < 1) {
                reduc[i] = 1;
            }
        }

        while (sum(reduc) > 20) {
            reduce(reduc, 0);
            reduce(reduc, 1);
            reduce(reduc, 1);
            reduce(reduc, 2);
            reduce(reduc, 2);
            reduce(reduc, 3);
        }

        return reduc;
    }

    private static void reduce(int[] arr, int slot) {
        if (arr[slot] > 2) {
            arr[slot] = arr[slot] - 1;
        }
    }

    private static int sum(int[] arr) {
        int a = 0;
        for (int i : arr) {
            a += i;
        }
        return a;
    }

}
