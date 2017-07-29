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

import com.gmail.socraticphoenix.jlsc.serialization.annotation.Name;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serialize;
import com.gmail.socraticphoenix.randores.game.block.RandoresBlocks;
import com.gmail.socraticphoenix.randores.game.item.RandoresItemBlock;
import com.gmail.socraticphoenix.randores.translations.Keys;
import net.minecraft.block.Block;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

@Serializable
public class OreComponent implements Component {
    private RandoresItemBlock item;

    @Serialize(value = "material", reflect = false)
    private MaterialComponent material;
    @Serialize(value = "dimension", reflect = false)
    private Dimension dimension;

    @Serialize(value = "minDrops", reflect = false)
    private int minDrops;
    @Serialize(value = "maxDrops", reflect = false)
    private int maxDrops;

    @Serialize(value = "minVein", reflect = false)
    private int minVein;
    @Serialize(value = "maxVein", reflect = false)
    private int maxVein;

    @Serialize(value = "minY", reflect = false)
    private int minY;
    @Serialize(value = "maxY", reflect = false)
    private int maxY;

    @Serialize(value = "minOccurrences", reflect = false)
    private int minOccurrences;
    @Serialize(value = "maxOccurrences", reflect = false)
    private int maxOccurrences;

    @Serialize(value = "smeltingXp", reflect = false)
    private float smeltingXp;
    @Serialize(value = "requiresSmelting", reflect = false)
    private boolean requiresSmelting;

    @Serialize(value = "hardness", reflect = false)
    private float hardness;
    @Serialize(value = "resistance", reflect = false)
    private float resistance;

    @Serialize(value = "harvestLevel", reflect = false)
    private int harvestLevel;

    @SerializationConstructor
    public OreComponent(@Name("material") MaterialComponent material, @Name("dimension") Dimension dimension, @Name("maxDrops") int maxDrops, @Name("minDrops") int minDrops, @Name("maxVein") int maxVein, @Name("minVein") int minVein, @Name("maxY") int maxY, @Name("minY") int minY, @Name("minOccurrences") int minOccurrences, @Name("maxOccurrences") int maxOccurrences, @Name("requiresSmelting") boolean requiresSmelting, @Name("smeltingXp") float smeltingXp, @Name("hardness") float hardness, @Name("resistance") float resistance, @Name("harvestLevel") int harvestLevel) {
        this.material = material;
        this.dimension = dimension;
        this.maxDrops = maxDrops;
        this.minDrops = minDrops;
        this.maxVein = maxVein;
        this.minVein = minVein;
        this.maxY = maxY;
        this.minY = minY;
        this.minOccurrences = minOccurrences;
        this.maxOccurrences = maxOccurrences;
        this.requiresSmelting = requiresSmelting;
        this.smeltingXp = smeltingXp;
        this.hardness = hardness;
        this.resistance = resistance;
        this.harvestLevel = harvestLevel;

        this.item = RandoresBlocks.getOreItem(dimension, material.getType());
    }

    public Block getBlock() {
        return this.item.getBlock();
    }

    public MaterialComponent getMaterial() {
        return this.material;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public int getMaxDrops() {
        return this.maxDrops;
    }

    public int getMinDrops() {
        return this.minDrops;
    }

    public int getMaxVein() {
        return this.maxVein;
    }

    public int getMinVein() {
        return this.minVein;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMinOccurrences() {
        return this.minOccurrences;
    }

    public int getMaxOccurrences() {
        return this.maxOccurrences;
    }

    public float getSmeltingXp() {
        return this.smeltingXp;
    }

    public float getHardness() {
        return this.hardness;
    }

    public float getResistance() {
        return this.resistance;
    }

    public boolean isRequiresSmelting() {
        return this.requiresSmelting;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    @Override
    public Item item() {
        return this.item;
    }

    @Override
    public int quantity() {
        return 1;
    }

    @Override
    public String name() {
        return Keys.ORE;
    }

    @Override
    public EntityEquipmentSlot slot() {
        return EntityEquipmentSlot.MAINHAND;
    }
}
