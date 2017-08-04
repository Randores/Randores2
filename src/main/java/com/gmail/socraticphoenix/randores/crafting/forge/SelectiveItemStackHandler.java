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

import com.google.common.base.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class SelectiveItemStackHandler extends ItemStackHandler {
    private Predicate<ItemStack> acceptIn;

    public SelectiveItemStackHandler(Predicate<ItemStack> acceptIn) {
        this.acceptIn = acceptIn;
    }

    public SelectiveItemStackHandler(int size, Predicate<ItemStack> acceptIn) {
        super(size);
        this.acceptIn = acceptIn;
    }

    public SelectiveItemStackHandler(NonNullList<ItemStack> stacks, Predicate<ItemStack> acceptIn) {
        super(stacks);
        this.acceptIn = acceptIn;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(this.acceptIn.apply(stack)) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }
}
