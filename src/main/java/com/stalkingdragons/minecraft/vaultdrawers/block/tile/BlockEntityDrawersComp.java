package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.block.BlockCompDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.EnumCompDrawer;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawer;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerGroup;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.FractionalDrawerGroup;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import com.stalkingdragons.minecraft.vaultdrawers.network.CountUpdateMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDrawersComp extends BlockEntityDrawers
{
    private final FractionalDrawerGroup group;

    public BlockEntityDrawersComp (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        int slotCount = getBlockState().getBlock() instanceof BlockCompDrawers ? ((BlockCompDrawers) getBlockState().getBlock()).getDrawerCount() : 2;
        group = new GroupData(slotCount);
        injectPortableData(group);
    }

    public FractionalDrawerGroup getFractionalGroup() {
        return group;
    }

    @Override
    public IDrawerGroup getGroup() {
        return group;
    }

    @Override
    public void onAttributeChanged () {
        super.onAttributeChanged();
        group.syncAttributes();
    }

    public static BlockEntityType.BlockEntitySupplier<BlockEntityDrawersComp> create(int slotCount) {
        return (pos, state) -> new BlockEntityDrawersComp(
            (slotCount == 3 ? ModBlockEntities.FRACTIONAL_DRAWERS_3 : ModBlockEntities.FRACTIONAL_DRAWERS_2).get(),
            pos, state);
    }

    protected class GroupData extends FractionalDrawerGroup
    {
        public GroupData (int slotCount) {
            super(slotCount);
        }

        @Override
        protected Level getWorld () {
            return BlockEntityDrawersComp.this.getLevel();
        }

        @Override
        protected int getStackCapacity () {
            return upgrades().getStorageMultiplier() * getEffectiveDrawerCapacity();
        }

        @Override
        protected void onItemChanged () {
            Level world = getWorld();
            if (world == null || world.isClientSide())
                return;

            int usedSlots = 0;
            for (int slot : getAccessibleDrawerSlots()) {
                IDrawer drawer = getDrawer(slot);
                if (!drawer.isEmpty())
                    usedSlots += 1;
            }
            usedSlots = Math.max(usedSlots, 1);

            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockCompDrawers) {
                EnumCompDrawer open = state.getValue(BlockCompDrawers.SLOTS);
                if (open.getOpenSlots() != usedSlots)
                    world.setBlock(getBlockPos(), state.setValue(BlockCompDrawers.SLOTS, EnumCompDrawer.byOpenSlots(usedSlots)), 3);
            }

            setChanged();
            markBlockForUpdate();
        }

        @Override
        protected void onAmountChanged () {
            if (!(getWorld() instanceof ServerLevel serverLevel))
                return;

            ChameleonServices.NETWORK.sendToPlayersNear(new CountUpdateMessage(getBlockPos(), 0, getPooledCount()),
                serverLevel, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 500);

            setChanged();
        }
    }
}
