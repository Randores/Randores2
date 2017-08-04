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
package com.gmail.socraticphoenix.randores.crafting.recipe;

import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.crafting.convert.CraftiniumConvert;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RandoresToOreDictConvert implements CraftiniumConvert {

    @Override
    public boolean matches(ItemStack in, World worldIn, BlockPos table, EntityPlayer player) {
        return !this.findInOreDict(in).isEmpty();
    }

    @Override
    public NonNullList<ItemStack> conversions(ItemStack in, World worldIn, BlockPos table, EntityPlayer player) {
        return this.findInOreDict(in);
    }

    private NonNullList<ItemStack> findInOreDict(ItemStack stack) {
        if (RandoresItemData.hasData(stack)) {
            return RandoresWorldData.delegate(new RandoresItemData(stack), this::findInOreDict, NonNullList::create);
        }
        return NonNullList.create();
    }

    private NonNullList<ItemStack> findInOreDict(MaterialDefinition definition) {
        return OreDictionary.getOres(definition.getMaterial().getType().getDictName() + definition.getName());
    }

}
