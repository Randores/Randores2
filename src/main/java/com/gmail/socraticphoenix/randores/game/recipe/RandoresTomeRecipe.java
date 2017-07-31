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

import com.gmail.socraticphoenix.randores.game.crafting.CraftingItems;
import com.gmail.socraticphoenix.randores.game.item.RandoresMaterial;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RandoresTomeRecipe implements IRecipe {
    private Ingredient book;

    public RandoresTomeRecipe(Ingredient book) {
        this.book = book;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean book = false;
        boolean material = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (this.book.apply(stack)) {
                    if (book) {
                        return false;
                    } else {
                        book = true;
                    }
                } else if (stack.getItem() instanceof RandoresMaterial && RandoresItemData.hasData(stack)) {
                    if(material) {
                        return false;
                    } else {
                        material = true;
                    }
                } else {
                    return false; //Not a book, not a data'd material, not empty
                }
            }
        }
        return book && material;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        RandoresItemData data = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack.getItem() instanceof RandoresMaterial) {
                data = new RandoresItemData(stack);
                break;
            }
        }

        if(data == null) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = new ItemStack(CraftingItems.tome);
            data.applyTo(stack);
            return stack;
        }
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    private ResourceLocation registryName = new ResourceLocation("randores", "randores.recipes.tome");

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
