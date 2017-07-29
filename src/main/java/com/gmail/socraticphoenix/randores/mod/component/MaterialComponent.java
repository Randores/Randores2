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
import com.gmail.socraticphoenix.randores.game.item.RandoresItems;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

@Serializable
public class MaterialComponent implements Component {
    private Item item;

    @Serialize(value = "type", reflect = false)
    private MaterialType type;
    @Serialize(value = "armorReduction", reflect = false, comments = "Armor reduction values for helmet, chestplate, leggings, and boots, in that order")
    private int[] armorReduction;
    @Serialize(value = "harvestLevel", reflect = false, comments = "The harvest level tools made from this material work on")
    private int harvestLevel;
    @Serialize(value = "durability", reflect = false)
    private int maxUses;
    @Serialize(value = "efficiency", reflect = false)
    private float efficiency;
    @Serialize(value = "damage", reflect = false)
    private float damage;
    @Serialize(value = "toughness", reflect = false)
    private float toughness;
    @Serialize(value = "enchantability", reflect = false)
    private int enchantability;

    @SerializationConstructor
    public MaterialComponent(@Name("type") MaterialType type, @Name("armorReduction") int[] armorReduction, @Name("harvestLevel") int harvestLevel, @Name("durability") int maxUses, @Name("efficiency") float efficiency, @Name("damage") float damage, @Name("toughness") float toughness, @Name("enchantability") int enchantability) {
        this.type = type;
        this.armorReduction = armorReduction;
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.damage = damage;
        this.toughness = toughness;
        this.enchantability = enchantability;
        this.item = RandoresItems.getMaterial(type);
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
        return this.type.getName();
    }

    @Override
    public EntityEquipmentSlot slot() {
        return EntityEquipmentSlot.MAINHAND;
    }

    public MaterialType getType() {
        return this.type;
    }

    public int[] getArmorReduction() {
        return this.armorReduction;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    public float getDamage() {
        return this.damage;
    }

    public float getToughness() {
        return this.toughness;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

}
