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
package com.gmail.socraticphoenix.randores.item;

import com.gmail.socraticphoenix.randores.IImplementableBlock;
import com.gmail.socraticphoenix.randores.IRandoresItem;
import com.gmail.socraticphoenix.randores.block.RandoresOre;
import com.gmail.socraticphoenix.randores.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.component.Component;
import com.gmail.socraticphoenix.randores.component.ComponentType;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.component.OreComponent;
import com.gmail.socraticphoenix.randores.component.ability.EmpoweredEnchantment;
import com.gmail.socraticphoenix.randores.component.craftable.CraftableComponent;
import com.gmail.socraticphoenix.randores.data.RandoresItemData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RandoresItemHelper {
    private static Random random = new Random();

    public static <T extends Item & IRandoresItem> String getItemStackDisplayNameImpl(T self, ItemStack stack) {
        if(RandoresItemData.hasData(stack)) {
            return RandoresWorldData.delegate(new RandoresItemData(stack), m -> m.formatLocalName(self.type().from(m)), () -> self.getUnlocalizedName());
        }
        return self.getUnlocalizedName();
    }

    public static int getMaxDamageImpl(Item self, ItemStack stack) {
        if(RandoresItemData.hasData(stack)) {
            return RandoresWorldData.delegate(new RandoresItemData(stack), m -> m.getMaterial().getMaxUses(), () -> 100);
        }

        return 100;
    }

    public static boolean getIsRepairableImpl(Item self, ItemStack toRepair, ItemStack repair) {
        if(RandoresItemData.hasData(toRepair)) {
            return RandoresWorldData.delegate(new RandoresItemData(toRepair), m -> {
                if(m.getMaterial().item() == repair.getItem() && RandoresItemData.hasData(repair)) {
                    RandoresItemData data = new RandoresItemData(repair);
                    return RandoresWorldData.delegate(data, m2 -> m2.getMaterial() == m.getMaterial(), () -> false);
                }

                return false;
            }, () -> false);
        }

        return false;
    }

    public static ItemStack getPickBlockImpl(Block self, IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return RandoresItemHelper.getRawDrop(world.getTileEntity(pos), state);
    }

    public static ItemStack getItemImpl(Block self, World world, BlockPos pos, IBlockState state) {
        return RandoresItemHelper.getRawDrop(world.getTileEntity(pos), state);
    }

    public static  <T extends Block & IImplementableBlock> void dropBlockAsItemWithChanceImpl(T self, World world, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!world.isRemote && !world.restoringBlockSnapshots) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity != null && entity instanceof RandoresTileEntity) {
                NonNullList<ItemStack> drops = NonNullList.create();
                RandoresItemHelper.getRawDrop(drops, entity, state);
                chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, fortune, chance, false, self.getHarvester());

                for (ItemStack drop : drops) {
                    if (world.rand.nextFloat() <= chance) {
                        Block.spawnAsEntity(world, pos, drop);
                    }
                }
            }
        }
    }

    public static TileEntity createTileEntityImpl(Block self, World world, IBlockState state) {
        return new RandoresTileEntity(new RandoresItemData(0, RandoresWorldData.getId(world)));
    }

    public static <T extends Block & IImplementableBlock> void harvestBlockImpl(T self, World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(self));
        player.addExhaustion(0.005F);

        self.setHarvester(player);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        self.dropBlockAsItemWithChance(worldIn, pos, state, 1f, i);
        self.setHarvester(null);
    }

    public static IBlockState getStateForOrePlacementImpl(Block self, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        if (RandoresItemData.hasData(stack)) {
            return RandoresWorldData.delegate(new RandoresItemData(stack), def -> self.getDefaultState().withProperty(RandoresOre.HARVEST_LEVEL, def.getOre().getHarvestLevel()), self::getDefaultState);
        }
        return self.getDefaultState();
    }

    public static <T extends Block & IImplementableBlock> void harvestOreBlockImpl(T self, World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(self));
        player.addExhaustion(0.005F);

        if (te != null && te instanceof RandoresTileEntity) {
            RandoresItemData data = ((RandoresTileEntity) te).getData();
            RandoresWorldData.delegateVoid(data, def -> {
                boolean flag = self.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
                if (flag || def.getOre().isRequiresSmelting()) {
                    java.util.List<ItemStack> items = new java.util.ArrayList<>();
                    items.add(def.getOre().createStack(data));
                    net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, flag, player);
                    for (ItemStack item : items) {
                        Block.spawnAsEntity(worldIn, pos, item);
                    }
                } else {
                    self.setHarvester(player);
                    int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
                    OreComponent component = def.getOre();
                    int max = component.getMaxDrops();
                    int min = component.getMinDrops();
                    int amount = random.nextInt((max - min) + 1) + min;
                    if (fortune > 0) {
                        int i = random.nextInt(fortune + 2) - 1;
                        if (i < 0) {
                            i = 0;
                        }
                        amount = amount * (i + 1);
                    }
                    amount = amount == 0 ? 1 : amount;

                    ItemStack res = new ItemStack(def.getMaterial().item());
                    List<ItemStack> items = new ArrayList<>();
                    data.applyTo(res);
                    for (int i = 0; i < amount; i++) {
                        items.add(res.copy());
                    }
                    float chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, false, player);
                    for (ItemStack item : items) {
                        if (worldIn.rand.nextFloat() <= chance) {
                            self.spawnAsEntity(worldIn, pos, item);
                        }
                    }
                    self.setHarvester(null);
                }
            }, () -> {
            });
        }
    }

    public static IBlockState getOreStateFromMetaImpl(Block self, int meta) {
        return self.getDefaultState().withProperty(RandoresOre.HARVEST_LEVEL, meta);
    }

    public static int getMetaFromOreStateImpl(Block self, IBlockState state) {
        return state.getValue(RandoresOre.HARVEST_LEVEL);
    }

    public static BlockStateContainer createOreBlockStateImpl(Block self) {
        return new BlockStateContainer(self, RandoresOre.HARVEST_LEVEL);
    }

    public static float getOreExplosionResistanceImpl(Block self, World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return RandoresItemHelper.delegate(world, pos, def -> def.getOre().getHardness(), () -> 0f);
    }

    public static float getOreBlockHardnessImpl(Block self, IBlockState blockState, World world, BlockPos pos) {
        return RandoresItemHelper.delegate(world, pos, def -> def.getOre().getHardness(), () -> 1f);
    }

    public static int getOreHarvestLevelImpl(Block self, IBlockState state) {
        return state.getValue(RandoresOre.HARVEST_LEVEL);
    }

    public static <T> T delegate(IBlockAccess access, BlockPos pos, Function<MaterialDefinition, T> ifPresent, Supplier<T> ifAbsent) {
        if(RandoresItemData.hasData(access, pos)) {
            return RandoresWorldData.delegate(RandoresItemData.getData(access, pos), ifPresent, ifAbsent);
        } else {
            return ifAbsent.get();
        }
    }

    public static void delegateVoid(IBlockAccess access, BlockPos pos, Consumer<MaterialDefinition> ifPresent, Runnable ifAbsent) {
        if(RandoresItemData.hasData(access, pos)) {
            RandoresWorldData.delegateVoid(RandoresItemData.getData(access, pos), ifPresent, ifAbsent);
        } else {
            ifAbsent.run();
        }
    }

    public static void doEmpowered(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (stack.getItem() instanceof IRandoresItem && EmpoweredEnchantment.appliedTo(stack) && RandoresItemData.hasData(stack)) {
            IRandoresItem item = (IRandoresItem) stack.getItem();
            ComponentType type = item.type();
            RandoresWorldData.delegateVoid(new RandoresItemData(stack), m -> {
                if (type.has(m)) {
                    Component component = type.from(m);
                    if (component instanceof CraftableComponent) {
                        CraftableComponent craftableComponent = (CraftableComponent) component;
                        if (craftableComponent.getType().isTool() || craftableComponent.getType().isDamage()) {
                            m.getAbilitySeries().onMeleeHit(attacker, target);
                        }
                    }
                }
            }, () -> {
            });
        }
    }

    public static ItemStack getRawDrop(TileEntity entity, IBlockState state) {
        if (entity != null && entity instanceof RandoresTileEntity) {
            RandoresItemData data = ((RandoresTileEntity) entity).getData();
            if (Item.getItemFromBlock(state.getBlock()) != Items.AIR) {
                Item itemBlock = Item.getItemFromBlock(state.getBlock());
                ItemStack stack = new ItemStack(itemBlock);
                data.applyTo(stack);
                return stack;
            }
        }

        return new ItemStack(state.getBlock());
    }

    public static void getRawDrop(NonNullList<ItemStack> drops, TileEntity entity, IBlockState state) {
        if (entity != null && entity instanceof RandoresTileEntity) {
            RandoresItemData data = ((RandoresTileEntity) entity).getData();
            if (Item.getItemFromBlock(state.getBlock()) != Items.AIR) {
                Item itemBlock = Item.getItemFromBlock(state.getBlock());
                ItemStack stack = new ItemStack(itemBlock);
                data.applyTo(stack);
                drops.add(stack);
            }
        }
    }

}
