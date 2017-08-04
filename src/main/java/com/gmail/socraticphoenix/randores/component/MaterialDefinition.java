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
package com.gmail.socraticphoenix.randores.component;

import com.gmail.socraticphoenix.jlsc.serialization.annotation.Name;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serialize;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.ability.AbilitySeries;
import com.gmail.socraticphoenix.randores.component.craftable.CraftableComponent;
import com.gmail.socraticphoenix.randores.component.property.MaterialProperty;
import com.gmail.socraticphoenix.randores.component.tome.TomeHookRegistry;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.item.TomeGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Serializable
public class MaterialDefinition {

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

    private UUID id;
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

    public MaterialDefinition setColor(Color color) {
        this.color = color;
        return this;
    }

    public MaterialDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public MaterialDefinition setMaterial(MaterialComponent material) {
        this.material = material;
        return this;
    }

    public MaterialDefinition setComponents(List<Component> components) {
        this.components = components;
        return this;
    }

    public List<MaterialProperty> getPropertiesList() {
        return this.propertiesList;
    }

    public MaterialDefinition setPropertiesList(List<MaterialProperty> propertiesList) {
        this.propertiesList = propertiesList;
        return this;
    }

    public MaterialDefinition setProperties(Map<String, MaterialProperty> properties) {
        this.properties = properties;
        return this;
    }

    public MaterialDefinition setToolMaterial(ToolMaterial toolMaterial) {
        this.toolMaterial = toolMaterial;
        return this;
    }

    public MaterialDefinition setArmorMaterial(ArmorMaterial armorMaterial) {
        this.armorMaterial = armorMaterial;
        return this;
    }

    public MaterialDefinition setAbilitySeries(AbilitySeries abilitySeries) {
        this.abilitySeries = abilitySeries;
        return this;
    }

    public MaterialDefinition setId(UUID id) {
        this.id = id;
        return this;
    }

    public MaterialDefinition setIndex(int index) {
        this.index = index;
        return this;
    }

    public MaterialDefinition setOre(OreComponent ore) {
        this.ore = ore;
        return this;
    }

    public MaterialDefinition setCraftables(List<CraftableComponent> craftables) {
        this.craftables = craftables;
        return this;
    }

    public void provideData(UUID id, int index) {
        if(this.id != null) {
            throw new IllegalStateException("Provided data twice");
        }

        this.id = id;
        this.index = index;
    }

    public boolean hasProvidedData() {
        return id != null;
    }

    @SideOnly(Side.CLIENT)
    public String formatLocalName(Component sub) {
        LocalDateTime time = LocalDateTime.now();
        if(time.getMonth() == Month.APRIL && time.getDayOfMonth() == 1) {
            return "April Fools!";
        }

        String format = I18n.format(RandoresKeys.FORMAT);
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

    public UUID getId() {
        return this.id;
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
        return new RandoresItemData(this.getIndex(), this.getId());
    }

    public List<TomeGui.Element> buildPages() {
        return TomeHookRegistry.instance().buildPages(this);
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

    public int getFullArmor() {
        return this.totalArmor;
    }

}
