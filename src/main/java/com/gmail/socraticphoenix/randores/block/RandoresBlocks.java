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
package com.gmail.socraticphoenix.randores.block;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.RandoresItemRegistry;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import com.gmail.socraticphoenix.randores.item.RandoresItemBlock;
import com.gmail.socraticphoenix.randores.tab.RandoresTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class RandoresBlocks {

    public static RandoresBlock brick;
    public static RandoresTorch torch;

    public static List<Block> blocks;


    public static RandoresItemBlock brickItem;
    public static RandoresItemBlock torchItem;

    public static List<RandoresItemBlock> blockItems;

    public static void init() {
        blocks = new ArrayList<>();

        blockItems = new ArrayList<>();
        RandoresTab oreTab = null;
        for(OreType oreType : Randores.getDefaultOres()) {
            for(MaterialType materialType : Randores.getDefaultMaterials()) {
                RandoresOre ore = new RandoresOre(Material.ROCK, oreType, materialType);

                RandoresItemBlock oreItem = new RandoresItemBlock(ore);
                oreItem.setUnlocalizedName(RandoresKeys.ORE + "_" + materialType.getOreName() + "_" + oreType.getName());
                blockItems.add(oreItem);

                ore.setUnlocalizedName(RandoresKeys.ORE + "_" + materialType.getOreName() + "_" + oreType.getName()).setHardness(3.0F).setResistance(5.0F).setCreativeTab(oreTab == null ? (oreTab = new RandoresTab("randores_ores", ore)) : oreTab);
                blocks.add(ore);
                RandoresItemRegistry.instance().register(ore);
            }
        }

        brick = new RandoresBlock(Material.ROCK, ComponentType.craftable(CraftableTypeRegistry.instance().get(RandoresKeys.BRICKS)), SoundType.STONE);
        brick.setHarvestLevel("pickaxe", 1);

        brickItem = new RandoresItemBlock(brick);
        brickItem.setUnlocalizedName(RandoresKeys.BRICKS);
        blockItems.add(brickItem);

        brick.setUnlocalizedName(RandoresKeys.BRICKS).setHardness(2f).setResistance(10f).setCreativeTab(new RandoresTab("randores_bricks", brick));
        blocks.add(brick);


        torch = new RandoresTorch();

        torchItem = new RandoresItemBlock(torch);
        torchItem.setUnlocalizedName(RandoresKeys.TORCH);
        blockItems.add(torchItem);

        torch.setUnlocalizedName(RandoresKeys.TORCH).setHardness(0.0F).setLightLevel(0.9375F).setCreativeTab(new RandoresTab("randores_torches", torch));
        blocks.add(torch);

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
        OreDictionary.registerOre("torch", RandoresBlocks.torchItem);
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
