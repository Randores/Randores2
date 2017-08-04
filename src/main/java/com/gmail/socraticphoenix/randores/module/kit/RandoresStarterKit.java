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
package com.gmail.socraticphoenix.randores.module.kit;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.Component;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.crafting.CraftingItems;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandoresStarterKit {
    private static Random random = new Random();
    private static String[] armor = {RandoresKeys.HELMET, RandoresKeys.CHESTPLATE, RandoresKeys.LEGGINGS, RandoresKeys.BOOTS};
    private static String[] tools = {RandoresKeys.PICKAXE, RandoresKeys.AXE, RandoresKeys.SHOVEL, RandoresKeys.HOE};

    @SubscribeEvent
    public void onMobSpawn(LivingSpawnEvent ev) {
        if (ev.getEntity() instanceof EntityPlayer) {
            EntityPlayer entity = (EntityPlayer) ev.getEntity();
            if (!entity.world.isRemote && !entity.getEntityData().getBoolean("randores_applied_kit")) {
                if (Randores.getConfigObj().getModules().isStarterKit()) {
                    List<MaterialDefinition> definitions = RandoresWorldData.getAll(RandoresWorldData.getId(entity.world));
                    MaterialDefinition material;
                    int times = 0;
                    do {
                        material = definitions.get(random.nextInt(definitions.size()));
                        times++;
                    }
                    while ((!ComponentType.craftable(RandoresKeys.HELMET).has(material) || !ComponentType.craftable(RandoresKeys.PICKAXE).has(material)) && times < 500);

                    List<Component> applicable = new ArrayList<>();
                    applicable.add(ComponentType.craftable(RandoresKeys.SWORD).from(material));
                    applicable.add(ComponentType.craftable(RandoresKeys.AXE).from(material));
                    applicable.add(ComponentType.craftable(RandoresKeys.BATTLEAXE).from(material));
                    applicable.add(ComponentType.craftable(RandoresKeys.SLEDGEHAMMER).from(material));

                    while (applicable.contains(null)) {
                        applicable.remove(null);
                    }

                    if (!applicable.isEmpty()) {
                        Component component = applicable.get(random.nextInt(applicable.size()));
                        entity.inventory.addItemStackToInventory(material.createStack(component));
                        if (ComponentType.craftable(RandoresKeys.BOW).is(component)) {
                            entity.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, 16));
                        }
                    }

                    if (ComponentType.craftable(RandoresKeys.PICKAXE).has(material)) {
                        for (String component : tools) {
                            entity.inventory.addItemStackToInventory(ComponentType.craftable(component).from(material).createStack(material.getData()));
                        }
                    }

                    if (ComponentType.craftable(RandoresKeys.HELMET).has(material)) {
                        for (String component : armor) {
                            Component comp = ComponentType.craftable(component).from(material);
                            entity.setItemStackToSlot(comp.slot(), comp.createStack(material.getData()));
                        }
                    }

                    ItemStack stack = new ItemStack(CraftingItems.tome);
                    material.getData().applyTo(stack);
                    entity.inventory.addItemStackToInventory(stack);
                }
            }

            if (!entity.world.isRemote) {
                entity.getEntityData().setBoolean("randores_applied_kit", true);
            }
        }
    }

}
