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
package com.gmail.socraticphoenix.randores.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;
import java.util.UUID;

public class RandoresItemData {
    private int index;
    private UUID id;

    public RandoresItemData(int index, UUID id) {
        this.index = index;
        this.id = id;
    }

    public static boolean hasData(ItemStack stack) {
        if(stack.hasTagCompound()) {
            NBTTagCompound randores = stack.getSubCompound("randores");
            return randores != null && randores.hasKey("index") && randores.hasKey("idMost") && randores.hasKey("idLeast");
        }

        return false;
    }

    public RandoresItemData(NBTTagCompound compound) {
        this(compound.getInteger("index"), compound.getUniqueId("id"));
    }

    public RandoresItemData(ItemStack stack) {
        this(stack.getOrCreateSubCompound("randores"));
    }

    public void applyTo(NBTTagCompound compound) {
        compound.setInteger("index", this.index);
        compound.setUniqueId("id", this.id);
    }

    public void applyTo(ItemStack stack) {
        this.applyTo(stack.getOrCreateSubCompound("randores"));
    }

    public void set(int index, UUID id) {
        this.index = index;
        this.id = id;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public UUID getId() {
        return this.id;
    }

    public RandoresItemData setId(UUID id) {
        this.id = id;
        return this;
    }

    public RandoresItemData copy() {
        return new RandoresItemData(this.index, this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RandoresItemData)) return false;
        RandoresItemData that = (RandoresItemData) o;
        return index == that.index &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, id);
    }

}
