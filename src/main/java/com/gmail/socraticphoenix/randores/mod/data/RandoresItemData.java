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
package com.gmail.socraticphoenix.randores.mod.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Objects;

public class RandoresItemData {
    private int index;
    private long seed;

    public RandoresItemData(int index, long seed) {
        this.index = index;
        this.seed = seed;
    }

    public static boolean hasData(ItemStack stack) {
        if(stack.hasTagCompound()) {
            NBTTagCompound randores = stack.getSubCompound("randores");
            return randores != null && randores.hasKey("index") && randores.hasKey("seed");
        }

        return false;
    }

    public RandoresItemData(NBTTagCompound compound) {
        this(compound.getInteger("index"), compound.getLong("seed"));
    }

    public RandoresItemData(ItemStack stack) {
        this(stack.getOrCreateSubCompound("randores"));
    }

    public void applyTo(NBTTagCompound compound) {
        compound.setInteger("index", this.index);
        compound.setLong("seed", this.seed);
    }

    public void applyTo(ItemStack stack) {
        this.applyTo(stack.getOrCreateSubCompound("randores"));
    }

    public void absorbFrom(World world) {
        this.seed = RandoresSeed.getSeed(world);
    }

    public void absorbFrom(NBTTagCompound compound) {
        this.index = compound.getInteger("index");
        this.seed = compound.getLong("seed");
    }

    public void absorbFrom(ItemStack stack) {
        this.absorbFrom(stack.getOrCreateSubCompound("randores"));
    }

    public void set(int index, long seed) {
        this.index = index;
        this.seed = seed;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public RandoresItemData copy() {
        return new RandoresItemData(this.index, this.seed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RandoresItemData)) return false;
        RandoresItemData that = (RandoresItemData) o;
        return index == that.index &&
                seed == that.seed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, seed);
    }

}
