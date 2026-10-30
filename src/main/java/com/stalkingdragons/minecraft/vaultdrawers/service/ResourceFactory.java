package com.stalkingdragons.minecraft.vaultdrawers.service;

import com.stalkingdragons.minecraft.vaultdrawers.block.BlockCompDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockStandardDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public interface ResourceFactory
{
    BlockEntityType.BlockEntitySupplier<BlockEntityDrawersStandard> createBlockEntityDrawersStandard (int slotCount);
    BlockEntityType.BlockEntitySupplier<BlockEntityDrawersComp> createBlockEntityDrawersComp (int slotCount);
    BlockEntityType.BlockEntitySupplier<BlockEntityController> createBlockEntityController ();
    BlockEntityType.BlockEntitySupplier<BlockEntityControllerIO> createBlockEntityControllerIO ();
    BlockEntityType.BlockEntitySupplier<BlockEntityFramingTable> createBlockEntityFramingTable ();
    BlockEntityType.BlockEntitySupplier<BlockEntityTrim> createBlockEntityTrim ();
}