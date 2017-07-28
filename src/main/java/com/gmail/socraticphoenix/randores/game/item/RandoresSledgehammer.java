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
package com.gmail.socraticphoenix.randores.game.item;

import com.gmail.socraticphoenix.randores.game.IRandoresItem;
import com.gmail.socraticphoenix.randores.mod.component.ComponentType;
import com.gmail.socraticphoenix.randores.mod.component.CraftableType;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.component.ability.EmpoweredEnchantment;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentSweepingEdge;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class RandoresSledgehammer extends Item implements IRandoresItem {
    private ComponentType type;

    public RandoresSledgehammer(ComponentType type) {
        this.type = type;
    }

    public RandoresSledgehammer(CraftableType type) {
        this(ComponentType.craftable(type));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return (enchantment instanceof EmpoweredEnchantment || enchantment.type.canEnchantItem(Items.DIAMOND_SWORD)) && !(enchantment instanceof EnchantmentSweepingEdge);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        Vec3d vector = target.getPositionVector().subtract(attacker.getPositionVector()).normalize().scale(2);
        target.addVelocity(vector.x, 0.5, vector.z);
        RandoresItemHelper.doEmpowered(stack, target, attacker);
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
        if (RandoresItemData.hasData(stack)) {
            RandoresItemData data = new RandoresItemData(stack);
            if(MaterialDefinitionRegistry.contains(data)) {
                MaterialDefinition definition = MaterialDefinitionRegistry.get(data);
                Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);
                if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
                    multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) definition.getMaterial().getDamage(), 0));
                    multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.2, 0));
                }

                return multimap;
            }
        }

        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) TOOL_DEFAULT.getDamageVsEntity() + 6, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.2, 0));
        }

        return multimap;

    }

    @Override
    public ComponentType type() {
        return this.type;
    }
}
