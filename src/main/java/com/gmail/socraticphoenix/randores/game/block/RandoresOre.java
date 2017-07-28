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
package com.gmail.socraticphoenix.randores.game.block;

import com.gmail.socraticphoenix.randores.game.IRandoresItem;
import com.gmail.socraticphoenix.randores.mod.component.ComponentType;
import com.gmail.socraticphoenix.randores.mod.component.Dimension;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.component.MaterialType;
import com.gmail.socraticphoenix.randores.mod.component.OreComponent;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandoresOre extends RandoresBlock implements IRandoresItem {
    public static final PropertyInteger HARVEST_LEVEL = PropertyInteger.create("harvest_level", 0, 15);
    private Random random = new Random();
    private Dimension dimension;
    private MaterialType materialType;

    public RandoresOre(Material material, Dimension dimension, MaterialType materialType) {
        super(material, ComponentType.ore(), SoundType.STONE);
        this.dimension = dimension;
        this.materialType = materialType;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public MaterialType getMaterialType() {
        return this.materialType;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        if (te != null && te instanceof RandoresTileEntity) {
            RandoresItemData data = ((RandoresTileEntity) te).getData();
            MaterialDefinitionRegistry.delegateVoid(data, def -> {
                boolean flag = this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
                if (flag || def.getOre().isRequiresSmelting()) {
                    java.util.List<ItemStack> items = new java.util.ArrayList<>();
                    items.add(def.getOre().createStack(data));
                    net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, flag, player);
                    for (ItemStack item : items) {
                        spawnAsEntity(worldIn, pos, item);
                    }
                } else {
                    harvesters.set(player);
                    int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
                    OreComponent component = def.getOre();
                    int max = component.getMaxDrops();
                    int min = component.getMinDrops();
                    int amount = this.random.nextInt((max - min) + 1) + min;
                    if (fortune > 0) {
                        int i = this.random.nextInt(fortune + 2) - 1;
                        if (i < 0) {
                            i = 0;
                        }
                        amount = amount * (i + 1);
                    }
                    ItemStack res = new ItemStack(def.getMaterial().item());
                    List<ItemStack> items = new ArrayList<>();
                    data.applyTo(res);
                    for (int i = 0; i < amount; i++) {
                        items.add(res.copy());
                    }
                    float chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, false, player);
                    for (ItemStack item : items) {
                        if (worldIn.rand.nextFloat() <= chance) {
                            spawnAsEntity(worldIn, pos, item);
                        }
                    }
                    harvesters.set(null);
                }
            }, () -> {
            });
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HARVEST_LEVEL, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HARVEST_LEVEL);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HARVEST_LEVEL);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return this.delegate(world, pos, d -> MaterialDefinitionRegistry.delegate(d, m -> m.getOre().getResistance(), () -> 0f), () -> 0f);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return this.delegate(worldIn, pos, d -> MaterialDefinitionRegistry.delegate(d, m -> m.getOre().getHardness(), () -> 1f), () -> 1f);
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return state.getValue(HARVEST_LEVEL);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

}
