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
package com.gmail.socraticphoenix.randores.component.tome;

import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.component.ability.Ability;
import com.gmail.socraticphoenix.randores.component.ability.AbilitySeries;
import com.gmail.socraticphoenix.randores.component.ability.AbilityType;
import com.gmail.socraticphoenix.randores.component.craftable.CraftableComponent;
import com.gmail.socraticphoenix.randores.component.property.Properties;
import com.gmail.socraticphoenix.randores.crafting.recipe.RandoresItemRecipe;
import com.gmail.socraticphoenix.randores.crafting.recipe.RandoresRecipeFactory;
import com.gmail.socraticphoenix.randores.item.TomeGui;
import com.gmail.socraticphoenix.randores.item.TomeGui.Element;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class DefaultTomeHook implements TomeHook {
    @Override
    public List<Element> pages(MaterialDefinition definition) {
        List<TomeGui.Element> pages = new ArrayList<>();
        String title = TextFormatting.DARK_AQUA + I18n.format(RandoresKeys.TOME).replace("${name}", definition.getName()) + TextFormatting.RESET;

        List<String> s = new ArrayList<String>();
        s.add(title);
        s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.ORE_HARVEST_LEVEL) + ": " + definition.getOre().getHarvestLevel());
        s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.TYPE) + ": " + definition.getMaterial().getLocalName());
        if (definition.hasTool()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.HARVEST_LEVEL) + ": " + definition.getMaterial().getHarvestLevel());
            s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.EFFICIENCY) + ": " + definition.getMaterial().getEfficiency());
        }

        if (definition.hasDurable()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.DURABILITY) + ": " + definition.getMaterial().getMaxUses());
        }

        if (definition.hasEnchant()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.ENCHANTABILITY) + ": " + definition.getMaterial().getEnchantability());
        }

        if (definition.hasDamage()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.DAMAGE) + ": " + definition.getMaterial().getDamage());
        }

        if (definition.hasArmor()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(RandoresKeys.FULL_ARMOR) + ": " + definition.getFullArmor());
        }

        StringBuilder b = new StringBuilder();
        for (String k : s) {
            b.append(k).append("\n");
        }

        pages.add(new TomeGui.StringElement(b.substring(0, b.length() - 1), RandoresKeys.INFORMATION));

        pages.add(new TomeGui.FurnaceElement(title + "\n" + TextFormatting.DARK_GREEN + definition.formatLocalName(definition.getMaterial()), new ItemStack(Items.STICK), definition.getOre().createStack(definition.getData()), definition.getMaterial().createStack(definition.getData()), RandoresKeys.OBTAINING));
        for (CraftableComponent component : definition.getCraftables()) {
            ItemStack[][] recipe = new ItemStack[3][3];
            RandoresItemRecipe itemRecipe = RandoresRecipeFactory.RECIPES.get(component.getType());
            if(itemRecipe != null) {
                char[][] grid = itemRecipe.getGrid();
                for (int y = 0; y < grid.length; y++) {
                    char[] row = grid[y];
                    for (int x = 0; x < row.length; x++) {
                        ItemStack stack = null;
                        char c = row[x];
                        if(c == '#') {
                            stack = definition.getMaterial().createStack(definition.getData());
                        } else if (itemRecipe.getMappings().containsKey(c)) {
                            ItemStack[] matching = itemRecipe.getMappings().get(c).getMatchingStacks();
                            if(matching.length > 0) {
                                stack = matching[0];
                            }
                        }

                        recipe[y][x] = stack;
                    }
                }

                ItemStack result = component.createStack(definition.getData());
                result.setCount(itemRecipe.getQuantity());
                pages.add(new TomeGui.CraftingElement(title + "\n" + TextFormatting.DARK_GREEN + definition.formatLocalName(component), recipe, result, RandoresKeys.RECIPES));
            }
        }

        if (definition.hasProperty(Properties.FLAMMABLE)) {
            pages.add(new TomeGui.FurnaceElement(title + "\n" + TextFormatting.DARK_GREEN + definition.formatLocalName(definition.getMaterial()), definition.getMaterial().createStack(definition.getData()), new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), RandoresKeys.PROPERTIES));
        }

        if(definition.hasDamage()) {
            this.applyAbilityToTome(title, AbilityType.MELEE, definition.getAbilitySeries(), pages);
        }

        if(ComponentType.craftable(RandoresKeys.BOW).has(definition)) {
            this.applyAbilityToTome(title, AbilityType.PROJECTILE, definition.getAbilitySeries(), pages);
        }

        if(definition.hasArmor()) {
            this.applyAbilityToTome(title, AbilityType.ARMOR_ACTIVE, definition.getAbilitySeries(), pages);
            this.applyAbilityToTome(title, AbilityType.ARMOR_PASSIVE, definition.getAbilitySeries(), pages);
        }

        return pages;
    }

    @SideOnly(Side.CLIENT)
    private void applyAbilityToTome(String title, AbilityType type, AbilitySeries series, List<TomeGui.Element> pages) {
        List<String> a = new ArrayList<>();
        a.add(title);
        a.add(TextFormatting.GOLD + I18n.format(RandoresKeys.ABILITIES) + ": " + type.getLocalName());
        List<Ability> abilities = series.getSeries(type);
        int i = 0;
        for (Ability ab : abilities) {
            i++;
            a.add(TextFormatting.DARK_GREEN + " " + i + ". " + ab.getLocalName());
        }

        StringBuilder bu = new StringBuilder();
        for (String k : a) {
            bu.append(k).append("\n");
        }
        pages.add(new TomeGui.StringElement(bu.substring(0, bu.length() - 1), RandoresKeys.ABILITIES));
    }
    
}
