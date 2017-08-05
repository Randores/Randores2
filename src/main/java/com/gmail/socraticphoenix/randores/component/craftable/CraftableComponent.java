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
package com.gmail.socraticphoenix.randores.component.craftable;

import com.gmail.socraticphoenix.jlsc.serialization.annotation.Name;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serialize;
import com.gmail.socraticphoenix.randores.component.Component;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

@Serializable
public class CraftableComponent implements Component {
    @Serialize(value = "type", reflect = false)
    private CraftableType type;
    @Serialize(value = "quantity", reflect = false)
    private int quantity;

    @SerializationConstructor
    public CraftableComponent(@Name("type") CraftableType type, @Name("quantity") int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public CraftableComponent setType(CraftableType type) {
        this.type = type;
        return this;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public CraftableComponent setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Item getItem() {
        return this.type.getItem().get();
    }

    public CraftableType getType() {
        return this.type;
    }

    @Override
    public Item item() {
        return this.getItem();
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
        return this.type.getSlot();
    }

}
