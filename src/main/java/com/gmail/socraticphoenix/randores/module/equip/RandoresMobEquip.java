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
package com.gmail.socraticphoenix.randores.module.equip;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.Component;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.component.ability.EmpoweredEnchantment;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import com.gmail.socraticphoenix.randores.probability.RandoresProbability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandoresMobEquip {
    private static Random random = new Random();
    private static String[] armor = {RandoresKeys.HELMET, RandoresKeys.CHESTPLATE, RandoresKeys.LEGGINGS, RandoresKeys.BOOTS};

    @SubscribeEvent
    public void onMobSpawn(EntityJoinWorldEvent ev) {
        Entity entity1 = ev.getEntity();
        if (entity1 instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) entity1;
            if (!entity.world.isRemote && !entity.getEntityData().getBoolean("randores_applied_equip")) {
                entity.getEntityData().setBoolean("randores_applied_equip", true);
                if (Randores.getConfigObj().getModules().isMobEquip()) {
                    if (RandoresProbability.percentChance(20, random)) {
                        List<MaterialDefinition> materials = RandoresWorldData.getAll(RandoresWorldData.getId(entity.world));
                        if (!materials.isEmpty()) {
                            MaterialDefinition material = materials.get(random.nextInt(materials.size()));
                            if (entity instanceof AbstractSkeleton || entity instanceof EntityZombie) {
                                List<Component> applicable = new ArrayList<>();
                                applicable.add(ComponentType.craftable(RandoresKeys.SWORD).from(material));
                                applicable.add(ComponentType.craftable(RandoresKeys.AXE).from(material));
                                applicable.add(ComponentType.craftable(RandoresKeys.BATTLEAXE).from(material));
                                applicable.add(ComponentType.craftable(RandoresKeys.SLEDGEHAMMER).from(material));

                                while (applicable.contains(null)) {
                                    applicable.remove(null);
                                }

                                if (!applicable.isEmpty()) {
                                    entity.setDropChance(EntityEquipmentSlot.MAINHAND, 0.25f);
                                    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, applyEnchant(applicable.get(random.nextInt(applicable.size())).createStack(material.getData()), entity.getRNG()));
                                }

                                if (ComponentType.craftable(RandoresKeys.HELMET).has(material)) {
                                    for (String type : armor) {
                                        Component component = ComponentType.craftable(type).from(material);
                                        entity.setItemStackToSlot(component.slot(), applyEnchant(component.createStack(material.getData()), entity.getRNG()));
                                        entity.setDropChance(component.slot(), 0.25f);
                                    }
                                }
                            } else if (entity instanceof EntityVindicator) {
                                List<Component> applicable = new ArrayList<>();
                                applicable.add(ComponentType.craftable(RandoresKeys.BATTLEAXE).from(material));
                                applicable.add(ComponentType.craftable(RandoresKeys.AXE).from(material));

                                while (applicable.contains(null)) {
                                    applicable.remove(null);
                                }

                                if (!applicable.isEmpty()) {
                                    entity.setDropChance(EntityEquipmentSlot.MAINHAND, 0.25f);
                                    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, applyEnchant(applicable.get(random.nextInt(applicable.size())).createStack(material.getData()), entity.getRNG()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static ItemStack applyEnchant(ItemStack stack, Random random) {
        if (RandoresProbability.percentChance(5, random)) {
            stack.addEnchantment(EmpoweredEnchantment.INSTANCE, 1);
        }

        return stack;
    }

}
