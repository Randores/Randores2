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

import com.gmail.socraticphoenix.randores.game.item.RandoresMaterial;
import com.gmail.socraticphoenix.randores.mod.component.CraftableComponent;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RandoresItemRecipe implements IRecipe {
    private Map<Character, Ingredient> mappings = new HashMap<Character, Ingredient>() {{
        put('S', new OreIngredient("stickWood"));
        put('T', new OreIngredient("torch"));
        put('R', new OreIngredient("string"));
    }};

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        Optional<RandoresItemData> itemDataOptional = extractData(inv);
        if (itemDataOptional.isPresent()) {
            RandoresItemData data = itemDataOptional.get();
            if (MaterialDefinitionRegistry.contains(data)) {
                List<ItemStack[][]> invAdjusted = extractReduceMirror(inv);
                MaterialDefinition definition = MaterialDefinitionRegistry.get(data);
                for (CraftableComponent craftable : definition.getCraftables()) {
                    char[][] grid = genGrid(craftable.getType().getRecipe());
                    for(ItemStack[][] invView : invAdjusted) {
                        if(matches(grid, invView, definition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        Optional<RandoresItemData> itemDataOptional = extractData(inv);
        if (itemDataOptional.isPresent()) {
            RandoresItemData data = itemDataOptional.get();
            if (MaterialDefinitionRegistry.contains(data)) {
                List<ItemStack[][]> invAdjusted = extractReduceMirror(inv);
                MaterialDefinition definition = MaterialDefinitionRegistry.get(data);
                for (CraftableComponent craftable : definition.getCraftables()) {
                    char[][] grid = genGrid(craftable.getType().getRecipe());
                    for(ItemStack[][] invView : invAdjusted) {
                        if(matches(grid, invView, definition)) {
                            ItemStack stack = craftable.createStack(data);
                            stack.setCount(craftable.quantity());
                            return stack;
                        }
                    }
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    private ResourceLocation registryName = new ResourceLocation("randores", "randores.recipe.dynamic");

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

    @Override
    public boolean isHidden() {
        return true;
    }

    public boolean matches(char[][] grid, ItemStack[][] inv, MaterialDefinition definition) {
        if (grid.length != inv.length || grid.length == 0 || inv.length == 0 || grid[0].length != inv[0].length) {
            return false;
        }

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char c = grid[y][x];
                ItemStack stack = inv[y][x];
                if (c == ' ' && (stack == null || stack.isEmpty())) {
                    return false;
                } else if (c == 'X') {
                    if (!(stack.getItem() instanceof RandoresMaterial) || !RandoresItemData.hasData(stack) || !new RandoresItemData(stack).equals(definition.getData())) {
                        return false;
                    }
                } else {
                    Item target = MaterialDefinition.CRAFTING_MAPPINGS.get(c);
                    if (target == null) {
                        return false;
                    } else if (target == stack.getItem()) {
                        return true;
                    } else {
                        Ingredient ingredient = this.mappings.get(c);
                        if (!ingredient.apply(stack)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public Optional<RandoresItemData> extractData(InventoryCrafting inv) {
        RandoresItemData data = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (RandoresItemData.hasData(stack) && stack.getItem() instanceof RandoresMaterial) {
                if (data != null) {
                    return Optional.empty();
                }
                data = new RandoresItemData(stack);
            }
        }
        return Optional.ofNullable(data);
    }

    public List<ItemStack[][]> extractReduceMirror(InventoryCrafting inv) {
        ItemStack[][] grid = new ItemStack[inv.getHeight()][inv.getWidth()];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = inv.getStackInRowAndColumn(y, x);
            }
        }
        return mirror(reduceBlank(grid));
    }

    public char[][] genGrid(String[] recipe) {
        char[][] grid = new char[recipe.length][];
        for (int i = 0; i < recipe.length; i++) {
            grid[i] = recipe[i].toCharArray();
        }
        return grid;
    }

    public List<ItemStack[][]> mirror(ItemStack[][] reduced) {
        List<ItemStack[][]> mirrors = new ArrayList<>();
        mirrors.add(reduced);

        ItemStack[][] reflect = new ItemStack[reduced.length][];
        for (int y = 0; y < reflect.length; y++) {
            reflect[y] = new ItemStack[reduced[y].length];
            int n = 0;
            for (int x = reduced[y].length - 1; x >= 0; x--) {
                reflect[y][n++] = reduced[y][x];
            }
        }
        mirrors.add(reflect);

        return mirrors;
    }

    public ItemStack[][] reduceBlank(ItemStack[][] inv) {
        int[] size = getSize(inv);
        ItemStack[][] res = new ItemStack[size[0]][size[1]];
        for (int y = 0; y < size[0]; y++) {
            for (int x = 0; x < size[1]; x++) {
                res[y][x] = inv[y + size[2]][x + size[3]];
            }
        }

        return res;
    }

    public int[] getSize(ItemStack[][] inv) {
        int width = inv[0].length;
        int height = inv.length;
        int widthOff = 0;
        int heightOff = 0;

        boolean full = false;
        for (int y = 0; y < inv.length; y++) {
            boolean empty = true;
            for (int x = 0; x < inv[0].length; x++) {
                if (inv[y][x] != null && !inv[y][x].isEmpty()) {
                    empty = false;
                    full = true;
                    break;
                }
            }

            if (empty) {
                height--;
                if (!full) {
                    heightOff++;
                }
            }
        }

        full = false;
        for (int x = 0; x < inv[0].length; x++) {
            boolean empty = true;

            for (int y = 0; y < inv.length; y++) {
                if (inv[y][x] != null && !inv[y][x].isEmpty()) {
                    empty = false;
                    full = true;
                    break;
                }
            }

            if (empty) {
                width--;
                if (!full) {
                    widthOff++;
                }
            }
        }

        return new int[]{height, width, heightOff, widthOff};
    }

}
