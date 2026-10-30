package com.stalkingdragons.minecraft.vaultdrawers.service;

import com.stalkingdragons.minecraft.vaultdrawers.block.tile.*;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PlatformResourceFactory implements ResourceFactory
{
    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntityDrawersStandard> createBlockEntityDrawersStandard (int slotCount) {
        return BlockEntityDrawersStandard.create(slotCount);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntityDrawersComp> createBlockEntityDrawersComp (int slotCount) {
        return BlockEntityDrawersComp.create(slotCount);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntityController> createBlockEntityController () {
        return BlockEntityController.create();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntityControllerIO> createBlockEntityControllerIO () {
        return BlockEntityControllerIO.create();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntityFramingTable> createBlockEntityFramingTable () {
        return BlockEntityFramingTable.create();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntityTrim> createBlockEntityTrim () {
        return BlockEntityTrim::new;
    }
}
