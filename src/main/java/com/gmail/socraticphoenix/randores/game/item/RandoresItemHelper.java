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
package com.gmail.socraticphoenix.randores.game.item;

import com.gmail.socraticphoenix.randores.game.IRandoresItem;
import com.gmail.socraticphoenix.randores.game.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.component.ability.EmpoweredEnchantment;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class RandoresItemHelper {

    public static void doEmpowered(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if(stack.getItem() instanceof IRandoresItem && EmpoweredEnchantment.appliedTo(stack) && RandoresItemData.hasData(stack)) {
            MaterialDefinitionRegistry.delegateVoid(new RandoresItemData(stack), m -> m.getAbilitySeries().onMeleeHit(attacker, target), () -> {});
        }
    }

    public static void getRawDrop(NonNullList<ItemStack> drops, TileEntity entity, IBlockState state) {
        if(entity != null && entity instanceof RandoresTileEntity) {
            RandoresItemData data = ((RandoresTileEntity) entity).getData();
            if(RandoresItemBlock.ITEM_BY_BLOCK.containsKey(state.getBlock())) {
                RandoresItemBlock itemBlock = RandoresItemBlock.ITEM_BY_BLOCK.get(state.getBlock());
                ItemStack stack = new ItemStack(itemBlock);
                data.applyTo(stack);
                drops.add(stack);
            }
        }
    }

}
