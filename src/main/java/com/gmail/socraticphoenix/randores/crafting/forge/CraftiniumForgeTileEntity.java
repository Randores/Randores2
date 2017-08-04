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
package com.gmail.socraticphoenix.randores.crafting.forge;

import com.google.common.base.Predicates;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CraftiniumForgeTileEntity extends TileEntity implements ITickable {
    private ItemStackHandler input;
    private ItemStackHandler fuel;
    private ItemStackHandler output;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;

    private int divisor;

    private boolean brokenByCreative;

    public CraftiniumForgeTileEntity() {
        this.input = new ItemStackHandler(1);
        this.output = new SelectiveItemStackHandler(1, Predicates.alwaysFalse());
        this.fuel = new SelectiveItemStackHandler(1, TileEntityFurnace::isItemFuel);
        this.furnaceBurnTime = 0;
        this.currentItemBurnTime = 0;
        this.cookTime = 0;
        this.totalCookTime = 0;
        this.divisor = 1;
        this.brokenByCreative = false;
    }

    public boolean isBrokenByCreative() {
        return this.brokenByCreative;
    }

    public void setBrokenByCreative(boolean brokenByCreative) {
        this.brokenByCreative = brokenByCreative;
    }

    public int getDivisor() {
        return this.divisor;
    }

    public void setDivisor(int divisor) {
        this.divisor = divisor;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.world != null) {;
                this.world.markChunkDirty(this.pos, this);
            }
            if (facing == null) {
                return (T) this.input;
            } else if (facing.getAxis().isHorizontal()) {
                return (T) this.fuel;
            } else if (facing == EnumFacing.UP) {
                return (T) this.input;
            } else if (facing == EnumFacing.DOWN) {
                return (T) this.output;
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound randores = compound.getCompoundTag("randores");
        this.input.deserializeNBT(randores.getCompoundTag("input"));
        this.fuel.deserializeNBT(randores.getCompoundTag("fuel"));
        this.output.deserializeNBT(randores.getCompoundTag("output"));
        this.furnaceBurnTime = randores.getInteger("burn_time");
        this.cookTime = randores.getInteger("cook_time");
        this.totalCookTime = randores.getInteger("cook_time_total");
        this.divisor = randores.getInteger("furnace_speed");
        this.currentItemBurnTime = this.getBurnTime(this.fuel.getStackInSlot(0));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound randores = new NBTTagCompound();
        randores.setTag("input", this.input.serializeNBT());
        randores.setTag("fuel", this.fuel.serializeNBT());
        randores.setTag("output", this.output.serializeNBT());
        randores.setInteger("burn_time", this.furnaceBurnTime);
        randores.setInteger("cook_time", this.cookTime);
        randores.setInteger("cook_time_total", this.totalCookTime);
        randores.setInteger("furnace_speed", this.divisor);
        compound.setTag("randores", randores);
        return compound;
    }

    public int getFurnaceBurnTime() {
        return this.furnaceBurnTime;
    }

    public int getCurrentItemBurnTime() {
        return this.currentItemBurnTime;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public int getTotalCookTime() {
        return this.totalCookTime;
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos) <= 64;
    }

    public ItemStackHandler getInput() {
        return this.input;
    }

    public ItemStackHandler getFuel() {
        return this.fuel;
    }

    public ItemStackHandler getOutput() {
        return this.output;
    }

    private boolean canSmelt() {
        if (this.input.getStackInSlot(0).isEmpty()) {
            return false;
        } else {
            CraftiniumSmelt rec = CraftiniumSmeltRegistry.findMatching(this.input.getStackInSlot(0), this.world, this.pos);
            if (rec != null) {
                ItemStack output = rec.result(this.input.getStackInSlot(0), this.world, this.pos);
                output.setCount(rec.maxResult(this.input.getStackInSlot(0), this.world, this.pos));
                if (output.isEmpty()) {
                    return false;
                } else {
                    ItemStack currentResult = this.output.getStackInSlot(0);
                    if (currentResult.isEmpty()) {
                        return true;
                    } else if (!ItemStack.areItemsEqual(output, currentResult) || !ItemStack.areItemStackTagsEqual(output, currentResult)) {
                        return false;
                    } else {
                        int result = currentResult.getCount() + output.getCount();
                        return result <= 64 && result <= currentResult.getMaxStackSize();
                    }
                }
            } else {
                return false;
            }
        }
    }

    public void smelt() {
        if (this.canSmelt()) {
            ItemStack input = this.input.getStackInSlot(0);
            ItemStack output = CraftiniumSmeltRegistry.findMatching(input, this.world, this.pos).result(input, this.world, this.pos);
            ItemStack currentOutput = this.output.getStackInSlot(0);
            if (currentOutput.isEmpty()) {
                this.output.setStackInSlot(0, output);
            } else {
                this.output.getStackInSlot(0).setCount(output.getCount() + currentOutput.getCount());
            }
            input.shrink(1);
        }
    }

    @Override
    public void update() {
        boolean needsSave = false;
        boolean burning = this.isBurning();


        if (this.isBurning()) {
            this.furnaceBurnTime--;

        }

        if (!this.world.isRemote) {
            ItemStack fuel = this.fuel.getStackInSlot(0);
            ItemStack input = this.input.getStackInSlot(0);
            if (this.totalCookTime == 0 && this.canSmelt()) {
                this.totalCookTime = this.getCookTime(input);
                needsSave = true;
            }
            if (this.isBurning() || (!fuel.isEmpty() && !input.isEmpty())) {
                if (!this.isBurning() && this.canSmelt()) {
                    this.furnaceBurnTime = this.getBurnTime(fuel);
                    this.currentItemBurnTime = this.furnaceBurnTime;
                    if (this.isBurning()) {
                        needsSave = true;
                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);
                            if (fuel.isEmpty()) {
                                ItemStack container = item.getContainerItem(fuel);
                                this.fuel.setStackInSlot(0, container);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    this.cookTime++;
                    if (this.cookTime >= this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(input);
                        this.smelt();
                        needsSave = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (burning != this.isBurning()) {
                needsSave = true;
                CraftiniumForge.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (needsSave) {
            this.markDirty();
        }
    }

    private int getBurnTime(ItemStack stack) {
        int burn = TileEntityFurnace.getItemBurnTime(stack) / this.divisor;
        return burn == 0 ? 1 : burn;
    }

    private int getCookTime(ItemStack stack) {
        int time = 200 / this.divisor;
        return time == 0 ? 1 : time;
    }

    public NonNullList<ItemStack> getSlots() {
        NonNullList list = NonNullList.withSize(3, ItemStack.EMPTY);
        list.set(0, this.getInput().getStackInSlot(0));
        list.set(1, this.getFuel().getStackInSlot(0));
        list.set(2, this.getOutput().getStackInSlot(0));
        return list;
    }

}
