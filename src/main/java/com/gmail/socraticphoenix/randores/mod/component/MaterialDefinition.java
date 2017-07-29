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
package com.gmail.socraticphoenix.randores.mod.component;

import com.gmail.socraticphoenix.jlsc.serialization.annotation.Name;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serialize;
import com.gmail.socraticphoenix.randores.game.tome.TomeGui;
import com.gmail.socraticphoenix.randores.mod.component.ability.Ability;
import com.gmail.socraticphoenix.randores.mod.component.ability.AbilitySeries;
import com.gmail.socraticphoenix.randores.mod.component.ability.AbilityType;
import com.gmail.socraticphoenix.randores.mod.component.property.MaterialProperty;
import com.gmail.socraticphoenix.randores.mod.component.property.Properties;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.translations.Keys;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Serializable
public class MaterialDefinition {
    public static final Map<Character, Item> CRAFTING_MAPPINGS = new HashMap<>();

    @Serialize(value = "color", reflect = false)
    private Color color;
    @Serialize(value = "name", reflect = false)
    private String name;

    private MaterialComponent material;
    private List<Component> components;

    @Serialize(value = "properties", reflect = false)
    private List<MaterialProperty> propertiesList;
    private Map<String, MaterialProperty> properties;

    private Item.ToolMaterial toolMaterial;
    private ItemArmor.ArmorMaterial armorMaterial;

    @Serialize(value = "abilities", reflect = false)
    private AbilitySeries abilitySeries;

    private long seed;
    private int totalArmor;
    private int index;

    @Serialize(value = "ore", reflect = false)
    private OreComponent ore;
    @Serialize(value = "components", reflect = false)
    private List<CraftableComponent> craftables;

    @SerializationConstructor
    public MaterialDefinition(@Name("color") Color color, @Name("name") String name, @Name("ore") OreComponent ore, @Name("components") List<CraftableComponent> craftables, @Name("properties") List<MaterialProperty> properties, @Name("abilities") AbilitySeries series) {
        this.abilitySeries = series;
        this.color = color;
        this.ore = ore;
        this.material = ore.getMaterial();
        this.craftables = craftables;
        this.name = name;
        this.toolMaterial = EnumHelper.addToolMaterial(this.name, this.material.getHarvestLevel(), this.material.getMaxUses(), this.material.getEfficiency(), this.material.getDamage(), this.material.getEnchantability());
        this.totalArmor = sum(this.material.getArmorReduction());
        this.armorMaterial = EnumHelper.addArmorMaterial(this.name, "randores:armor", this.material.getMaxUses() / 10, this.material.getArmorReduction(), this.material.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, this.material.getToughness());

        this.components = new ArrayList<>();
        this.components.addAll(this.craftables);
        this.components.add(this.ore);
        this.components.add(this.material);

        this.propertiesList = properties;
        this.properties = new HashMap<>();
        for (MaterialProperty property : properties) {
            this.properties.put(property.name(), property);
        }
    }

    public void provideData(long seed, int index) {
        if(this.seed != 0) {
            throw new IllegalStateException("Provided data twice");
        }

        this.seed = seed;
        this.index = index;
    }

    public String formatLocalName(Component sub) {
        String format = I18n.format(Keys.FORMAT);
        String name = this.getName();
        String compName = sub.getLocalName();
        return format.replace("${name}", name).replace("${item}", compName);
    }

    public ItemStack createStack(Component component) {
        return component.createStack(this.getData());
    }

    public ItemStack createStack(ComponentType type) {
        if(type.has(this)) {
            return this.createStack(type.from(this));
        } else {
            return ItemStack.EMPTY;
        }
    }

    private int sum(int[] arr) {
        int a = 0;
        for (int i : arr) {
            a += i;
        }
        return a;
    }

    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public OreComponent getOre() {
        return this.ore;
    }

    public MaterialComponent getMaterial() {
        return this.material;
    }

    public List<CraftableComponent> getCraftables() {
        return this.craftables;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public Map<String, MaterialProperty> getProperties() {
        return this.properties;
    }

    public ToolMaterial getToolMaterial() {
        return this.toolMaterial;
    }

    public ArmorMaterial getArmorMaterial() {
        return this.armorMaterial;
    }

    public AbilitySeries getAbilitySeries() {
        return this.abilitySeries;
    }

    public long getSeed() {
        return this.seed;
    }

    public int getTotalArmor() {
        return this.totalArmor;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean hasProperty(String name) {
        return this.properties.containsKey(name);
    }

    public RandoresItemData getData() {
        return new RandoresItemData(this.getIndex(), this.getSeed());
    }

    @SideOnly(Side.CLIENT)
    public List<TomeGui.Element> buildPages() {
        List<TomeGui.Element> pages = new ArrayList<TomeGui.Element>();
        String title = TextFormatting.DARK_AQUA + I18n.format(Keys.TOME).replace("${name}", this.getName()) + TextFormatting.RESET;

        List<String> s = new ArrayList<String>();
        s.add(title);
        s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.ORE_HARVEST_LEVEL) + ": " + this.ore.getHarvestLevel());
        s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.TYPE) + ": " + this.material.getLocalName());
        if (this.hasTool()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.HARVEST_LEVEL) + ": " + this.material.getHarvestLevel());
            s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.EFFICIENCY) + ": " + this.material.getEfficiency());
        }

        if (this.hasDurable()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.DURABILITY) + ": " + this.material.getMaxUses());
        }

        if (this.hasEnchant()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.ENCHANTABILITY) + ": " + this.material.getEnchantability());
        }

        if (this.hasDamage()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.DAMAGE) + ": " + this.material.getDamage());
        }

        if (this.hasArmor()) {
            s.add(TextFormatting.DARK_GREEN + I18n.format(Keys.FULL_ARMOR) + ": " + this.totalArmor);
        }

        StringBuilder b = new StringBuilder();
        for (String k : s) {
            b.append(k).append("\n");
        }

        pages.add(new TomeGui.StringElement(b.substring(0, b.length() - 1), Keys.INFORMATION));

        pages.add(new TomeGui.FurnaceElement(title + "\n" + TextFormatting.DARK_GREEN + this.formatLocalName(this.material), new ItemStack(Items.STICK), this.ore.createStack(this.getData()), this.material.createStack(this.getData()), Keys.OBTAINING));
        for (CraftableComponent component : this.craftables) {
            ItemStack[][] recipe = new ItemStack[3][3];
            String[] template = component.getType().getRecipe();
            for (int i = 0; i < template.length; i++) {
                String row = template[i];
                for (int j = 0; j < row.length(); j++) {
                    char c = row.charAt(j);
                    ItemStack stack = null;
                    if (c == 'X') {
                        stack = this.material.createStack(this.getData());
                    } else if (CRAFTING_MAPPINGS.containsKey(c)) {
                        stack = new ItemStack(CRAFTING_MAPPINGS.get(c));
                    }
                    if (stack != null) {
                        recipe[i][j] = stack;
                    }
                }
            }

            ItemStack result = component.createStack(this.getData());
            pages.add(new TomeGui.CraftingElement(title + "\n" + TextFormatting.DARK_GREEN + this.formatLocalName(component), recipe, result, Keys.RECIPES));
        }

        if (this.hasProperty(Properties.FLAMMABLE)) {
            pages.add(new TomeGui.FurnaceElement(title + "\n" + TextFormatting.DARK_GREEN + this.formatLocalName(this.material), this.material.createStack(this.getData()), new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), Keys.PROPERTIES));
        }

        if(this.hasDamage()) {
            this.applyAbilityToTome(title, AbilityType.MELEE, pages);
        }

        if(ComponentType.craftable(CraftableType.BOW).has(this)) {
            this.applyAbilityToTome(title, AbilityType.PROJECTILE, pages);
        }

        if(this.hasArmor()) {
            this.applyAbilityToTome(title, AbilityType.ARMOR_ACTIVE, pages);
            this.applyAbilityToTome(title, AbilityType.ARMOR_PASSIVE, pages);
        }

        return pages;
    }

    @SideOnly(Side.CLIENT)
    private void applyAbilityToTome(String title, AbilityType type, List<TomeGui.Element> pages) {
        List<String> a = new ArrayList<>();
        a.add(title);
        a.add(TextFormatting.GOLD + I18n.format(Keys.ABILITIES) + ": " + type.getLocalName());
        List<Ability> abilities = this.abilitySeries.getSeries(type);
        int i = 0;
        for (Ability ab : abilities) {
            i++;
            a.add(TextFormatting.DARK_GREEN + " " + i + ". " + ab.getLocalName());
        }

        StringBuilder bu = new StringBuilder();
        for (String k : a) {
            bu.append(k).append("\n");
        }
        pages.add(new TomeGui.StringElement(bu.substring(0, bu.length() - 1), Keys.ABILITIES));
    }

    public boolean hasTool() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isTool()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasDurable() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isDurable()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasEnchant() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isEnchant()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasArmor() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isArmor()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasDamage() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isDamage()) {
                return true;
            }
        }

        return false;
    }
}
