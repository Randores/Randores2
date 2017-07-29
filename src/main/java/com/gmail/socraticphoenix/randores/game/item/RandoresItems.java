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
package com.gmail.socraticphoenix.randores.game.item;

import com.gmail.socraticphoenix.randores.game.tab.RandoresTab;
import com.gmail.socraticphoenix.randores.mod.component.CraftableType;
import com.gmail.socraticphoenix.randores.mod.component.MaterialType;
import com.gmail.socraticphoenix.randores.translations.Keys;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class RandoresItems {

    public static RandoresItem pickaxe;
    public static RandoresItem axe;
    public static RandoresItem shovel;
    public static RandoresItem hoe;
    public static RandoresItem sword;
    public static RandoresBow bow;
    public static RandoresBasicItem stick;
    public static RandoresBattleaxe battleaxe;
    public static RandoresSledgehammer sledgehammer;

    public static RandoresItemArmor helmet;
    public static RandoresItemArmor chestplate;
    public static RandoresItemArmor leggings;
    public static RandoresItemArmor boots;

    public static List<RandoresMaterial> materials;

    public static List<Item> items;

    public static void init() {
        items = new ArrayList<>();

        materials = new ArrayList<>();
        RandoresTab materialsTab = null;
        for(MaterialType type : MaterialType.values()) {
            RandoresMaterial material = new RandoresMaterial(type);
            material.setUnlocalizedName(type.getName()).setCreativeTab(materialsTab == null ? (materialsTab = new RandoresTab("randores_materials", material)) : materialsTab);
            materials.add(material);
            items.add(material);
        }

        pickaxe = new RandoresItem(CraftableType.PICKAXE);
        pickaxe.setUnlocalizedName(Keys.PICKAXE).setCreativeTab(new RandoresTab("randores_pickaxes", pickaxe));
        items.add(pickaxe);

        axe = new RandoresItem(CraftableType.AXE);
        axe.setUnlocalizedName(Keys.AXE).setCreativeTab(new RandoresTab("randores_axes", axe));
        items.add(axe);

        shovel = new RandoresItem(CraftableType.SHOVEL);
        shovel.setUnlocalizedName(Keys.SHOVEL).setCreativeTab(new RandoresTab("randores_spades", shovel));
        items.add(shovel);

        hoe = new RandoresItem(CraftableType.HOE);
        hoe.setUnlocalizedName(Keys.HOE).setCreativeTab(new RandoresTab("randores_hoes", hoe));
        items.add(hoe);

        sword = new RandoresItem(CraftableType.SWORD);
        sword.setUnlocalizedName(Keys.SWORD).setCreativeTab(new RandoresTab("randores_swords", sword));
        items.add(sword);

        bow = new RandoresBow(CraftableType.BOW);
        bow.setUnlocalizedName(Keys.BOW).setCreativeTab(new RandoresTab("randores_bows", bow));
        items.add(bow);

        stick = new RandoresBasicItem(CraftableType.STICK);
        stick.setUnlocalizedName(Keys.STICK).setCreativeTab(new RandoresTab("randores_sticks", stick));
        items.add(stick);

        battleaxe = new RandoresBattleaxe(CraftableType.BATTLEAXE);
        battleaxe.setUnlocalizedName(Keys.BATTLEAXE).setCreativeTab(new RandoresTab("randores_battleaxes", battleaxe));
        items.add(battleaxe);

        sledgehammer = new RandoresSledgehammer(CraftableType.SLEDGEHAMMER);
        sledgehammer.setUnlocalizedName(Keys.SLEDGEHAMMER).setCreativeTab(new RandoresTab("randores_sledgehammers", sledgehammer));
        items.add(sledgehammer);

        helmet = new RandoresItemArmor(CraftableType.HELMET, EntityEquipmentSlot.HEAD);
        helmet.setUnlocalizedName(Keys.HELMET).setCreativeTab(new RandoresTab("randores_helmets", helmet));
        items.add(helmet);

        chestplate = new RandoresItemArmor(CraftableType.CHESTPLATE, EntityEquipmentSlot.CHEST);
        chestplate.setUnlocalizedName(Keys.CHESTPLATE).setCreativeTab(new RandoresTab("randores_chestplates", chestplate));
        items.add(chestplate);

        leggings = new RandoresItemArmor(CraftableType.LEGGINGS, EntityEquipmentSlot.LEGS);
        leggings.setUnlocalizedName(Keys.LEGGINGS).setCreativeTab(new RandoresTab("randores_leggings", leggings));
        items.add(leggings);

        boots = new RandoresItemArmor(CraftableType.BOOTS, EntityEquipmentSlot.FEET);
        boots.setUnlocalizedName(Keys.BOOTS).setCreativeTab(new RandoresTab("randores_boots", boots));
        items.add(boots);

        for(Item item : items) {
            item.setRegistryName(item.getUnlocalizedName().substring(5));
        }
    }

    public static RandoresMaterial getMaterial(MaterialType type) {
        for(RandoresMaterial material : materials) {
            if(material.getMaterialType() == type) {
                return material;
            }
        }

        return null;
    }

    public static void register(IForgeRegistry<Item> registry) {
        for(Item item : items) {
            registry.register(item);
        }
    }

    public static void registerModels() {
        for(Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5), "inventory"));
        }
    }

}
