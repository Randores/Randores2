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
import com.gmail.socraticphoenix.randores.game.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.mod.component.ComponentType;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class RandoresItemBlock extends ItemBlock implements IRandoresItem {
    public static final Map<Block, RandoresItemBlock> ITEM_BY_BLOCK = new LinkedHashMap<>();

    private ComponentType type;

    public <T extends Block & IRandoresItem> RandoresItemBlock(T block) {
        super(block);
        this.type = block.type();
        ITEM_BY_BLOCK.put(block, this);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if(RandoresItemData.hasData(stack)) {
            return MaterialDefinitionRegistry.delegate(new RandoresItemData(stack), m -> m.formatLocalName(this.type.from(m)), () -> super.getItemStackDisplayName(stack));
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        boolean success = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

        if(success) {
            TileEntity entity = world.getTileEntity(pos);
            if(entity != null && entity instanceof RandoresTileEntity && RandoresItemData.hasData(stack)) {
                ((RandoresTileEntity) entity).setData(new RandoresItemData(stack));
            }
        }

        return success;
    }

    @Override
    public ComponentType type() {
        return this.type;
    }

}
