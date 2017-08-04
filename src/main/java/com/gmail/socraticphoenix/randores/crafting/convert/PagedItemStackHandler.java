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
package com.gmail.socraticphoenix.randores.crafting.convert;

import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class PagedItemStackHandler extends ItemStackHandler {
    private int index;

    public PagedItemStackHandler(int index) {
        this.index = index;
    }

    public PagedItemStackHandler(int size, int index) {
        super(size);
        this.index = index;
    }

    public PagedItemStackHandler(NonNullList<ItemStack> stacks, int index) {
        super(stacks);
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public PagedItemStackHandler setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        super.setStackInSlot(slot + this.index, stack);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return super.getStackInSlot(slot + this.index);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot + this.index, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return super.getSlotLimit(slot + this.index);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return super.getStackLimit(slot + this.index, stack);
    }

    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

}
