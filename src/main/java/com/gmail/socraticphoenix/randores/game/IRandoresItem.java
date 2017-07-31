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
package com.gmail.socraticphoenix.randores.game;

import com.gmail.socraticphoenix.randores.mod.component.ComponentType;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.util.EnumHelper;

public interface IRandoresItem {
    ItemArmor.ArmorMaterial ARMOR_DEFAULT = EnumHelper.addArmorMaterial("randores_armor", "randores:armor", 100, new int[]{0, 0, 0, 0}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f);
    ItemTool.ToolMaterial TOOL_DEFAULT = EnumHelper.addToolMaterial("MATERIAL_DEFAULT", 1, 100, 1, 1, 1);

    ComponentType type();

    default Item getThis() {
        if(this instanceof Item) {
            return (Item) this;
        } else {
            throw new UnsupportedOperationException("No item: " + this.getClass().getName());
        }
    }

    default MaterialDefinition getDefinition(ItemStack stack) {
        if(RandoresItemData.hasData(stack)) {
            return MaterialDefinitionRegistry.get(new RandoresItemData(stack));
        }

        return null;
    }

    default boolean hasDefinition(ItemStack stack) {
        return RandoresItemData.hasData(stack) && stack.getItem() == this && MaterialDefinitionRegistry.contains(new RandoresItemData(stack));
    }

}
