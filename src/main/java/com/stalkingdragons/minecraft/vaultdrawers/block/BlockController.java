package com.stalkingdragons.minecraft.vaultdrawers.block;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlock;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedSourceBlock;
import com.stalkingdragons.minecraft.vaultdrawers.api.security.ISecurityProvider;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesGroupControl;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.INetworked;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityController;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.util.FrameHelper;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlocks;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModSecurity;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemKey;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemKeyring;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemPersonalKey;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemUpgradeRemote;
import com.stalkingdragons.minecraft.vaultdrawers.security.SecurityManager;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import java.util.EnumSet;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

public class BlockController extends HorizontalDirectionalBlock implements INetworked, EntityBlock, IFramedSourceBlock
{
    public static final MapCodec<BlockController> CODEC = simpleCodec(BlockController::new);

    public BlockController (Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<BlockController> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement (BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem (@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!SecurityManager.canInteract(player, InteractionHand.MAIN_HAND, pos))
            return InteractionResult.PASS;

        Direction blockDir = state.getValue(FACING);
        BlockEntityController blockEntity = WorldUtils.getBlockEntity(level, pos, BlockEntityController.class);
        if (blockEntity == null)
            return InteractionResult.FAIL;

        ItemStack item = player.getInventory().getItem(player.getInventory().getSelectedSlot());
        if (player.getCooldowns().isOnCooldown(item))
            return InteractionResult.FAIL;

        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        if (!item.isEmpty() && toggle(level, pos, player, item.getItem()))
            return InteractionResult.SUCCESS;

        if (blockDir != hit.getDirection())
            return InteractionResult.CONSUME;

        if (!level.isClientSide()) {
            if (ModCommonConfig.INSTANCE.GENERAL.debugTrace.get() && item.isEmpty())
                blockEntity.printDebugInfo();

            if (item.getItem() instanceof ItemUpgradeRemote remote) {
                item = remote.setBoundController(item, blockEntity);
                player.getInventory().setItem(player.getInventory().getSelectedSlot(), item);

                player.sendSystemMessage(Component.translatable("message.vault_drawers.updated_remote_binding", pos.getX(), pos.getY(), pos.getZ()));
            }
        }

        return InteractionResult.SUCCESS;
    }

    private boolean toggle (Level level, BlockPos pos, Player player, Item item) {
        return false;
    }

    public void toggle (@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull KeyType keyType) {
        if (level.isClientSide())
            return;

        if (!keyType.isEnabled())
            return;

        BlockEntityController blockEntity = WorldUtils.getBlockEntity(level, pos, BlockEntityController.class);
        if (blockEntity == null)
            return;

        IDrawerAttributesGroupControl controlAttrs = blockEntity.getGroupControllableAttributes(player);
        if (controlAttrs != null) {
            if (keyType == KeyType.DRAWER)
                controlAttrs.setItemLocked(EnumSet.allOf(LockAttribute.class), LockAttribute.LOCK_EMPTY, true);
            else if (keyType == KeyType.CONCEALMENT)
                controlAttrs.setIsConcealed(true);
            else if (keyType == KeyType.QUANTIFY)
                controlAttrs.setIsShowingQuantity(true);
            else if (keyType == KeyType.SUSPEND)
                controlAttrs.setIsSuspended(true);
        }
    }

    @Override
    public void setPlacedBy (@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        BlockEntityController blockEntity = WorldUtils.getBlockEntity(world, pos, BlockEntityController.class);
        if (blockEntity == null)
            return;

        Item key = null;
        if (entity != null) {
            if (entity.getOffhandItem().getItem() instanceof ItemKey itemKey)
                key = itemKey;
            else if (entity.getOffhandItem().getItem() instanceof ItemKeyring itemKeyring)
                key = itemKeyring.getKey().getItem();
        }

        boolean keyEnabled = true;
        if (key instanceof ItemKey itemKey)
            keyEnabled = itemKey.isEnabled();

        IDrawerAttributesGroupControl _attrs = blockEntity.getGroupControllableAttributes(entity != null ? (Player) entity : null);
        if (_attrs != null && key != null && keyEnabled) {
            if (key == ModItems.DRAWER_KEY.get())
                _attrs.setItemLocked(EnumSet.of(LockAttribute.LOCK_EMPTY), LockAttribute.LOCK_EMPTY, true);
            else if (key == ModItems.QUANTIFY_KEY.get())
                _attrs.setIsShowingQuantity(true);
            else if (key == ModItems.SHROUD_KEY.get())
                _attrs.setIsConcealed(true);
        }
    }

    @Override
    public ItemStack makeFramedItem (ItemStack source, ItemStack matSide, ItemStack matTrim, ItemStack matFront) {
        IFramedBlock frameBlock = ModBlocks.FRAMED_CONTROLLER.get();
        if (frameBlock == null)
            return ItemStack.EMPTY;

        return FrameHelper.makeFramedItem(frameBlock, source, matSide, matTrim, matFront);
    }

    @Override
    @Nullable
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity (@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModServices.RESOURCE_FACTORY.createBlockEntityController().create(pos, state);
    }
}