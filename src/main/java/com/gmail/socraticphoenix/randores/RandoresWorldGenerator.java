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

import com.gmail.socraticphoenix.randores.block.RandoresOre;
import com.gmail.socraticphoenix.randores.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.component.OreComponent;
import com.gmail.socraticphoenix.randores.component.enumerable.OreTypeRegistry;
import com.gmail.socraticphoenix.randores.config.RandoresConfig;
import com.gmail.socraticphoenix.randores.config.RandoresModules;
import com.gmail.socraticphoenix.randores.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import com.gmail.socraticphoenix.randores.probability.IntRange;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;

/**
 * The IWorldGenerator that populates the world with {@link IRandoresOre}s.
 */
public class RandoresWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!world.isRemote) {
            RandoresConfig config = Randores.getConfigObj();
            RandoresModules modules = config.getModules();

            boolean flag = modules.isDimensionLess();
            boolean flag2 = modules.isYoutubeMode();
            int ores = config.getMaxOres();

            if (RandoresWorldData.hasData(world)) {
                RandoresWorldData data = RandoresWorldData.getSimply(world);
                for (OreType oreType : OreTypeRegistry.instance().values()) {
                    if (flag || oreType.getIsDimension().apply(world)) {
                        List<MaterialDefinition> target = data.getAndShuffleDimensionCache(oreType, random);
                        if (!target.isEmpty()) {
                            int count = Math.min(ores, target.size()) * (flag2 ? 2 : 1);
                            for (int i = 0; i < count; i++) {
                                MaterialDefinition element = target.get(i);
                                OreComponent component = element.getOre();
                                this.generateOre(component.getBlock().getDefaultState().withProperty(RandoresOre.HARVEST_LEVEL, component.getHarvestLevel()), element.getData(), world, random, chunkX * 16, chunkZ * 16, component.getMaxVein(), component.getMinVein(), component.getMaxY(), component.getMinY(), component.getMaxOccurrences(), component.getMinOccurrences(), component.getOreType().getGenerateIn());
                            }
                        }
                    }
                }
            }

            if (flag || DimensionType.OVERWORLD.getId() == world.provider.getDimension()) {
                this.generateOre(CraftingBlocks.craftiniumOre.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 6, 2, world.getHeight(), 0, 100, 50, new Block[] {Blocks.STONE});
            }
        }
    }

    private void generateOre(IBlockState block, World world, Random random, int blockX, int blockZ, int maxVein, int minVein, int maxY, int minY, int maxOccurrences, int minOccurrences, Block[] generateIn) {
        IntRange vein = new IntRange(minVein, maxVein);
        IntRange height = new IntRange(minY, maxY);
        IntRange occurrences = new IntRange(minOccurrences, maxOccurrences);

        Predicate<IBlockState> predicate;
        if (generateIn.length == 0) {
            predicate = input -> true;
        } else {
            predicate = BlockMatcher.forBlock(generateIn[0]);
            if (generateIn.length > 1) {
                for (int i = 1; i < generateIn.length; i++) {
                    predicate = this.or(predicate, BlockMatcher.forBlock(generateIn[i]));
                }
            }
        }

        WorldGenMinable gen = new WorldGenMinable(block, vein.randomElement(random), predicate);

        int i = 0;
        int o = occurrences.randomElement(random);
        while (i < o) {
            int x = blockX + random.nextInt(16);
            int y = height.randomElement(random);
            int z = blockZ + random.nextInt(16);
            gen.generate(world, random, new BlockPos(x, y, z));
            i++;
        }
    }


    private void generateOre(IBlockState block, RandoresItemData data, World world, Random random, int blockX, int blockZ, int maxVein, int minVein, int maxY, int minY, int maxOccurrences, int minOccurrences, Predicate<IBlockState> generateIn) {
        IntRange vein = new IntRange(minVein, maxVein);
        IntRange height = new IntRange(minY, maxY);
        IntRange occurrences = new IntRange(minOccurrences, maxOccurrences);

        int i = 0;
        int o = occurrences.randomElement(random);
        while (i < o) {
            int x = blockX + random.nextInt(16);
            int y = height.randomElement(random);
            int z = blockZ + random.nextInt(16);
            generate(world, random, new BlockPos(x, y, z), block, vein.randomElement(), generateIn, data);
            i++;
        }
    }

    public boolean generate(World worldIn, Random rand, BlockPos position, IBlockState oreBlock, int numberOfBlocks, Predicate<IBlockState> predicate, RandoresItemData data) {
        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (double) ((float) (position.getX() + 8) + MathHelper.sin(f) * (float) numberOfBlocks / 8.0F);
        double d1 = (double) ((float) (position.getX() + 8) - MathHelper.sin(f) * (float) numberOfBlocks / 8.0F);
        double d2 = (double) ((float) (position.getZ() + 8) + MathHelper.cos(f) * (float) numberOfBlocks / 8.0F);
        double d3 = (double) ((float) (position.getZ() + 8) - MathHelper.cos(f) * (float) numberOfBlocks / 8.0F);
        double d4 = (double) (position.getY() + rand.nextInt(3) - 2);
        double d5 = (double) (position.getY() + rand.nextInt(3) - 2);

        for (int i = 0; i < numberOfBlocks; ++i) {
            float f1 = (float) i / (float) numberOfBlocks;
            double d6 = d0 + (d1 - d0) * (double) f1;
            double d7 = d4 + (d5 - d4) * (double) f1;
            double d8 = d2 + (d3 - d2) * (double) f1;
            double d9 = rand.nextDouble() * (double) numberOfBlocks / 16.0D;
            double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1) {
                double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for (int j2 = l; j2 <= k1; ++j2) {
                                double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                                    BlockPos blockpos = new BlockPos(l1, i2, j2);

                                    IBlockState state = worldIn.getBlockState(blockpos);
                                    if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, predicate)) {
                                        if (worldIn.setBlockState(blockpos, oreBlock, 2)) {
                                            TileEntity entity = worldIn.getTileEntity(blockpos);
                                            if (entity != null && entity instanceof RandoresTileEntity) {
                                                ((RandoresTileEntity) entity).setDataUnsafe(data.getIndex(), data.getId());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private <T> Predicate<T> or(final Predicate<T> a, final Predicate<T> b) {
        return input -> a.apply(input) || b.apply(input);
    }

}
