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
package com.gmail.socraticphoenix.randores.module.altar;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.block.RandoresBlocks;
import com.gmail.socraticphoenix.randores.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.component.Component;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import com.gmail.socraticphoenix.randores.probability.IntRange;
import com.gmail.socraticphoenix.randores.probability.RandoresProbability;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandoresAltarGenerator implements IWorldGenerator {
    private static IntRange levels = new IntRange(10, 20);

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!world.isRemote && world.provider.getDimension() == DimensionType.OVERWORLD.getId()) {
            if (Randores.getConfigObj().getModules().isAltar() && RandoresProbability.percentChance(Randores.getConfigObj().getModules().isYoutubeMode() ? 10 : 0.5, random)) {
                List<MaterialDefinition> definitions = RandoresWorldData.getAll(RandoresWorldData.getId(world));
                if (!definitions.isEmpty()) {
                    MaterialDefinition definition;
                    int tries = 0;
                    do {
                        definition = definitions.get(random.nextInt(definitions.size()));
                        tries++;
                    } while (!ComponentType.craftable(RandoresKeys.BRICKS).has(definition) && tries < 500);


                    if (ComponentType.craftable(RandoresKeys.BRICKS).has(definition)) {
                        int blockX = chunkX * 16 + 8;
                        int blockZ = chunkZ * 16 + 8;
                        blockX = random.nextBoolean() ? blockX - random.nextInt(5) : blockX + random.nextInt(5);
                        blockZ = random.nextBoolean() ? blockZ - random.nextInt(5) : blockZ + random.nextInt(5);
                        int blockY = world.getHeight();

                        boolean valid = false;
                        for (int i = blockY; i >= 0; i--) {
                            BlockPos pos = new BlockPos(blockX, i, blockZ);
                            if (world.getBlockState(pos).getBlock() != Blocks.AIR && !world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                                blockY = i + 1;
                                if (world.getHeight() - 10 > blockY && world.getBlockState(pos).isOpaqueCube() && !(world.getBlockState(pos).getMaterial() instanceof MaterialLiquid) && !world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos)) {
                                    int numBelow = 0;
                                    BlockPos down = pos;
                                    while (this.hasAir(world, down, 2, 2)) {
                                        numBelow++;
                                        down = down.add(0, -1, 0);
                                    }
                                    valid = numBelow < 10;
                                }
                                break;
                            }
                        }

                        if (valid) {
                            RandoresItemData data = definition.getData();

                            BlockPos center = new BlockPos(blockX, blockY, blockZ);
                            IBlockState state = RandoresBlocks.brick.getDefaultState();
                            this.clear(world, center, 2, 2, random, true);
                            this.fill(world, center, state, data, 2, 2, random, false);
                            this.setBrick(world, center, state, data);
                            for (int i = 1; i <= 4; i++) {
                                this.clear(world, center.add(0, i, 0), 2, 2, random, false);
                                this.fillPerimeter(world, center.add(0, i, 0), state, data, 2, 2, random);
                            }

                            BlockPos down = center.add(0, -1, 0);
                            while (this.hasAir(world, down, 2, 2)) {
                                this.fill(world, down, state, data, 2, 2, random, true);
                                down = down.add(0, -1, 0);
                            }

                            BlockPos valuables = center.add(0, 1, 0);
                            world.setBlockState(valuables, Blocks.CHEST.getDefaultState());
                            TileEntityChest chest = new TileEntityChest(BlockChest.Type.BASIC);
                            for (int i = 0; i < chest.getSizeInventory(); i++) {
                                if (RandoresProbability.percentChance(30, random)) {
                                    List<Component> components = definition.getComponents().stream().filter(c -> !ComponentType.ore().is(c)).collect(Collectors.toList());
                                    Component component = components.get(random.nextInt(components.size()));
                                    int size = component.quantity() == 1 ? 1 : random.nextInt(4) + component.quantity();
                                    ItemStack stack = component.createStack(definition.getData());
                                    stack.setCount(size);
                                    if (RandoresProbability.percentChance(10, random)) {
                                        stack = EnchantmentHelper.addRandomEnchantment(random, stack, levels.randomElement(random), true);
                                    }
                                    chest.setInventorySlotContents(i, stack);
                                }
                            }
                            world.setTileEntity(valuables, chest);
                        }
                    }
                }
            }
        }
    }

    private void setBrick(World world, BlockPos pos, IBlockState state, RandoresItemData data) {
        world.setBlockState(pos, state);
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof RandoresTileEntity) {
            ((RandoresTileEntity) te).setData(data);
        }
    }

    private boolean hasAir(World world, BlockPos pos, int width, int length) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                if (world.getBlockState(loc).getBlock() == Blocks.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    private void fillPerimeter(World world, BlockPos pos, IBlockState state, RandoresItemData data, int width, int length, Random random) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                if (RandoresProbability.percentChance(70, random) && (x == minWidth || x == maxWidth || z == minLength || z == maxLength)) {
                    this.setBrick(world, loc, state, data);
                }
            }
        }
    }

    private void fill(World world, BlockPos pos, IBlockState state, RandoresItemData data, int width, int length, Random random, boolean override) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                if (state == null) {
                    world.setBlockToAir(loc);
                } else {
                    if (override || RandoresProbability.percentChance(70, random)) {
                        this.setBrick(world, loc, state, data);
                    }
                }
            }
        }
    }

    private void clear(World world, BlockPos pos, int width, int length, Random random, boolean override) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                world.setBlockToAir(loc);
            }
        }
    }


}
