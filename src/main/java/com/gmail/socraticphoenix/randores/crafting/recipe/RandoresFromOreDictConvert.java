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
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RandoresFromOreDictConvert implements CraftiniumConvert {

    @Override
    public boolean matches(ItemStack in, World worldIn, BlockPos table, EntityPlayer player) {
        return !this.findFromOreDict(worldIn, in).isEmpty();
    }

    @Override
    public NonNullList<ItemStack> conversions(ItemStack in, World worldIn, BlockPos table, EntityPlayer player) {
        NonNullList<ItemStack> conversions = NonNullList.create();
        for(MaterialDefinition definition : this.findFromOreDict(worldIn, in)) {
            conversions.add(definition.createStack(definition.getMaterial()));
        }
        return conversions;
    }

    public List<MaterialDefinition> findFromOreDict(World world, ItemStack stack) {
        List<String> names = new ArrayList<>();
        for(int index : OreDictionary.getOreIDs(stack)) {
            names.add(OreDictionary.getOreName(index));
        }

        return RandoresWorldData.getAll(RandoresWorldData.getId(world)).stream().filter(def -> !names.contains(def.getMaterial().getType().getDictName() + def.getName())).collect(Collectors.toList());
    }

}
