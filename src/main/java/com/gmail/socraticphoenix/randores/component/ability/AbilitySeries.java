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
package com.gmail.socraticphoenix.randores.component.ability;

import com.gmail.socraticphoenix.jlsc.serialization.annotation.Name;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serialize;
import com.gmail.socraticphoenix.randores.listener.ScheduleListener;
import com.google.common.base.Supplier;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.List;

@Serializable
public class AbilitySeries {
    @Serialize(value = "armorPassive", reflect = false)
    private List<Ability> armorPassive;
    @Serialize(value = "armorActive", reflect = false)
    private List<Ability> armorActive;
    @Serialize(value = "projectile", reflect = false)
    private List<Ability> projectile;
    @Serialize(value = "melee", reflect = false)
    private List<Ability> melee;

    @SerializationConstructor
    public AbilitySeries(@Name("armorPassive") List<Ability> armorPassive, @Name("armorActive") List<Ability> armorActive, @Name("melee") List<Ability> melee, @Name("projectile") List<Ability> projectile) {
        this.armorActive = armorActive;
        this.armorPassive = armorPassive;
        this.projectile = projectile;
        this.melee = melee;
    }

    public List<Ability> getArmorPassive() {
        return this.armorPassive;
    }

    public List<Ability> getArmorActive() {
        return this.armorActive;
    }

    public List<Ability> getProjectile() {
        return this.projectile;
    }

    public List<Ability> getMelee() {
        return this.melee;
    }

    public void onArmorUpdate(EntityLivingBase entity) {
        for (Ability ability : this.armorPassive) {
            ability.apply(entity.getPositionVector(), entity, new AbilityContext(AbilityType.ARMOR_PASSIVE));
        }
    }

    public void onArmorHit(EntityLivingBase entity, final EntityLivingBase source) {
        if (!this.armorActive.isEmpty()) {
            AbilityContext context = new AbilityContext(source, entity, AbilityType.ARMOR_ACTIVE);
            RunNextAbility nextAbility = new RunNextAbility(this.armorActive, 0, entity, context, source::getPositionVector);
            nextAbility.run();
        }
    }

    public void onProjectileHitEntity(EntityLivingBase attacker, final EntityLivingBase target) {
        if (!this.projectile.isEmpty()) {
            AbilityContext context = new AbilityContext(attacker, target, AbilityType.PROJECTILE);
            RunNextAbility nextAbility = new RunNextAbility(this.projectile, 0, attacker, context, target::getPositionVector);
            nextAbility.run();
        }
    }

    public void onProjectileHit(EntityLivingBase attacker, final Vec3d location) {
        if (!this.projectile.isEmpty()) {
            AbilityContext context = new AbilityContext(attacker, null, AbilityType.PROJECTILE);
            RunNextAbility nextAbility = new RunNextAbility(this.projectile, 0, attacker, context, () -> location);
            nextAbility.run();
        }
    }

    public void onMeleeHit(EntityLivingBase attacker, final EntityLivingBase target) {
        if (!this.melee.isEmpty()) {
            AbilityContext context = new AbilityContext(attacker, target, AbilityType.MELEE);
            RunNextAbility nextAbility = new RunNextAbility(this.melee, 0, attacker, context, target::getPositionVector);
            nextAbility.run();
        }
    }

    public List<Ability> getSeries(AbilityType type) {
        switch (type) {
            case ARMOR_PASSIVE:
                return this.armorPassive;
            case ARMOR_ACTIVE:
                return this.armorActive;
            case PROJECTILE:
                return this.projectile;
            case MELEE:
                return this.melee;
            default:
                throw new IllegalArgumentException("Unreachable code");
        }
    }

    public void onArmorCancel(EntityLivingBase entity) {
        for (Ability ability : this.armorPassive) {
            ability.remove(entity.getPositionVector(), entity, new AbilityContext(AbilityType.ARMOR_PASSIVE));
        }
    }

    static class RunNextAbility implements Runnable {
        private List<Ability> abilities;
        private int index;

        private EntityLivingBase activator;
        private AbilityContext context;
        private Supplier<Vec3d> location;

        public RunNextAbility(List<Ability> abilities, int index, EntityLivingBase activator, AbilityContext context, Supplier<Vec3d> location) {
            this.abilities = abilities;
            this.index = index;
            this.activator = activator;
            this.context = context;
            this.location = location;
        }

        @Override
        public void run() {
            Ability ability = this.abilities.get(this.index);
            boolean flag = ability.apply(this.location.get(), this.activator, this.context);

            int next = this.index + 1;
            if (next < abilities.size() && flag) {
                RunNextAbility nextAbility = new RunNextAbility(this.abilities, next, this.activator, this.context, this.location);
                if (abilities.get(next).delayAfter() == 0) {
                    nextAbility.run();
                } else {
                    ScheduleListener.schedule(nextAbility, ability.delayAfter());
                }
            }
        }

    }

}
