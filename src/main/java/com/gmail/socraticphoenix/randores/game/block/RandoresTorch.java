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
import com.gmail.socraticphoenix.randores.game.item.RandoresItemBlock;
import com.gmail.socraticphoenix.randores.game.item.RandoresItemHelper;
import com.gmail.socraticphoenix.randores.mod.component.ComponentType;
import com.gmail.socraticphoenix.randores.mod.component.CraftableType;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.mod.data.RandoresSeed;
import javax.annotation.Nullable;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RandoresTorch extends BlockTorch implements IRandoresItem {

    public RandoresTorch() {
        super();
        setSoundType(SoundType.WOOD);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        harvesters.set(player);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        this.dropBlockAsItemWithChance(worldIn, pos, state, te, 1f, i);
        harvesters.set(null);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, TileEntity entity, float chance, int fortune) {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
        {
            NonNullList<ItemStack> drops = NonNullList.create();
            RandoresItemHelper.getRawDrop(drops, entity, state);
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, worldIn, pos, state, fortune, chance, false, harvesters.get());

            for (ItemStack drop : drops) {
                if (worldIn.rand.nextFloat() <= chance) {
                    spawnAsEntity(worldIn, pos, drop);
                }
            }
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    protected RandoresItemData getData(IBlockAccess access, BlockPos pos) {
        TileEntity entity = access.getTileEntity(pos);
        if (entity != null && entity instanceof RandoresTileEntity) {
            return ((RandoresTileEntity) entity).getData();
        } else {
            return new RandoresItemData(0, 0);
        }
    }

    protected boolean hasData(IBlockAccess access, BlockPos pos) {
        TileEntity entity = access.getTileEntity(pos);
        return entity != null && entity instanceof RandoresTileEntity;
    }

    protected void delegateVoid(IBlockAccess access, BlockPos pos, Consumer<RandoresItemData> consumer, Runnable def) {
        if (this.hasData(access, pos)) {
            consumer.accept(this.getData(access, pos));
        } else {
            def.run();
        }
    }

    protected <T> T delegate(IBlockAccess access, BlockPos pos, Function<RandoresItemData, T> function, Supplier<T> def) {
        if (this.hasData(access, pos)) {
            return function.apply(this.getData(access, pos));
        } else {
            return def.get();
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new RandoresTileEntity(new RandoresItemData(0, RandoresSeed.getSeed(world)));
    }

    @Override
    public ComponentType type() {
        return ComponentType.craftable(CraftableType.TORCH);
    }

    @Override
    public Item getThis() {
        return RandoresItemBlock.ITEM_BY_BLOCK.get(this);
    }

}
