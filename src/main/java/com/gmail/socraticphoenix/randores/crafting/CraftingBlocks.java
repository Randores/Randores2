/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
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
package com.gmail.socraticphoenix.randores.crafting;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.crafting.convert.CraftiniumConverter;
import com.gmail.socraticphoenix.randores.crafting.forge.CraftiniumForge;
import com.gmail.socraticphoenix.randores.crafting.table.CraftiniumTable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class CraftingBlocks {

    public static Block craftiniumTable;
    public static Block craftiniumForge;
    public static Block craftiniumForgeLit;
    public static Block craftiniumConverter;
    public static Block craftiniumOre;

    public static List<Block> blocks;

    public static ItemBlock tableItem;
    public static ItemBlock forgeItem;
    public static ItemBlock forgeLitItem;
    public static ItemBlock converterItem;
    public static ItemBlock oreItem;

    public static List<ItemBlock> blockItems;

    public static void init() {
        blocks = new ArrayList<>();

        craftiniumTable = new CraftiniumTable();
        craftiniumTable.setCreativeTab(Randores.TAB_CRAFTING);
        blocks.add(craftiniumTable);

        craftiniumForge = new CraftiniumForge(false);
        craftiniumForge.setCreativeTab(Randores.TAB_CRAFTING);
        blocks.add(craftiniumForge);

        craftiniumOre = new CraftiniumOre();
        craftiniumOre.setCreativeTab(Randores.TAB_CRAFTING);
        blocks.add(craftiniumOre);

        craftiniumConverter = new CraftiniumConverter();
        craftiniumConverter.setCreativeTab(Randores.TAB_CRAFTING);
        blocks.add(craftiniumConverter);

        craftiniumForgeLit = new CraftiniumForge(true);
        blocks.add(craftiniumForgeLit);


        blockItems = new ArrayList<>();

        tableItem = new ItemBlock(craftiniumTable);
        tableItem.setUnlocalizedName("craftinium_table");
        blockItems.add(tableItem);

        forgeItem = new ItemBlock(craftiniumForge);
        forgeItem.setUnlocalizedName("craftinium_forge");
        blockItems.add(forgeItem);

        forgeLitItem = new ItemBlock(craftiniumForgeLit);
        forgeLitItem.setUnlocalizedName("craftinium_forge_lit");
        blockItems.add(forgeLitItem);

        converterItem = new ItemBlock(craftiniumConverter);
        converterItem.setUnlocalizedName("craftinium_converter");
        blockItems.add(converterItem);

        oreItem = new ItemBlock(craftiniumOre);
        oreItem.setUnlocalizedName("craftinium_ore");
        blockItems.add(oreItem);

        for(Item item : blockItems) {
            item.setRegistryName(item.getUnlocalizedName().substring(5));
        }

        for(Block block : blocks) {
            block.setRegistryName(block.getUnlocalizedName().substring(5));
        }
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        for(Item item : blockItems) {
            registry.register(item);
            Randores.debug("Registered " + item.getUnlocalizedName());
        }
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for(Block block : blocks) {
            registry.register(block);
            Randores.debug("Registered block " + block.getUnlocalizedName());
        }
    }

    public static void registerModels() {
        for(Item item : blockItems) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5), "inventory"));
            Randores.debug("Registered model " + item.getUnlocalizedName());
        }
    }

}
