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
import com.gmail.socraticphoenix.randores.mod.component.ComponentType;
import com.gmail.socraticphoenix.randores.mod.component.CraftableType;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresItemData;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RandoresItemArmor extends ItemArmor implements ISpecialArmor, IRandoresItem {
    private Map<RandoresItemData, ItemArmor> handlers = new HashMap<>();
    private ComponentType type;

    public RandoresItemArmor(ComponentType type, EntityEquipmentSlot equipmentSlotIn) {
        super(ARMOR_DEFAULT, getRenderIndex(equipmentSlotIn), equipmentSlotIn);
        this.type = type;
    }

    public void registerBacker(RandoresItemData data, ArmorMaterial material) {
        this.handlers.put(data, new ItemArmor(material, this.renderIndex, this.getEquipmentSlot()));
    }

    public void removeBacker(RandoresItemData data) {
        this.handlers.remove(data);
    }

    public RandoresItemArmor(CraftableType type, EntityEquipmentSlot slot) {
        this(ComponentType.craftable(type), slot);
    }

    private static int getRenderIndex(EntityEquipmentSlot slot) {
        if (slot == EntityEquipmentSlot.LEGS) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        return this.delegate(armor, i -> new ArmorProperties(0, i.damageReduceAmount / 25d, Integer.MAX_VALUE), () -> new ArmorProperties(0, 1, Integer.MAX_VALUE));
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return this.delegate(armor, i -> i.damageReduceAmount, () -> 0);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        this.setDamage(stack, this.getDamage(stack) + damage);
        if (this.getDamage(stack) >= this.getMaxDamage(stack)) {
            stack.shrink(1);
        }
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        return super.hasColor(stack);
    }

    @Override
    public int getColor(ItemStack stack) {
        if(RandoresItemData.hasData(stack)) {
            return MaterialDefinitionRegistry.delegate(new RandoresItemData(stack), m -> m.getColor().getRGB(), () -> Color.WHITE.getRGB());
        }
        return Color.WHITE.getRGB();
    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return RandoresItemData.hasData(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if(RandoresItemData.hasData(stack)) {
            return MaterialDefinitionRegistry.delegate(new RandoresItemData(stack), m -> m.formatLocalName(this.type.from(m)), () -> super.getItemStackDisplayName(stack));
        }
        return super.getItemStackDisplayName(stack);
    }

    protected void delegateVoid(RandoresItemData data, Consumer<ItemArmor> action, Runnable def) {
        ItemArmor handler = this.handlers.get(data);
        if (handler != null) {
            action.accept(handler);
        } else {
            def.run();
        }
    }

    protected void delegateVoid(ItemStack stack, Consumer<ItemArmor> action, Runnable def) {
        if (stack != null) {
            this.delegateVoid(new RandoresItemData(stack), action, def);
        } else {
            def.run();
        }
    }

    protected <T> T delegate(RandoresItemData data, Function<ItemArmor, T> action, Supplier<T> def) {
        ItemArmor handler = this.handlers.get(data);
        if (handler != null) {
            return action.apply(handler);
        } else {
            return def.get();
        }
    }

    protected <T> T delegate(ItemStack stack, Function<ItemArmor, T> action, Supplier<T> def) {
        if (stack != null) {
            return this.delegate(new RandoresItemData(stack), action, def);
        } else {
            return def.get();
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.delegate(player.getHeldItem(hand), i -> i.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ), () -> super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        return this.delegate(stack, i -> i.getStrVsBlock(stack, state), () -> super.getStrVsBlock(stack, state));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return this.delegate(playerIn.getHeldItem(handIn), i -> i.onItemRightClick(worldIn, playerIn, handIn), () -> super.onItemRightClick(worldIn, playerIn, handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return this.delegate(stack, i -> i.onItemUseFinish(stack, worldIn, entityLiving), () -> super.onItemUseFinish(stack, worldIn, entityLiving));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return this.delegate(stack, i -> i.hitEntity(stack, target, attacker), () -> super.hitEntity(stack, target, attacker));
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return this.delegate(stack, i -> i.onBlockDestroyed(stack, worldIn, state, pos, entityLiving), () -> super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        return this.delegate(stack, i -> i.itemInteractionForEntity(stack, playerIn, target, hand), () -> super.itemInteractionForEntity(stack, playerIn, target, hand));
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        this.delegateVoid(stack, i -> i.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected), () -> super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected));
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        this.delegateVoid(stack, i -> i.onCreated(stack, worldIn, playerIn), () -> super.onCreated(stack, worldIn, playerIn));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return this.delegate(stack, i -> i.getItemUseAction(stack), () -> super.getItemUseAction(stack));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return this.delegate(stack, i -> i.getMaxItemUseDuration(stack), () -> super.getMaxItemUseDuration(stack));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        this.delegateVoid(stack, i -> i.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft), () -> super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.delegate(stack, i -> i.hasEffect(stack), () -> super.hasEffect(stack));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return this.delegate(stack, i -> i.getRarity(stack), () -> super.getRarity(stack));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.delegate(stack, i -> i.isEnchantable(stack), () -> super.isEnchantable(stack));
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this.delegate(toRepair, i -> i.getIsRepairable(toRepair, repair), () -> super.getIsRepairable(toRepair, repair));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return this.delegate(stack, i -> i.getAttributeModifiers(slot, stack), () -> super.getAttributeModifiers(slot, stack));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
        return this.delegate(item, i -> i.onDroppedByPlayer(item, player), () -> super.onDroppedByPlayer(item, player));
    }

    @Override
    public String getHighlightTip(ItemStack item, String displayName) {
        return this.delegate(item, i -> i.getHighlightTip(item, displayName), () -> super.getHighlightTip(item, displayName));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return this.delegate(player.getHeldItem(hand), i -> i.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand), () -> super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand));
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        return this.delegate(stack, i -> i.getNBTShareTag(stack), () -> super.getNBTShareTag(stack));
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return this.delegate(itemstack, i -> i.onBlockStartBreak(itemstack, pos, player), () -> super.onBlockStartBreak(itemstack, pos, player));
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        this.delegateVoid(stack, i -> i.onUsingTick(stack, player, count), () -> super.onUsingTick(stack, player, count));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return this.delegate(stack, i -> i.onLeftClickEntity(stack, player, entity), () -> super.onLeftClickEntity(stack, player, entity));
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return this.delegate(itemStack, i -> i.getContainerItem(itemStack), () -> super.getContainerItem(itemStack));
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return this.delegate(itemStack, i -> i.hasContainerItem(itemStack), () -> super.hasContainerItem(itemStack));
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return this.delegate(itemStack, i -> i.getEntityLifespan(itemStack, world), () -> super.getEntityLifespan(itemStack, world));
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return this.delegate(stack, i -> i.hasCustomEntity(stack), () -> super.hasCustomEntity(stack));
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return this.delegate(itemstack, i -> i.createEntity(world, location, itemstack), () -> super.createEntity(world, location, itemstack));
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return this.delegate(entityItem.getItem(), i -> i.onEntityItemUpdate(entityItem), () -> super.onEntityItemUpdate(entityItem));
    }

    @Override
    public float getSmeltingExperience(ItemStack item) {
        return this.delegate(item, i -> i.getSmeltingExperience(item), () -> super.getSmeltingExperience(item));
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return this.delegate(stack, i -> i.doesSneakBypassUse(stack, world, pos, player), () -> super.doesSneakBypassUse(stack, world, pos, player));
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        this.delegateVoid(itemStack, i -> i.onArmorTick(world, player, itemStack), () -> super.onArmorTick(world, player, itemStack));
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return this.delegate(stack, i -> i.isValidArmor(stack, armorType, entity), () -> super.isValidArmor(stack, armorType, entity));
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return this.delegate(stack, i -> i.isBookEnchantable(stack, book), () -> super.isBookEnchantable(stack, book));
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        return this.delegate(stack, i -> i.onEntitySwing(entityLiving, stack), () -> super.onEntitySwing(entityLiving, stack));
    }

    @Override
    public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks) {
        this.delegateVoid(stack, i -> i.renderHelmetOverlay(stack, player, resolution, partialTicks), () -> super.renderHelmetOverlay(stack, player, resolution, partialTicks));
    }

    @Override
    public int getDamage(ItemStack stack) {
        return this.delegate(stack, i -> i.getDamage(stack), () -> super.getDamage(stack));
    }

    @Override
    public int getMetadata(ItemStack stack) {
        return this.delegate(stack, i -> i.getMetadata(stack), () -> super.getMetadata(stack));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return this.delegate(stack, i -> i.showDurabilityBar(stack), () -> super.showDurabilityBar(stack));
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return this.delegate(stack, i -> i.getDurabilityForDisplay(stack), () -> super.getDurabilityForDisplay(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return this.delegate(stack, i -> i.getRGBDurabilityForDisplay(stack), () -> super.getRGBDurabilityForDisplay(stack));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.delegate(stack, i -> i.getMaxDamage(stack), () -> super.getMaxDamage(stack));
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return this.delegate(stack, i -> i.isDamaged(stack), () -> super.isDamaged(stack));
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        this.delegateVoid(stack, i -> i.setDamage(stack, damage), () -> super.setDamage(stack, damage));
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return this.delegate(stack, i -> i.canDestroyBlockInCreative(world, pos, stack, player), () -> super.canDestroyBlockInCreative(world, pos, stack, player));
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return this.delegate(stack, i -> i.canHarvestBlock(state, stack), () -> super.canHarvestBlock(state, stack));
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return this.delegate(stack, i -> i.getItemStackLimit(stack), () -> super.getItemStackLimit(stack));
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return this.delegate(stack, i -> i.getToolClasses(stack), () -> super.getToolClasses(stack));
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return this.delegate(stack, i -> i.getHarvestLevel(stack, toolClass, player, blockState), () -> super.getHarvestLevel(stack, toolClass, player, blockState));
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return this.delegate(stack, i -> i.getItemEnchantability(stack), () -> super.getItemEnchantability(stack));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.delegate(stack, i -> i.canApplyAtEnchantingTable(stack, enchantment), () -> super.canApplyAtEnchantingTable(stack, enchantment));
    }

    @Override
    public boolean isBeaconPayment(ItemStack stack) {
        return this.delegate(stack, i -> i.isBeaconPayment(stack), () -> super.isBeaconPayment(stack));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return this.delegate(oldStack, i -> i.shouldCauseReequipAnimation(oldStack, newStack, slotChanged), () -> super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged));
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return this.delegate(oldStack, i -> i.shouldCauseBlockBreakReset(oldStack, newStack), () -> super.shouldCauseBlockBreakReset(oldStack, newStack));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return this.delegate(stack, i -> i.initCapabilities(stack, nbt), () -> super.initCapabilities(stack, nbt));
    }

    @Override
    public ImmutableMap<String, ITimeValue> getAnimationParameters(ItemStack stack, World world, EntityLivingBase entity) {
        return this.delegate(stack, i -> i.getAnimationParameters(stack, world, entity), () -> super.getAnimationParameters(stack, world, entity));
    }

    @Override
    public ComponentType type() {
        return this.type;
    }
}
