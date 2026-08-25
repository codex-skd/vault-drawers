package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IPortable;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockStandardDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.UpgradeData;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ItemDrawers extends BlockItem implements IPortable
{
    public ItemDrawers (Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText (ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);

        Component textCapacity = Component.translatable("tooltip.storagedrawers.drawers.capacity", getCapacityForBlock(stack));
        tooltip.accept(Component.literal("").append(textCapacity).withStyle(ChatFormatting.GRAY));

        TypedEntityData<BlockEntityType<?>> blockData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (blockData != null || customData != null) {
            Component textSealed = Component.translatable("tooltip.storagedrawers.drawers.sealed");
            tooltip.accept(Component.literal("").append(textSealed).withStyle(ChatFormatting.YELLOW));
        }

        if (ModCommonConfig.INSTANCE.DRAWERS.filled.heavyDrawers.get() && isHeavy(context.registries(), stack)) {
            tooltip.accept(Component.translatable("tooltip.storagedrawers.drawers.too_heavy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public Component getName (ItemStack stack) {
        String fallback = null;
        Block block = Block.byItem(stack.getItem());

        if (block instanceof BlockStandardDrawers drawers) {
            String matKey = drawers.getMatKey();
            if (matKey != null) {
                String mat = Component.translatable(drawers.getNameMatKey()).getString();
                fallback = Component.translatable(drawers.getNameTypeKey(), mat).getString();
            }
        }

        return Component.translatableWithFallback(this.getDescriptionId(), fallback);
    }

    @NotNull
    public Component getDescription() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    @Override
    public boolean isHeavy(HolderLookup.Provider provider, @NotNull ItemStack stack) {
        if (stack.getItem() != this)
            return false;

        TypedEntityData<BlockEntityType<?>> data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null)
            return false;

        var x = new UpgradeData(7);
        try {
            x.read(provider, data.getUnsafe());
        } catch (Exception e) {
            return false;
        }

        return !x.hasPortabilityUpgrade();
    }

    private int getCapacityForBlock (@NotNull ItemStack itemStack) {
        Block block = Block.byItem(itemStack.getItem());
        if (block instanceof BlockDrawers blockDrawers) {
            return blockDrawers.getStorageUnits() * ModCommonConfig.INSTANCE.DRAWERS.baseStackStorage.get();
        }

        return 0;
    }

    @Override
    public boolean canFitInsideContainerItems () {
        return ModCommonConfig.INSTANCE.DRAWERS.filled.canStoreInContainers.get();
    }

    public boolean doesSneakBypassUse (ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        return block instanceof BlockDrawers bd && bd.retrimType() != null;
    }
}