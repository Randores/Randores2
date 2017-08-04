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
package com.gmail.socraticphoenix.randores.item;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.RandoresItemRegistry;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import com.gmail.socraticphoenix.randores.tab.RandoresTab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.oredict.OreDictionary;
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

    public static List<Item> items;

    public static void init() {
        items = new ArrayList<>();

        RandoresTab materialsTab = null;
        for(MaterialType type : Randores.getDefaultMaterials()) {
            RandoresMaterial material = new RandoresMaterial(type);
            material.setUnlocalizedName(type.getName()).setCreativeTab(materialsTab == null ? (materialsTab = new RandoresTab("randores_materials", material)) : materialsTab);
            items.add(material);
            RandoresItemRegistry.register(material);
        }

        pickaxe = new RandoresItem(RandoresKeys.PICKAXE);
        pickaxe.setUnlocalizedName(RandoresKeys.PICKAXE).setCreativeTab(new RandoresTab("randores_pickaxes", pickaxe));
        items.add(pickaxe);

        axe = new RandoresItem(RandoresKeys.AXE);
        axe.setUnlocalizedName(RandoresKeys.AXE).setCreativeTab(new RandoresTab("randores_axes", axe));
        items.add(axe);

        shovel = new RandoresItem(RandoresKeys.SHOVEL);
        shovel.setUnlocalizedName(RandoresKeys.SHOVEL).setCreativeTab(new RandoresTab("randores_spades", shovel));
        items.add(shovel);

        hoe = new RandoresItem(RandoresKeys.HOE);
        hoe.setUnlocalizedName(RandoresKeys.HOE).setCreativeTab(new RandoresTab("randores_hoes", hoe));
        items.add(hoe);

        sword = new RandoresItem(RandoresKeys.SWORD);
        sword.setUnlocalizedName(RandoresKeys.SWORD).setCreativeTab(new RandoresTab("randores_swords", sword));
        items.add(sword);

        bow = new RandoresBow(ComponentType.craftable(RandoresKeys.BOW));
        bow.setUnlocalizedName(RandoresKeys.BOW).setCreativeTab(new RandoresTab("randores_bows", bow));
        items.add(bow);

        stick = new RandoresBasicItem(ComponentType.craftable(RandoresKeys.STICK));
        stick.setUnlocalizedName(RandoresKeys.STICK).setCreativeTab(new RandoresTab("randores_sticks", stick));
        items.add(stick);

        battleaxe = new RandoresBattleaxe(ComponentType.craftable(RandoresKeys.BATTLEAXE));
        battleaxe.setUnlocalizedName(RandoresKeys.BATTLEAXE).setCreativeTab(new RandoresTab("randores_battleaxes", battleaxe));
        items.add(battleaxe);

        sledgehammer = new RandoresSledgehammer(ComponentType.craftable(RandoresKeys.SLEDGEHAMMER));
        sledgehammer.setUnlocalizedName(RandoresKeys.SLEDGEHAMMER).setCreativeTab(new RandoresTab("randores_sledgehammers", sledgehammer));
        items.add(sledgehammer);

        helmet = new RandoresItemArmor(CraftableTypeRegistry.instance().get(RandoresKeys.HELMET));
        helmet.setUnlocalizedName(RandoresKeys.HELMET).setCreativeTab(Randores.TAB_ARMOR);
        items.add(helmet);

        chestplate = new RandoresItemArmor(CraftableTypeRegistry.instance().get(RandoresKeys.CHESTPLATE));
        chestplate.setUnlocalizedName(RandoresKeys.CHESTPLATE).setCreativeTab(Randores.TAB_ARMOR);
        items.add(chestplate);

        leggings = new RandoresItemArmor(CraftableTypeRegistry.instance().get(RandoresKeys.LEGGINGS));
        leggings.setUnlocalizedName(RandoresKeys.LEGGINGS).setCreativeTab(Randores.TAB_ARMOR);
        items.add(leggings);

        boots = new RandoresItemArmor(CraftableTypeRegistry.instance().get(RandoresKeys.BOOTS));
        boots.setUnlocalizedName(RandoresKeys.BOOTS).setCreativeTab(Randores.TAB_ARMOR);
        items.add(boots);

        for(Item item : items) {
            item.setRegistryName(item.getUnlocalizedName().substring(5));
        }
    }

    public static void register(IForgeRegistry<Item> registry) {
        for(Item item : items) {
            registry.register(item);
            Randores.debug("Registered " + item.getUnlocalizedName());
        }
        OreDictionary.registerOre("stickWood", RandoresItems.stick);
    }

    public static void registerModels() {
        for(Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5), "inventory"));
            Randores.debug("Registered model " + item.getUnlocalizedName());
        }
    }

}
