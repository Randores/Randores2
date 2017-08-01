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
package com.gmail.socraticphoenix.randores.mod.listener;

import com.gmail.socraticphoenix.randores.game.item.RandoresItemHelper;
import com.gmail.socraticphoenix.randores.game.item.RandoresSledgehammer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingHurtListener {

    @SubscribeEvent
    public void onHurt(LivingHurtEvent ev) {
        DamageSource source = ev.getSource();
        Entity root = source.getImmediateSource();
        if (root instanceof EntityLivingBase) {
            EntityLivingBase cause = (EntityLivingBase) root;
            EntityLivingBase hurt = ev.getEntityLiving();

            EnumHand active = cause.getActiveHand();
            ItemStack stack = cause.getHeldItem(active);
            RandoresItemHelper.doEmpowered(stack, hurt, cause);

            if(stack.getItem() instanceof RandoresSledgehammer) {
                Vec3d vector = hurt.getPositionVector().subtract(cause.getPositionVector()).normalize().scale(2);
                hurt.addVelocity(vector.x, 0.5, vector.z);
            }
        }
    }

}
