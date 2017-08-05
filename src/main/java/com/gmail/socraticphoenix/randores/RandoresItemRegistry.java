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

import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a registry for {@link IRandoresOre}s and {@link IRandoresMaterial}s.
 */
public class RandoresItemRegistry {
    private static final RandoresItemRegistry instance = new RandoresItemRegistry();

    private List<IRandoresOre> ores = new ArrayList<>();
    private List<IRandoresMaterial> materials = new ArrayList<>();

    /**
     * @return The current instance of this registry.
     */
    public static RandoresItemRegistry instance() {
        return instance;
    }

    /**
     * Registers the given {@link IRandoresOre}.
     *
     * @param ore The ore to register.
     * @throws IllegalArgumentException If an ore with the same {@link OreType} and {@link MaterialType} is already registered.
     */
    public void register(IRandoresOre ore) {
        if (getOre(ore.getOreType(), ore.getMaterialType()) != null) {
            throw new IllegalArgumentException("An ore with type " + ore.getOreType().getName() + " and material " + ore.getMaterialType().getName() + " is already registered!");
        }
        ores.add(ore);
    }

    /**
     * Registers all of the given {@link IRandoresOre}.
     *
     * @param ores The ores to register.
     * @see RandoresItemRegistry#register(IRandoresOre)
     */
    public void register(IRandoresOre... ores) {
        Collections.addAll(this.ores, ores);
    }

    /**
     * Registers the given {@link IRandoresMaterial}.
     *
     * @param material The material to register.
     * @throws IllegalArgumentException If a material with the same {@link MaterialType} is already.
     */
    public void register(IRandoresMaterial material) {
        if (getMaterial(material.getType()) != null) {
            throw new IllegalArgumentException("A material with type " + material.getType().getName() + " is already registered!");
        }
        materials.add(material);
    }

    /**
     * Registers all of the given {@link IRandoresMaterial}.
     *
     * @param materials The materials to register.
     * @see RandoresItemRegistry#register(IRandoresMaterial)
     */
    public void register(IRandoresMaterial... materials) {
        Collections.addAll(this.materials, materials);
    }

    /**
     * Searches the registry for an {@link IRandoresOre} with the given {@link OreType} and {@link MaterialType}.
     *
     * @param oreType The oreType.
     * @param type The material type.
     * @return The ore, or null if no ore could be found.
     */
    public IRandoresOre getOre(OreType oreType, MaterialType type) {
        for (IRandoresOre ore : ores) {
            if (ore.getOreType() == oreType && ore.getMaterialType() == type) {
                return ore;
            }
        }

        return null;
    }

    /**
     * Searches the registry for an {@link IRandoresItem} with the give {@link MaterialType}.
     *
     * @param type The material type.
     * @return The material, or null of no material could be found.
     */
    public IRandoresMaterial getMaterial(MaterialType type) {
        for (IRandoresMaterial material : materials) {
            if (material.getType() == type) {
                return material;
            }
        }

        return null;
    }

    /**
     * Searches the registry for an {@link IRandoresOre} and returns the Item associated with it.
     *
     * @param oreType The oreType.
     * @param type The material type.
     * @return The Item associated with the ore, ore Item.AIR.
     */
    public Item getOreItem(OreType oreType, MaterialType type) {
        return Item.getItemFromBlock(getOre(oreType, type).getOreBlock());
    }

}
