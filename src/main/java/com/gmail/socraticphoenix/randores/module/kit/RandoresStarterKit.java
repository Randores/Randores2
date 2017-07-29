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
import com.gmail.socraticphoenix.randores.game.crafting.CraftingItems;
import com.gmail.socraticphoenix.randores.mod.component.Component;
import com.gmail.socraticphoenix.randores.mod.component.ComponentType;
import com.gmail.socraticphoenix.randores.mod.component.CraftableType;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresSeed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandoresStarterKit {
    private static Random random = new Random();
    private static CraftableType[] armor = {CraftableType.HELMET, CraftableType.CHESTPLATE, CraftableType.LEGGINGS, CraftableType.BOOTS};
    private static CraftableType[] tools = {CraftableType.PICKAXE, CraftableType.AXE, CraftableType.SHOVEL, CraftableType.HOE};

    @SubscribeEvent
    public void onMobSpawn(PlayerEvent.PlayerLoggedInEvent ev) {
        EntityPlayer entity = ev.player;
        if (!entity.world.isRemote && !entity.getEntityData().getBoolean("randores_applied_kit")) {
            if (Randores.getConfigObj().getModules().isStarterKit()) {
                List<MaterialDefinition> definitions = MaterialDefinitionRegistry.getAll(RandoresSeed.getSeed(entity.world));
                MaterialDefinition material;
                int times = 0;
                do {
                    material = definitions.get(random.nextInt(definitions.size()));
                    times++;
                }
                while ((!ComponentType.craftable(CraftableType.HELMET).has(material) || !ComponentType.craftable(CraftableType.PICKAXE).has(material)) && times < 500);

                List<Component> applicable = new ArrayList<>();
                applicable.add(ComponentType.craftable(CraftableType.SWORD).from(material));
                applicable.add(ComponentType.craftable(CraftableType.AXE).from(material));
                applicable.add(ComponentType.craftable(CraftableType.BATTLEAXE).from(material));
                applicable.add(ComponentType.craftable(CraftableType.SLEDGEHAMMER).from(material));

                while (applicable.contains(null)) {
                    applicable.remove(null);
                }

                if (!applicable.isEmpty()) {
                    Component component = applicable.get(random.nextInt(applicable.size()));
                    entity.inventory.addItemStackToInventory(material.createStack(component));
                    if (ComponentType.craftable(CraftableType.BOW).is(component)) {
                        entity.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, 16));
                    }
                }

                if (ComponentType.craftable(CraftableType.PICKAXE).has(material)) {
                    for (CraftableType component : tools) {
                        entity.inventory.addItemStackToInventory(ComponentType.craftable(component).from(material).createStack(material.getData()));
                    }
                }

                if (ComponentType.craftable(CraftableType.HELMET).has(material)) {
                    for (CraftableType component : armor) {
                        Component comp = ComponentType.craftable(component).from(material);
                        entity.setItemStackToSlot(comp.slot(), comp.createStack(material.getData()));
                    }
                }

                ItemStack stack = new ItemStack(CraftingItems.tome);
                material.getData().applyTo(stack);
                entity.inventory.addItemStackToInventory(stack);
            }
        }

        if(!entity.world.isRemote) {
            entity.getEntityData().setBoolean("randores_applied_kit", true);
        }
    }

}
