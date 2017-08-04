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

import com.gmail.socraticphoenix.randores.component.Component;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import com.gmail.socraticphoenix.randores.item.RandoresMaterial;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RandoresItemRecipe implements IRecipe {
    private Map<Character, Ingredient> mappings;
    private ComponentType type;
    private char[][] grid;
    private int quantity;

    public RandoresItemRecipe(Map<Character, Ingredient> mappings, CraftableType type, char[][] grid, int quantity) {
        this.mappings = mappings;
        this.type = ComponentType.craftable(type);
        this.grid = reduceBlank(grid);
        this.quantity = quantity;
    }

    public Map<Character, Ingredient> getMappings() {
        return this.mappings;
    }

    public ComponentType getType() {
        return this.type;
    }

    public char[][] getGrid() {
        return this.grid;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        Optional<RandoresItemData> itemDataOptional = extractData(inv);
        if (itemDataOptional.isPresent()) {
            RandoresItemData data = itemDataOptional.get();
            if (RandoresWorldData.contains(data)) {
                List<ItemStack[][]> invAdjusted = extractReduceMirror(inv);
                MaterialDefinition definition = RandoresWorldData.get(data);
                if (this.type.has(definition)) {
                    for (ItemStack[][] invView : invAdjusted) {
                        if (matches(grid, invView, definition)) {
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
            if (RandoresWorldData.contains(data)) {
                MaterialDefinition definition = RandoresWorldData.get(data);
                Component component = this.type.from(definition);
                ItemStack res = definition.createStack(component);
                res.setCount(this.quantity);
                return res;
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
                if (c == ' ') {
                    if (stack != null && !stack.isEmpty()) {
                        return false;
                    }
                } else if (c == '#') {
                    if (!(stack.getItem() instanceof RandoresMaterial) || !RandoresItemData.hasData(stack) || !new RandoresItemData(stack).equals(definition.getData())) {
                        return false;
                    }
                } else {
                    Ingredient target = this.mappings.get(c);
                    if (target == null) {
                        return false;
                    } else if (!target.apply(stack)) {
                        return false;
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
                RandoresItemData stackData = new RandoresItemData(stack);
                if (data != null && !data.equals(stackData)) {
                    return Optional.empty();
                }
                data = new RandoresItemData(stack);
            }
        }
        return Optional.ofNullable(data);
    }

    public List<ItemStack[][]> extractReduceMirror(InventoryCrafting inv) {
        ItemStack[][] grid = new ItemStack[inv.getHeight()][inv.getWidth()];
        for (int y = 0; y < inv.getHeight(); y++) {
            for (int x = 0; x < inv.getWidth(); x++) {
                grid[y][x] = inv.getStackInRowAndColumn(x, y);
            }
        }
        return mirror(reduceBlank(grid));
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
        int widthK = inv[0].length;
        int heightK = inv.length;

        int width = inv[0].length;
        int height = inv.length;
        int widthOff = 0;
        int heightOff = 0;

        top:
        for (int y = 0; y < heightK; y++) {
            for (int x = 0; x < widthK; x++) {
                ItemStack stack = inv[y][x];
                if (stack != null && !stack.isEmpty()) {
                    break top;
                }
            }
            height--;
            heightOff++;
        }

        bottom:
        for (int y = heightK - 1; y >= 0; y--) {
            for (int x = 0; x < widthK; x++) {
                ItemStack stack = inv[y][x];
                if (stack != null && !stack.isEmpty()) {
                    break bottom;
                }
            }
            height--;
        }

        left:
        for (int x = 0; x < widthK; x++) {
            for (int y = 0; y < heightK; y++) {
                ItemStack stack = inv[y][x];
                if (stack != null && !stack.isEmpty()) {
                    break left;
                }
            }
            width--;
            widthOff++;
        }

        right:
        for (int x = widthK - 1; x >= 0; x--) {
            for (int y = 0; y < heightK; y++) {
                ItemStack stack = inv[y][x];
                if (stack != null && !stack.isEmpty()) {
                    break right;
                }
            }
            width--;
        }

        return new int[]{height, width, heightOff, widthOff};
    }

    public char[][] reduceBlank(char[][] inv) {
        int[] size = getSize(inv);
        char[][] res = new char[size[0]][size[1]];
        for (int y = 0; y < size[0]; y++) {
            for (int x = 0; x < size[1]; x++) {
                res[y][x] = inv[y + size[2]][x + size[3]];
            }
        }

        return res;
    }

    public int[] getSize(char[][] inv) {
        int widthK = inv[0].length;
        int heightK = inv.length;

        int width = inv[0].length;
        int height = inv.length;
        int widthOff = 0;
        int heightOff = 0;

        top:
        for (int y = 0; y < heightK; y++) {
            for (int x = 0; x < widthK; x++) {
                char stack = inv[y][x];
                if (stack != ' ') {
                    break top;
                }
            }
            height--;
            heightOff++;
        }

        bottom:
        for (int y = heightK - 1; y >= 0; y--) {
            for (int x = 0; x < widthK; x++) {
                char stack = inv[y][x];
                if (stack != ' ') {
                    break bottom;
                }
            }
            height--;
        }

        left:
        for (int x = 0; x < widthK; x++) {
            for (int y = 0; y < heightK; y++) {
                char stack = inv[y][x];
                if (stack != ' ') {
                    break left;
                }
            }
            width--;
            widthOff++;
        }

        right:
        for (int x = widthK - 1; x >= 0; x--) {
            for (int y = 0; y < heightK; y++) {
                char stack = inv[y][x];
                if (stack != ' ') {
                    break right;
                }
            }
            width--;
        }

        return new int[]{height, width, heightOff, widthOff};
    }

}
