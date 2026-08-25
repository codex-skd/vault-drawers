package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IPortable;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.DetachedDrawerData;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.DetachedDrawerContents;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.util.ComponentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ItemDetachedDrawer extends Item implements IPortable
{
    public ItemDetachedDrawer (Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance () {
        ItemStack stack = new ItemStack(this);

        DetachedDrawerData data = new DetachedDrawerData();
        data.setStorageMultiplier(ModCommonConfig.INSTANCE.DRAWERS.getBaseStackStorage() * 32);

        ItemStack savedItem = data.getStoredItemPrototype().copyWithCount(data.getStoredItemCount());
        DetachedDrawerContents contents = new DetachedDrawerContents(savedItem, data.getStorageMultiplier(), data.isHeavy());
        stack.set(ModDataComponents.DETACHED_DRAWER_CONTENTS.get(), contents);

        return stack;
    }

    @Override
    public void appendHoverText (@NotNull ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        ComponentUtil.appendSplitDescription(tooltip, this);

        if (ModCommonConfig.INSTANCE.DRAWERS.detached.heavyDrawers.get() && isHeavy(context.registries(), stack)) {
            tooltip.accept(Component.translatable("tooltip.storagedrawers.drawers.too_heavy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public Component getName (@NotNull ItemStack stack) {
        if (this == ModItems.DETACHED_DRAWER.get())
            return super.getName(stack);

        return Component.translatable(ModItems.DETACHED_DRAWER.get().getDescriptionId());
    }

    @NotNull
    public Component getDescription() {
        return ModCommonConfig.INSTANCE.DRAWERS.detached.enable.get()
            ? Component.translatable(this.getDescriptionId() + ".desc")
            : Component.translatable("itemConfig.storagedrawers.disabled_tool").withStyle(ChatFormatting.RED);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage (ItemStack stack) {
        return Optional.empty();
    }

    @Override
    public boolean canFitInsideContainerItems () {
        return ModCommonConfig.INSTANCE.DRAWERS.detached.canStoreInContainers.get();
    }

    @Override
    public boolean isHeavy(HolderLookup.Provider provider, @NotNull ItemStack stack) {
        if (stack.getItem() != this)
            return false;

        CustomData cdata = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        DetachedDrawerData data = new DetachedDrawerData(provider, cdata.copyTag());
        return data.isHeavy() && data.getStoredItemCount() > data.getStoredItemStackSize();
    }
}