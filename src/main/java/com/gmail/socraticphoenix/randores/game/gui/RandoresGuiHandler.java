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
package com.gmail.socraticphoenix.randores.game.gui;

import com.gmail.socraticphoenix.randores.game.crafting.forge.CraftiniumForgeContainer;
import com.gmail.socraticphoenix.randores.game.crafting.forge.CraftiniumForgeGui;
import com.gmail.socraticphoenix.randores.game.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.randores.game.crafting.table.CraftiniumTableContainer;
import com.gmail.socraticphoenix.randores.game.crafting.table.CraftiniumTableGui;
import com.gmail.socraticphoenix.randores.game.tome.ItemTome;
import com.gmail.socraticphoenix.randores.game.tome.TomeGui;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

public class RandoresGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == RandoresGuiType.FORGE.ordinal()) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof CraftiniumForgeTileEntity) {
                return new CraftiniumForgeContainer((CraftiniumForgeTileEntity) entity, player.inventory);
            }
        } else if (ID == RandoresGuiType.TABLE.ordinal()) {
            return new CraftiniumTableContainer(player.inventory, world, pos);
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == RandoresGuiType.FORGE.ordinal()) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof CraftiniumForgeTileEntity) {
                return new CraftiniumForgeGui((CraftiniumForgeTileEntity) entity, player.inventory);
            }
        } else if (ID == RandoresGuiType.TABLE.ordinal()) {
            return new CraftiniumTableGui(player.inventory, world, new BlockPos(x, y, z));
        } else if (ID == RandoresGuiType.TOME.ordinal()) {
            ItemStack[] stacks = new ItemStack[] {player.getHeldItemMainhand(), player.getHeldItemOffhand()};
            for(ItemStack stack : stacks) {
                if(stack.getItem() instanceof ItemTome && RandoresItemData.hasData(stack)) {
                    RandoresItemData data = new RandoresItemData(stack);
                    if(MaterialDefinitionRegistry.contains(data)) {
                        return new TomeGui(MaterialDefinitionRegistry.get(data).buildPages());
                    }
                }
            }

            return new TomeGui(Arrays.asList(new TomeGui.StringElement("An example text page: \n" + TextFormatting.RED + "This tome has no data associated with it, so it instead displays examples of the different kinds of pages in a tome.", "Example"),
                    new TomeGui.CraftingElement("An example crafting recipe page: ", new ItemStack[][]{
                               {new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT)},
                               {null, new ItemStack(Items.STICK), null},
                               {null, new ItemStack(Items.STICK), null}
                       }, new ItemStack(Items.IRON_PICKAXE), "Example"),
                    new TomeGui.FurnaceElement("An example furnace recipe page: ", new ItemStack(Items.STICK), new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), "Example")));
        }
        return null;
    }

}
