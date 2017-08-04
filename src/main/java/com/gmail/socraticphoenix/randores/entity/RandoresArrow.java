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
package com.gmail.socraticphoenix.randores.entity;

import com.gmail.socraticphoenix.randores.component.ability.AbilitySeries;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class RandoresArrow extends EntityTippedArrow {
    public UUID id;
    public int index;
    public boolean hasEffect;
    private boolean hadEffect;

    public RandoresArrow(World worldIn) {
        super(worldIn);
    }

    public RandoresArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.inGround && !this.hadEffect && this.hasEffect) {
            this.hadEffect = true;
            AbilitySeries series = this.get();
            if(series != null) {
                series.onProjectileHit((EntityLivingBase) this.shootingEntity, this.getPositionVector());
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        NBTTagCompound sub = new NBTTagCompound();
        sub.setUniqueId("id", this.id);
        sub.setInteger("index", this.index);
        sub.setBoolean("hadEffect", this.hadEffect);
        sub.setBoolean("hasEffect", this.hasEffect);
        compound.setTag("randores", sub);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        NBTTagCompound sub = compound.getCompoundTag("randores");
        this.id = sub.getUniqueId("id");
        this.index = sub.getInteger("index");
        this.hadEffect = sub.getBoolean("hadEffect");
        this.hasEffect = sub.getBoolean("hasEffect");
    }

    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);

        if(!this.hadEffect && this.hasEffect) {
            this.hadEffect = true;
            AbilitySeries series = this.get();
            if(series != null) {
                series.onProjectileHitEntity((EntityLivingBase) this.shootingEntity, living);
            }
        }
    }

    private AbilitySeries get() {
        return RandoresWorldData.delegate(new RandoresItemData(this.index, this.id), def -> def.getAbilitySeries(), () -> null);
    }

}
