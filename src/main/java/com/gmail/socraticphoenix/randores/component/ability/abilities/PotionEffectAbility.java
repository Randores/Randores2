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
package com.gmail.socraticphoenix.randores.component.ability.abilities;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.jlsc.value.annotation.ConversionConstructor;
import com.gmail.socraticphoenix.jlsc.value.annotation.Convert;
import com.gmail.socraticphoenix.jlsc.value.annotation.Convertible;
import com.gmail.socraticphoenix.jlsc.value.annotation.Index;
import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.component.ability.Ability;
import com.gmail.socraticphoenix.randores.component.ability.AbilityContext;
import com.gmail.socraticphoenix.randores.component.ability.AbilityGenerator;
import com.gmail.socraticphoenix.randores.component.ability.AbilityStage;
import com.gmail.socraticphoenix.randores.component.ability.AbilityType;
import com.gmail.socraticphoenix.randores.plugin.RandoresPlugin;
import com.gmail.socraticphoenix.randores.probability.IntRange;
import com.gmail.socraticphoenix.randores.probability.RandoresProbability;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Convertible
public class PotionEffectAbility implements Ability {
    @Convert(value = 0, reflect = false)
    private Potion potion;
    @Convert(value = 1, reflect = false)
    private int ticks;
    @Convert(value = 2, reflect = false)
    private int amplifier;

    @ConversionConstructor
    public PotionEffectAbility(@Index(0) Potion potion, @Index(1) int ticks, @Index(2) int amplifier) {
        this.potion = potion;
        this.ticks = ticks;
        this.amplifier = amplifier;
    }

    @Override
    public int delayAfter() {
        return 0;
    }

    @Override
    public boolean apply(Vec3d location, EntityLivingBase activator, AbilityContext context) {
        if (context.getType() == AbilityType.ARMOR_PASSIVE) {
            activator.removePotionEffect(this.potion);
            activator.addPotionEffect(new PotionEffect(this.potion, this.ticks, this.amplifier, false, false));
        } else if (context.getType() == AbilityType.ARMOR_ACTIVE) {
            this.addEffect(context.getAttacker());
        } else if (context.hasTarget()) {
            this.addEffect(context.getTarget());
        }

        return true;
    }

    private void addEffect(EntityLivingBase entity) {
        entity.removePotionEffect(this.potion);
        entity.addPotionEffect(new PotionEffect(this.potion, this.ticks, this.amplifier));
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
    public boolean canActivateWith(Ability other) {
        return !(other instanceof PotionEffectAbility) || !((PotionEffectAbility) other).potion.equals(this.potion);
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

    public static class Generator implements AbilityGenerator {
        private static IntRange ticks = new IntRange(20 * 5, 20 * 30); //5-30 seconds!
        private static IntRange amplifier = new IntRange(1, 5);

        @Override
        public List<Ability> generate(Random random, AbilityStage stage, AbilityType type) {
            Potion potion;
            int ticks;
            int amplifier;

            if(type == AbilityType.ARMOR_PASSIVE) {
                ticks = 20 * 15;
            } else {
                ticks = Generator.ticks.randomElement(random);
            }

            Iterator<Potion> potionIterator = Potion.REGISTRY.iterator();
            List<Potion> potions = new ArrayList<>();
            potionIterator.forEachRemaining(potions::add);

            int index = random.nextInt(potions.size());

            do {
                potion = potions.get(index);
                index++;
                if(index >= potions.size()) {
                    index = 0;
                }
            } while (!goodFor(potion, type));

            if(potion.isInstant()) {
                ticks = 1;
            }

            amplifier = Generator.amplifier.randomElement(random);

            return Items.buildList(new PotionEffectAbility(potion, ticks, amplifier));
        }


        private static boolean goodFor(Potion potion, AbilityType type) {
            if(type == AbilityType.ARMOR_PASSIVE) {
                return !potion.isBadEffect() && !potion.isInstant();
            } else {
                return potion.isBadEffect();
            }
        }

        @Override
        public boolean applicableStage(AbilityStage stage) {
            return stage == AbilityStage.MIDDLE;
        }

        @Override
        public boolean applicableContext(AbilityType context) {
            return true;
        }

        @Override
        public boolean test(Random random) {
            return RandoresProbability.percentChance(40, random);
        }

        @Override
        public RandoresPlugin parent() {
            return Randores.INSTANCE;
        }

    }

}
