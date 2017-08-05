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
package com.gmail.socraticphoenix.randores;

import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.util.EnumHelper;

/**
 * An interface representing a Randores Item or Block. All Items or Blocks used or registered in Randores must implement
 * this interface.
 */
public interface IRandoresItem {
    ItemArmor.ArmorMaterial ARMOR_DEFAULT = EnumHelper.addArmorMaterial("ARMOR_DEFAULT", "randores:armor", 100, new int[]{0, 0, 0, 0}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f);
    ItemTool.ToolMaterial TOOL_DEFAULT = EnumHelper.addToolMaterial("MATERIAL_DEFAULT", 1, 100, 1, 1, 1);

    /**
     * @return The {@link ComponentType} associated with this IRandoresItem
     */
    ComponentType type();

    /**
     * @return The Item associated with this IRandoresItem. May be an ItemBlock for blocks.
     */
    default Item getThis() {
        if(this instanceof Item) {
            return (Item) this;
        } else if (this instanceof Block) {
            return Item.getItemFromBlock((Block) this);
        } else {
            throw new UnsupportedOperationException("Can't implement getThis for " + this.getClass().getName() + ", tried instanceof Block and instanceof Item");
        }
    }

    /**
     * Gets the {@link MaterialDefinition} associated with the given ItemStack.
     *
     * @param stack The ItemStack.
     * @return The {@link MaterialDefinition} associated with the given ItemStack, or null if  there is no associated definition.
     */
    default MaterialDefinition getDefinition(ItemStack stack) {
        if(RandoresItemData.hasData(stack)) {
            return RandoresWorldData.get(new RandoresItemData(stack));
        }

        return null;
    }

    /**
     * Tests whether or not the given ItemStack has an {@link MaterialDefinition}.
     *
     * @param stack The ItemStack.
     * @return True if {@link IRandoresItem#getDefinition(ItemStack)} would return a non-null value, false otherwise.
     */
    default boolean hasDefinition(ItemStack stack) {
        return RandoresItemData.hasData(stack) && stack.getItem() == this && RandoresWorldData.contains(new RandoresItemData(stack));
    }

}
