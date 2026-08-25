package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockCompDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerGroup;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.FractionalDrawerGroup;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDrawersComp extends BlockEntityDrawers
{
    private final FractionalDrawerGroup group;

    public BlockEntityDrawersComp (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        int slotCount = getBlockState().getBlock() instanceof BlockCompDrawers ? ((BlockCompDrawers) getBlockState().getBlock()).getDrawerCount() : 2;
        group = new FractionalDrawerGroup(slotCount);
    }

    @Override
    public int getDrawerCapacity() {
        return group.getDrawer(0).getMaxCapacity();
    }

    public FractionalDrawerGroup getFractionalGroup() {
        return group;
    }

    @Override
    public IDrawerGroup getGroup() {
        return group;
    }

    public static BlockEntityType.BlockEntitySupplier<BlockEntityDrawersComp> create(int slotCount) {
        return (pos, state) -> new BlockEntityDrawersComp(ModBlockEntities.FRACTIONAL_DRAWERS_2.get(), pos, state);
    }
}