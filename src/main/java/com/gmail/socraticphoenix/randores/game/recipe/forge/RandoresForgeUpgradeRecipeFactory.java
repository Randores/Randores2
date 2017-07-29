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
package com.gmail.socraticphoenix.randores.game.recipe.forge;

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

import java.util.LinkedHashMap;
import java.util.Map;

public class RandoresForgeUpgradeRecipeFactory implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        int clamp = JsonUtils.getInt(json, "clamp");
        boolean combining = JsonUtils.getBoolean(json, "combining");

        JsonArray upgradeList = JsonUtils.getJsonArray(json, "upgrades");
        Map<Ingredient, Double> upgrades = new LinkedHashMap<>();
        int n = 0;
        for (JsonElement element : upgradeList) {
            if (element.isJsonObject()) {
                JsonObject upgrade = element.getAsJsonObject();
                double amount = JsonUtils.getFloat(upgrade, "amount");
                Ingredient ingredient = CraftingHelper.getIngredient(upgrade.get("ingredient"), context);
                upgrades.put(ingredient, amount);
            } else {
                throw new JsonSyntaxException("Expected " + n + " to be a JsonObject, was " + JsonUtils.toString(json));
            }
            n++;
        }

        return new RandoresForgeUpgradeRecipe(clamp, combining, upgrades);
    }

}
