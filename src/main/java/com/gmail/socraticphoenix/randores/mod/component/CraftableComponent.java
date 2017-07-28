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

import com.gmail.socraticphoenix.randores.game.block.RandoresBlocks;
import com.gmail.socraticphoenix.randores.game.item.RandoresItems;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class CraftableComponent implements Component {
    private CraftableType type;
    private int quantity;
    private Item item;

    public CraftableComponent(CraftableType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
        switch (type) {
            case AXE:
                this.item = RandoresItems.axe;
                break;
            case HOE:
                this.item = RandoresItems.hoe;
                break;
            case PICKAXE:
                this.item = RandoresItems.pickaxe;
                break;
            case SHOVEL:
                this.item = RandoresItems.shovel;
                break;
            case SWORD:
                this.item = RandoresItems.sword;
                break;
            case STICK:
                this.item = RandoresItems.stick;
                break;
            case BOOTS:
                this.item = RandoresItems.boots;
                break;
            case CHESTPLATE:
                this.item = RandoresItems.chestplate;
                break;
            case HELMET:
                this.item = RandoresItems.helmet;
                break;
            case LEGGINGS:
                this.item = RandoresItems.leggings;
                break;
            case BATTLEAXE:
                this.item = RandoresItems.battleaxe;
                break;
            case SLEDGEHAMMER:
                this.item = RandoresItems.sledgehammer;
                break;
            case BOW:
                this.item = RandoresItems.bow;
                break;
            case BRICKS:
                this.item = RandoresBlocks.brickItem;
                break;
            case TORCH:
                this.item = RandoresBlocks.torchItem;
                break;
        }
    }

    public CraftableType getType() {
        return this.type;
    }

    @Override
    public Item item() {
        return this.item;
    }

    @Override
    public int quantity() {
        return this.quantity;
    }

    @Override
    public String name() {
        return this.type.getName();
    }

    @Override
    public EntityEquipmentSlot slot() {
        switch (this.type) {
            case BOOTS:
                return EntityEquipmentSlot.FEET;
            case CHESTPLATE:
                return EntityEquipmentSlot.CHEST;
            case HELMET:
                return EntityEquipmentSlot.HEAD;
            case LEGGINGS:
                return EntityEquipmentSlot.LEGS;
            default:
                return EntityEquipmentSlot.MAINHAND;
        }
    }

}
