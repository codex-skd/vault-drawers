package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerGroup;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.StandardDrawerGroup;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BlockEntityDrawersStandard extends BlockEntityDrawers
{
    private final StandardDrawerGroup group;

    public BlockEntityDrawersStandard (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        int slotCount = getBlockState().getBlock() instanceof BlockDrawers ? ((BlockDrawers) getBlockState().getBlock()).getDrawerCount() : 2;
        group = new GroupData(slotCount);
        injectPortableData(group);
    }

    public StandardDrawerGroup getStandardGroup() {
        return group;
    }

    @Override
    public IDrawerGroup getGroup() {
        return group;
    }

    public static BlockEntityType.BlockEntitySupplier<BlockEntityDrawersStandard> create(int slotCount) {
        return (pos, state) -> new BlockEntityDrawersStandard(typeForSlotCount(slotCount).get(), pos, state);
    }

    private static com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.RegistryEntry<BlockEntityType<BlockEntityDrawersStandard>> typeForSlotCount(int slotCount) {
        return switch (slotCount) {
            case 4 -> ModBlockEntities.STANDARD_DRAWERS_4;
            case 2 -> ModBlockEntities.STANDARD_DRAWERS_2;
            default -> ModBlockEntities.STANDARD_DRAWERS_1;
        };
    }

    private class GroupData extends StandardDrawerGroup implements com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IProtectable, com.stalkingdragons.minecraft.vaultdrawers.api.storage.INetworked
    {
        public GroupData (int slotCount) {
            super(slotCount);
        }

        @NotNull
        @Override
        protected StandardDrawerGroup.DrawerData createDrawer (int slot) {
            return new GroupDrawerData(this, slot);
        }

        @Override
        public boolean isGroupValid () {
            return BlockEntityDrawersStandard.this.isGroupValid();
        }

        @Override
        public UUID getOwner () {
            return BlockEntityDrawersStandard.this.getOwner();
        }

        @Override
        public boolean setOwner (UUID owner) {
            return BlockEntityDrawersStandard.this.setOwner(owner);
        }

        @Override
        public com.stalkingdragons.minecraft.vaultdrawers.api.security.ISecurityProvider getSecurityProvider () {
            return BlockEntityDrawersStandard.this.getSecurityProvider();
        }

        @Override
        public boolean setSecurityProvider (com.stalkingdragons.minecraft.vaultdrawers.api.security.ISecurityProvider provider) {
            return BlockEntityDrawersStandard.this.setSecurityProvider(provider);
        }

        @Override
        public <T> T getCapability (com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability<T> capability) {
            if (getLevel() == null)
                return null;

            if (capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.DRAWER_ATTRIBUTES
                    || capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.DRAWER_GROUP
                    || capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.ITEM_REPOSITORY
                    || capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.ITEM_HANDLER) {
                return (T) BlockEntityDrawersStandard.this.getDrawerAttributes();
            }
            return null;
        }

        private class GroupDrawerData extends StandardDrawerGroup.DrawerData {
            private final int slot;

            GroupDrawerData(StandardDrawerGroup group, int slot) {
                super(group, slot);
                this.slot = slot;
            }

            @Override
            protected int getStackCapacity () {
                try {
                    return Math.multiplyExact(upgrades().getStorageMultiplier(), getEffectiveDrawerCapacity());
                } catch (ArithmeticException e) {
                    return Integer.MAX_VALUE;
                }
            }

            @Override
            protected void onItemChanged () {
                if (getLevel() != null && !getLevel().isClientSide()) {
                    setChanged();
                    markBlockForUpdate();
                }
            }

            @Override
            protected void onAmountChanged () {
                if (getLevel() != null && !getLevel().isClientSide()) {
                    syncClientCount(slot, getStoredItemCount());
                    setChanged();
                }
            }
        }
    }
}