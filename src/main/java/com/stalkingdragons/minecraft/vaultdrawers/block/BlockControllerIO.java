package com.stalkingdragons.minecraft.vaultdrawers.block;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedSourceBlock;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.INetworked;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityController;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityControllerIO;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.util.FrameHelper;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlocks;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockControllerIO extends Block implements INetworked, EntityBlock, IFramedSourceBlock
{
    public BlockControllerIO (Properties properties) {
        super(properties);
    }

    public BlockEntityController getController(Level world, BlockPos pos) {
        BlockEntityControllerIO blockEntity = WorldUtils.getBlockEntity(world, pos, BlockEntityControllerIO.class);
        if (blockEntity == null)
            return null;

        return blockEntity.getController();
    }

    @Override
    public BlockEntityControllerIO newBlockEntity (@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModServices.RESOURCE_FACTORY.createBlockEntityControllerIO().create(pos, state);
    }

    @Override
    public ItemStack makeFramedItem (ItemStack source, ItemStack matSide, ItemStack matTrim, ItemStack matFront) {
        return FrameHelper.makeFramedItem(ModBlocks.FRAMED_CONTROLLER_IO.get(), source, matSide, matTrim, matFront);
    }
}