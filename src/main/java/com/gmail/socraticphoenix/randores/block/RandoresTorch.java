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

import com.gmail.socraticphoenix.randores.IImplementableBlock;
import com.gmail.socraticphoenix.randores.IRandoresItem;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableTypeRegistry;
import com.gmail.socraticphoenix.randores.item.RandoresItemHelper;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class RandoresTorch extends BlockTorch implements IRandoresItem, IImplementableBlock {

    public RandoresTorch() {
        super();
        setSoundType(SoundType.WOOD);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return RandoresItemHelper.getPickBlockImpl(this, state, target, world, pos, player);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return RandoresItemHelper.getItemImpl(this, worldIn, pos, state);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        RandoresItemHelper.harvestBlockImpl(this, worldIn, player, pos, state, te, stack);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        RandoresItemHelper.dropBlockAsItemWithChanceImpl(this, worldIn, pos, state, chance, fortune);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune, TileEntity entity) {
        RandoresItemHelper.dropBlockAsItemWithChanceImpl(this, worldIn, pos, state, chance, fortune, entity);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return RandoresItemHelper.createTileEntityImpl(this, world, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        EnumFacing enumfacing = stateIn.getValue(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;

        if (enumfacing.getAxis().isHorizontal()) {
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (double) enumfacing1.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D);
        } else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }


    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public ComponentType type() {
        return ComponentType.craftable(CraftableTypeRegistry.instance().get(RandoresKeys.TORCH));
    }

    @Override
    public void setHarvester(EntityPlayer player) {
        harvesters.set(player);
    }

    @Override
    public EntityPlayer getHarvester() {
        return harvesters.get();
    }

}
