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
package com.gmail.socraticphoenix.randores.game.recipe;

import com.gmail.socraticphoenix.randores.game.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.randores.game.crafting.forge.CraftiniumForge;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Map.Entry;

public class RandoresForgeUpgradeRecipe implements IRecipe {
    private int clamp;
    private boolean combining;
    private Map<Ingredient, Double> upgrades;

    public RandoresForgeUpgradeRecipe(int clamp, boolean combining, Map<Ingredient, Double> upgrades) {
        this.clamp = clamp;
        this.combining = combining;
        this.upgrades = upgrades;
    }


    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        int count = 0;
        boolean forge = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty()) {
                if(stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof CraftiniumForge) {
                    if(forge && !this.combining) {
                        return false;
                    } else {
                        forge = true;
                        if(!stack.hasTagCompound() || stack.getSubCompound("randores") == null || !stack.getSubCompound("randores").hasKey("furnace_speed")) {
                            return false;
                        }
                    }
                } else if (this.upgrades.keySet().stream().noneMatch(k -> k.apply(stack))) {
                    return false;
                }
                count++;
            }
        }
        return count >= 2 && forge;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        double speed = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof CraftiniumForge) {
                speed += stack.getSubCompound("randores").getInteger("furnace_speed");
            } else {
                speed += this.upgrades.entrySet().stream().filter(e -> e.getKey().apply(stack)).map(Entry::getValue).findFirst().orElse(0d);
            }
        }

        int intSpeed = (int) speed;
        if(intSpeed > this.clamp) {
            intSpeed = this.clamp;
        }

        ItemStack res = new ItemStack(CraftingBlocks.forgeItem);
        res.getOrCreateSubCompound("randores").setInteger("furnace_speed", intSpeed);
        return res;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(CraftingBlocks.forgeItem);
    }

    private ResourceLocation registryName = new ResourceLocation("randores", "randores.recipes.forge_upgrade");

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

}
