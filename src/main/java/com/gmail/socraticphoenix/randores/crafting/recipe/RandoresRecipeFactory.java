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

import com.gmail.socraticphoenix.randores.component.enumerable.CraftableType;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableTypeRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandoresRecipeFactory implements IRecipeFactory {
    public static final Map<CraftableType, RandoresItemRecipe> RECIPES = new HashMap<>();

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        JsonArray pattern = JsonUtils.getJsonArray(json, "pattern");
        List<String> strs = new ArrayList<>();
        for (JsonElement element : pattern) {
            strs.add(element.getAsString());
        }

        char[][] grid = new char[strs.size()][];
        for (int i = 0; i < strs.size(); i++) {
            grid[i] = strs.get(i).toCharArray();
        }
        if (grid.length > 3 || (grid.length > 0 && grid[0].length > 3)) {
            throw new JsonSyntaxException("Pattern may not be larger than 3x3");
        }

        Map<Character, Ingredient> ingredientMap = new HashMap<>();
        if (json.has("key")) {
            JsonObject keys = JsonUtils.getJsonObject(json, "key");
            keys.entrySet().forEach(entry -> {
                String key = entry.getKey();
                if (key.length() != 1) {
                    throw new JsonSyntaxException("Keys must be 1 character, was: " + key);
                } else if (key.equals(" ")) {
                    throw new JsonSyntaxException("' ' is a reserved key");
                } else if (key.equals("#")) {
                    throw new JsonSyntaxException("'#' is a reserved key");
                }

                Ingredient ingredient = CraftingHelper.getIngredient(entry.getValue(), context);
                ingredientMap.put(key.charAt(0), ingredient);
            });
        }

        String component = JsonUtils.getString(json, "component");

        CraftableType type = CraftableTypeRegistry.instance().get(component);
        if (type == null) {
            throw new JsonSyntaxException("Unknown component " + component);
        }

        RandoresItemRecipe recipe = new RandoresItemRecipe(ingredientMap, type, grid, JsonUtils.getInt(json, "quantity"));
        RECIPES.put(type, recipe);
        return recipe;
    }

}
