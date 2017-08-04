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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class CraftiniumConverterContainer extends Container {
    private World world;
    private BlockPos converter;
    private EntityPlayer player;

    private DelegatingItemStackHandler delegate;
    private PagedItemStackHandler handler;
    private CraftiniumSlotConvertInput input;

    public CraftiniumConverterContainer(InventoryPlayer playerInventory, World world, BlockPos converter) {
        this.world = world;
        this.converter = converter;
        this.player = playerInventory.player;

        this.input = new CraftiniumSlotConvertInput(this);

        this.delegate = new DelegatingItemStackHandler();
        PagedItemStackHandler handler = new PagedItemStackHandler(9, 0);
        this.handler = handler;
        this.delegate.setHandler(handler);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new CraftiniumSlotConvertOutput(this, this.delegate, j + i * 3, 30 + j * 18, 17 + i * 18)); //TODO proper offsets
            }
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public void onInputChanged() {
        ItemStack input = this.input.getStackInSlot(0);
        if (!input.isEmpty()) {
            List<CraftiniumConvert> recipes = CraftiniumConvertRegistry.findMatching(input, this.world, this.converter, this.player);
            NonNullList<ItemStack> allResults = NonNullList.create();
            for (CraftiniumConvert convert : recipes) {
                allResults.addAll(convert.conversions(input, this.world, this.converter, this.player));
            }

            NonNullList<ItemStack> toDisplay = NonNullList.withSize(Math.max(9, allResults.size()), ItemStack.EMPTY);
            for (int i = 0; i < allResults.size(); i++) {
                toDisplay.set(i, allResults.get(i));
            }

            PagedItemStackHandler handler = new PagedItemStackHandler(toDisplay, this.handler.getIndex());
            this.handler = handler;
            this.delegate.setHandler(handler);

            this.detectAndSendChanges();
        }
    }

    public void onOutputTaken() {
        ItemStack input = this.input.getStackInSlot(0);
        input.setCount(input.getCount() - 1);
        if (input.getCount() == 0) {
            input = ItemStack.EMPTY;
        }

        this.input.setStackInSlot(0, input);

        NonNullList<ItemStack> toDisplay = NonNullList.withSize(9, ItemStack.EMPTY);
        PagedItemStackHandler handler = new PagedItemStackHandler(toDisplay, this.handler.getIndex());
        this.handler = handler;
        this.delegate.setHandler(handler);

        this.detectAndSendChanges();
    }

    public void advancePage() {
        int currentIndex = this.handler.getIndex() + 9;
        while (currentIndex + 9 >= this.delegate.getSlots()) {
            currentIndex--;
        }
        this.handler.setIndex(currentIndex);

        this.detectAndSendChanges();
    }

    public void retreatPage() {
        int currentIndex = this.handler.getIndex() - 9;
        while (currentIndex - 9 < 0) {
            currentIndex++;
        }
        this.handler.setIndex(currentIndex);

        this.detectAndSendChanges();
    }

}
