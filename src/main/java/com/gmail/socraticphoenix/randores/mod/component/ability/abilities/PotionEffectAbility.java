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
package com.gmail.socraticphoenix.randores.mod.component.ability.abilities;

import com.gmail.socraticphoenix.randores.mod.component.ability.Ability;
import com.gmail.socraticphoenix.randores.mod.component.ability.AbilityContext;
import com.gmail.socraticphoenix.randores.mod.component.ability.AbilityStage;
import com.gmail.socraticphoenix.randores.mod.component.ability.AbilityType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionEffectAbility implements Ability {
    private Potion potion;

    public PotionEffectAbility(Potion potion) {
        this.potion = potion;
    }

    @Override
    public boolean applicableStage(AbilityStage stage) {
        return stage == AbilityStage.MIDDLE;
    }

    @Override
    public boolean applicableContext(AbilityType context) {
        return (this.potion.isBadEffect() && context != AbilityType.ARMOR_PASSIVE) || (!this.potion.isBadEffect() && !this.potion.isInstant() && context == AbilityType.ARMOR_PASSIVE);
    }

    @Override
    public int delayAfter() {
        return 0;
    }

    @Override
    public boolean apply(Vec3d location, EntityLivingBase activator, AbilityContext context) {
        if (context.getType() == AbilityType.ARMOR_PASSIVE) {
            activator.removePotionEffect(this.potion);
            activator.addPotionEffect(new PotionEffect(this.potion, 20 * 15, 1, true, false));
        } else if (context.getType() == AbilityType.ARMOR_ACTIVE) {
            this.addEffect(context.getAttacker());
        } else if (context.hasTarget()) {
            this.addEffect(context.getTarget());
        }

        return true;
    }

    private void addEffect(EntityLivingBase entity) {
        if (entity.getActivePotionEffect(this.potion) == null || entity.getActivePotionEffect(this.potion).getDuration() <= 1) {
            entity.removePotionEffect(this.potion);
            entity.addPotionEffect(new PotionEffect(this.potion, this.potion.isInstant() ? 1 : 20 * 5, 1));
        }
    }

    @Override
    public void remove(Vec3d location, EntityLivingBase activator, AbilityContext context) {
        if (context.getType() == AbilityType.ARMOR_PASSIVE) {
            activator.removePotionEffect(this.potion);
        } else if (context.getType() == AbilityType.ARMOR_ACTIVE) {
            context.getAttacker().removePotionEffect(this.potion);
        } else if (context.hasTarget()) {
            context.getTarget().removePotionEffect(this.potion);
        }
    }

    @Override
    public int weight() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getLocalName() {
        return I18n.format(this.getName());
    }

    @Override
    public String getName() {
        return this.potion.getName();
    }

}
