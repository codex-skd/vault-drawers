package com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata;

import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityController;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ControllerData extends BlockEntityDataShim
{
    private BlockPos controllerCoord;
    private boolean needsValidation;

    @Override
    public void read (HolderLookup.Provider provider, CompoundTag tag) {
        controllerCoord = null;
        if (tag.contains("Controller")) {
            var ctagOpt = tag.getCompound("Controller");
            ctagOpt.ifPresent(ctag -> controllerCoord = new BlockPos(
                ctag.getInt("x").orElse(0),
                ctag.getInt("y").orElse(0),
                ctag.getInt("z").orElse(0)
            ));
        }

        needsValidation = tag.getBoolean("Validate").orElse(false);
    }

    @Override
    public CompoundTag write (HolderLookup.Provider provider, CompoundTag tag) {
        if (controllerCoord != null) {
            CompoundTag ctag = new CompoundTag();
            ctag.putInt("x", controllerCoord.getX());
            ctag.putInt("y", controllerCoord.getY());
            ctag.putInt("z", controllerCoord.getZ());
            tag.put("Controller", ctag);
        }

        if (needsValidation)
            tag.putBoolean("Validate", needsValidation);

        return tag;
    }

    public BlockPos getCoord () {
        return controllerCoord;
    }

    public BlockEntityController getController (BlockEntity host) {
        if (controllerCoord == null)
            return null;
        if (host.getLevel() == null)
            return null;

        BlockEntity blockEntity = host.getLevel().getBlockEntity(controllerCoord);
        if (!(blockEntity instanceof BlockEntityController)) {
            controllerCoord = null;
            host.setChanged();
            return null;
        }

        return (BlockEntityController)blockEntity;
    }

    public boolean bind (BlockEntityController entity) {
        return bindCoord(entity != null ? entity.getBlockPos() : null);
    }

    public boolean bindCoord (BlockPos pos) {
        if (ModCommonConfig.INSTANCE.GENERAL.debugTrace.get())
            VaultDrawers.log.info("ControllerData [{}] bind coord [{}]", controllerCoord, pos);

        if (controllerCoord == null || !controllerCoord.equals(pos)) {
            controllerCoord = pos;
            return true;
        }

        return false;
    }

    public boolean needsValidation () {
        return needsValidation;
    }

    public void setNeedsValidation (boolean state) {
        needsValidation = state;
    }
}