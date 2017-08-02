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
package com.gmail.socraticphoenix.randores.mod.component;

import com.gmail.socraticphoenix.randores.translations.Keys;
import net.minecraft.client.resources.I18n;

public enum CraftableType {
    AXE(Keys.AXE, false, true, true, false, true, true),
    HOE(Keys.HOE, false, true, true, false, true, false),
    PICKAXE(Keys.PICKAXE, false, true, true, false, true, true),
    SHOVEL(Keys.SHOVEL, false, true, true, false, true, true),
    HELMET(Keys.HELMET, false, true, true, true, false, false),
    CHESTPLATE(Keys.CHESTPLATE, false, true, true, true, false, false),
    LEGGINGS(Keys.LEGGINGS, false, true, true, true, false, false),
    BOOTS(Keys.BOOTS, false, true, true, true, false, false),
    SWORD(Keys.SWORD, false, true, true, false, true, false),
    BATTLEAXE(Keys.BATTLEAXE, false, true, true, false, true, true),
    SLEDGEHAMMER(Keys.SLEDGEHAMMER, false, true, true, false, true, false),
    BOW(Keys.BOW, false, true, true, false, false, false),
    STICK(Keys.STICK, false, false, false, false, false, false),
    BRICKS(Keys.BRICKS, true, false, false, false, false, false),
    TORCH(Keys.TORCH, true, false, false, false, false, false);

    private String name;

    private boolean block;
    private boolean enchant;
    private boolean durable;
    private boolean armor;
    private boolean damage;
    private boolean tool;

    CraftableType(String name, boolean block, boolean enchant, boolean durable, boolean armor, boolean damage, boolean tool) {
        this.name = name;
        this.enchant = enchant;
        this.durable = durable;
        this.armor = armor;
        this.damage = damage;
        this.tool = tool;
        this.block = block;
    }

    public boolean isTool() {
        return this.tool;
    }

    public boolean isArmor() {
        return this.armor;
    }

    public boolean isDamage() {
        return this.damage;
    }

    public boolean isEnchant() {
        return this.enchant;
    }

    public boolean isDurable() {
        return this.durable;
    }

    public boolean isBlock() {
        return this.block;
    }

    public String getName() {
        return this.name;
    }

    public String getLocalName() {
        return I18n.format(this.getName());
    }

}