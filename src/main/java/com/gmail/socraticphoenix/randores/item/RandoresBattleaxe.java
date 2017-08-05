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
package com.gmail.socraticphoenix.randores.item;

import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.ability.EmpoweredEnchantment;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentSweepingEdge;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RandoresBattleaxe extends RandoresItem {

    public RandoresBattleaxe(ComponentType type) {
        super(type);
    }

    public RandoresBattleaxe(CraftableType type) {
        super(type);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return (enchantment instanceof EmpoweredEnchantment || enchantment.type.canEnchantItem(Items.DIAMOND_SWORD) || enchantment.type.canEnchantItem(Items.DIAMOND_AXE)) && !(enchantment instanceof EnchantmentSweepingEdge);
    }

}
