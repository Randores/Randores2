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
package com.gmail.socraticphoenix.randores;

import com.gmail.socraticphoenix.randores.block.RandoresBlocks;
import com.gmail.socraticphoenix.randores.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import com.gmail.socraticphoenix.randores.item.RandoresItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public interface RandoresColors {

    IItemColor ITEM_COLOR = (stack, tintIndex) -> {
        if(tintIndex > 0 && RandoresItemData.hasData(stack)) {
            return getColor(new RandoresItemData(stack));
        } else {
            return -1;
        }
    };

    IBlockColor BLOCK_COLOR = (state, worldIn, pos, tintIndex) -> {
        if(tintIndex >= 1 && worldIn != null && pos != null) {
            TileEntity entity = worldIn.getTileEntity(pos);
            if(entity != null && entity instanceof RandoresTileEntity) {
                return getColor(((RandoresTileEntity) entity).getData());
            }
        }

        return -1;
    };

    static void registerItems() {
        List<Item> colors = new ArrayList<>();
        colors.addAll(RandoresItems.items);
        colors.addAll(RandoresBlocks.blockItems);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ITEM_COLOR, colors.toArray(new Item[colors.size()]));
    }

    static void registerBlocks() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(BLOCK_COLOR, RandoresBlocks.blocks.toArray(new Block[RandoresBlocks.blocks.size()]));
    }

    static int getColor(RandoresItemData data) {
        return RandoresWorldData.delegate(data, m -> m.getColor().getRGB(), Color.WHITE::getRGB);
    }

}
