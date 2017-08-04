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
package com.gmail.socraticphoenix.randores.waila;

import com.gmail.socraticphoenix.randores.IRandoresItem;
import com.gmail.socraticphoenix.randores.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.crafting.forge.CraftiniumForge;
import com.gmail.socraticphoenix.randores.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class RandoresWailaHandler implements IWailaDataProvider {
    private boolean forgeMode;

    public RandoresWailaHandler(boolean forgeMode) {
        this.forgeMode = forgeMode;
    }

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerStackProvider(new RandoresWailaHandler(false), IRandoresItem.class);
        registrar.registerBodyProvider(new RandoresWailaHandler(false), IRandoresItem.class);
        registrar.registerBodyProvider(new RandoresWailaHandler(true), CraftiniumForgeTileEntity.class);
        registrar.registerNBTProvider(new RandoresWailaHandler(true), CraftiniumForgeTileEntity.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        if (!this.forgeMode && iWailaDataAccessor.getBlock() instanceof IRandoresItem) {
            ItemStack stack = new ItemStack(((IRandoresItem) iWailaDataAccessor.getBlock()).getThis());
            TileEntity entity = iWailaDataAccessor.getTileEntity();
            if (entity != null && entity instanceof RandoresTileEntity) {
                ((RandoresTileEntity) entity).getData().applyTo(stack);
            }
            return stack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        Block block = iWailaDataAccessor.getBlock();
        if (this.forgeMode && block instanceof CraftiniumForge) {
            NBTTagCompound randores = iWailaDataAccessor.getNBTData().getCompoundTag("randores");
            int cookTime = randores.getInteger("cook_time");
            int totalCookTime = randores.getInteger("cook_time_total");
            int burnTime = randores.getInteger("burn_time");
            if (burnTime > 0) {
                ItemStackHandler input = new ItemStackHandler();
                ItemStackHandler output = new ItemStackHandler();
                ItemStackHandler fuel = new ItemStackHandler();
                input.deserializeNBT(randores.getCompoundTag("input"));
                fuel.deserializeNBT(randores.getCompoundTag("fuel"));
                output.deserializeNBT(randores.getCompoundTag("output"));

                ItemStack[] inventory = new ItemStack[3];
                inventory[0] = input.getStackInSlot(0);
                inventory[1] = fuel.getStackInSlot(0);
                inventory[2] = output.getStackInSlot(0);
                for (int i = 0; i < inventory.length; i++) {
                    if (inventory[i] == ItemStack.EMPTY) {
                        inventory[i] = null;
                    }
                }

                String renderStr = "";

                if (inventory[0] != null) {
                    String name = inventory[0].getItem().getRegistryName().toString();
                    renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(inventory[0].getCount()), String.valueOf(inventory[0].getItemDamage()));
                } else {
                    renderStr += SpecialChars.getRenderString("waila.stack", "2");
                }

                if (inventory[1] != null) {
                    String name = inventory[1].getItem().getRegistryName().toString();
                    renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(inventory[1].getCount()), String.valueOf(inventory[1].getItemDamage()));
                } else {
                    renderStr += SpecialChars.getRenderString("waila.stack", "2");
                }

                renderStr += SpecialChars.getRenderString("waila.progress", String.valueOf(cookTime), String.valueOf(totalCookTime));

                if (inventory[2] != null) {
                    String name = inventory[2].getItem().getRegistryName().toString();
                    renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(inventory[2].getCount()), String.valueOf(inventory[2].getItemDamage()));
                } else {
                    renderStr += SpecialChars.getRenderString("waila.stack", "2");
                }
                list.add(renderStr);
                list.add(I18n.format(RandoresKeys.FORGE_POWER) + ": " + randores.getInteger("furnace_speed"));
            }
        }

        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity te, NBTTagCompound tag, World world, BlockPos blockPos) {
        if (this.forgeMode && te != null) {
            te.writeToNBT(tag);
        }
        return tag;
    }


}
