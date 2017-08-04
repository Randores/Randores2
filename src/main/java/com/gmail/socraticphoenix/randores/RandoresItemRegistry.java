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
package com.gmail.socraticphoenix.randores;

import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandoresItemRegistry {
    private static List<IRandoresOre> ores = new ArrayList<>();
    private static List<IRandoresMaterial> materials = new ArrayList<>();

    public static void register(IRandoresOre ore) {
        if(getOre(ore.getOreType(), ore.getMaterialType()) != null) {
            throw new IllegalArgumentException("An ore with dimension " + ore.getOreType().getName() + " and material " + ore.getMaterialType().getName() + " is already registered!");
        }
        ores.add(ore);
    }

    public static void register(IRandoresOre... ores) {
        Collections.addAll(RandoresItemRegistry.ores, ores);
    }

    public static void register(IRandoresMaterial material) {
        if(getMaterial(material.getType()) != null) {
            throw new IllegalArgumentException("A material with type " + material.getType().getName() + " is already registered!");
        }
        materials.add(material);
    }

    public static void Register(IRandoresMaterial... materials) {
        Collections.addAll(RandoresItemRegistry.materials, materials);
    }

    public static IRandoresOre getOre(OreType oreType, MaterialType type) {
        for(IRandoresOre ore : ores) {
            if(ore.getOreType() == oreType && ore.getMaterialType() == type) {
                return ore;
            }
        }

        return null;
    }

    public static IRandoresMaterial getMaterial(MaterialType type) {
        for(IRandoresMaterial material : materials) {
            if(material.getType() == type) {
                return material;
            }
        }

        return null;
    }

    public static Item getOreItem(OreType oreType, MaterialType type) {
        return Item.getItemFromBlock(getOre(oreType, type).getOreBlock());
    }
}
