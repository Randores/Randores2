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
package com.gmail.socraticphoenix.randores.block;

import com.gmail.socraticphoenix.randores.IRandoresOre;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import com.gmail.socraticphoenix.randores.item.RandoresItemHelper;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

public class RandoresOre extends RandoresBlock implements IRandoresOre {
    public static final PropertyInteger HARVEST_LEVEL = PropertyInteger.create("harvest_level", 1, 4);
    private Random random = new Random();
    private OreType oreType;
    private MaterialType materialType;

    public RandoresOre(Material material, OreType oreType, MaterialType materialType) {
        super(material, ComponentType.ore(), SoundType.STONE);
        this.oreType = oreType;
        this.materialType = materialType;
    }

    public OreType getOreType() {
        return this.oreType;
    }

    public MaterialType getMaterialType() {
        return this.materialType;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return RandoresItemHelper.getStateForOrePlacementImpl(this, world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        RandoresItemHelper.harvestOreBlockImpl(this, worldIn, player, pos, state, te, stack);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return RandoresItemHelper.getOreStateFromMetaImpl(this, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return RandoresItemHelper.getMetaFromOreStateImpl(this, state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return RandoresItemHelper.createOreBlockStateImpl(this);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return RandoresItemHelper.getOreExplosionResistanceImpl(this, world, pos, exploder, explosion);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return RandoresItemHelper.getOreBlockHardnessImpl(this, blockState, worldIn, pos);
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return RandoresItemHelper.getOreHarvestLevelImpl(this, state);
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

}
