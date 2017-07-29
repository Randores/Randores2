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
package com.gmail.socraticphoenix.randores.game.crafting.forge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CraftiniumDelegateSmelt implements CraftiniumSmelt {

    @Override
    public boolean matches(ItemStack in, World worldIn, BlockPos forge) {
        return !FurnaceRecipes.instance().getSmeltingResult(in).isEmpty();
    }

    @Override
    public ItemStack result(ItemStack in, World worldIn, BlockPos forge) {
        return FurnaceRecipes.instance().getSmeltingResult(in).copy();
    }

    @Override
    public boolean matchesExperience(ItemStack out, World world, BlockPos forge) {
        return FurnaceRecipes.instance().getSmeltingExperience(out) > 0;
    }

    @Override
    public float experience(ItemStack in, World worldIn, BlockPos forge) {
        return FurnaceRecipes.instance().getSmeltingExperience(in);
    }

    @Override
    public int maxResult(ItemStack in, World worldIn, BlockPos forge) {
        return FurnaceRecipes.instance().getSmeltingResult(in).getCount();
    }

}