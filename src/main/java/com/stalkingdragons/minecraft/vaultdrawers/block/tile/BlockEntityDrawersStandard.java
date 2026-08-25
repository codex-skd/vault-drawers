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

public class BlockEntityDrawersStandard extends BlockEntityDrawers
{
    private final StandardDrawerGroup group;

    public BlockEntityDrawersStandard (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        int slotCount = getBlockState().getBlock() instanceof BlockDrawers ? ((BlockDrawers) getBlockState().getBlock()).getDrawerCount() : 2;
        group = new GroupData(slotCount);
    }

    @Override
    public int getDrawerCapacity() {
        return group.getDrawer(0).getMaxCapacity();
    }

    public StandardDrawerGroup getStandardGroup() {
        return group;
    }

    @Override
    public IDrawerGroup getGroup() {
        return group;
    }

    public static BlockEntityType.BlockEntitySupplier<BlockEntityDrawersStandard> create(int slotCount) {
        return (pos, state) -> new BlockEntityDrawersStandard(ModBlockEntities.STANDARD_DRAWERS_1.get(), pos, state);
    }

    private class GroupData extends StandardDrawerGroup implements com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IProtectable, com.stalkingdragons.minecraft.vaultdrawers.api.storage.INetworked
    {
        public GroupData (int slotCount) {
            super(slotCount);
        }

        @NotNull
        @Override
        protected StandardDrawerGroup.DrawerData createDrawer (int slot) {
            return new StandardDrawerGroup.DrawerData(this, slot);
        }

        @Override
        public boolean isGroupValid () {
            return BlockEntityDrawersStandard.this.isGroupValid();
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
    }
}