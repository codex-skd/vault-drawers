package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.BlockEntityDataShim;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseBlockEntity extends BlockEntity
{
    private CompoundTag failureSnapshot;
    private List<BlockEntityDataShim> fixedShims;
    private List<BlockEntityDataShim> portableShims;

    public BaseBlockEntity (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    public boolean hasDataPacket () {
        return true;
    }

    public boolean dataPacketRequiresRenderUpdate () {
        return false;
    }

    public void injectData (BlockEntityDataShim shim) {
        if (fixedShims == null)
            fixedShims = new ArrayList<>();
        fixedShims.add(shim);
    }

    public void injectPortableData (BlockEntityDataShim shim) {
        if (portableShims == null)
            portableShims = new ArrayList<>();
        portableShims.add(shim);
    }

    protected void onLoadFinished () { }

    public void readPortable (HolderLookup.Provider provider, CompoundTag tag) {
        if (portableShims != null) {
            for (BlockEntityDataShim shim : portableShims)
                shim.read(provider, tag);
        }
    }

    public CompoundTag writePortable (HolderLookup.Provider provider, CompoundTag tag) {
        if (portableShims != null) {
            for (BlockEntityDataShim shim : portableShims)
                tag = shim.write(provider, tag);
        }

        return tag;
    }

    protected void readFixed (HolderLookup.Provider provider, CompoundTag tag) {
        if (fixedShims != null) {
            for (BlockEntityDataShim shim : fixedShims)
                shim.read(provider, tag);
        }
    }

    protected CompoundTag writeFixed (HolderLookup.Provider provider, CompoundTag tag) {
        if (fixedShims != null) {
            for (BlockEntityDataShim shim : fixedShims)
                tag = shim.write(provider, tag);
        }

        return tag;
    }

    public final void read (CompoundTag tag, HolderLookup.Provider registries) {
        readFixed(registries, tag);
        readPortable(registries, tag);
    }

    @Override
    @NotNull
    public final CompoundTag getUpdateTag (HolderLookup.Provider provider) {
        return this.saveCustomOnly(provider);
    }

    @Override
    public final ClientboundBlockEntityDataPacket getUpdatePacket () {
        return hasDataPacket() ? ClientboundBlockEntityDataPacket.create(this) : null;
    }

    public final void onDataPacket (Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (pkt != null && pkt.getTag() != null)
            read(pkt.getTag(), lookupProvider);

        if (getLevel() != null && getLevel().isClientSide() && dataPacketRequiresRenderUpdate()) {
            BlockState state = getLevel().getBlockState(getBlockPos());
            getLevel().sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_ALL);
        }
    }

    public void markBlockForUpdate () {
        if (getLevel() != null && !getLevel().isClientSide()) {
            BlockState state = getLevel().getBlockState(worldPosition);
            getLevel().sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    public void markBlockForUpdateClient () {
        if (getLevel() != null && getLevel().isClientSide()) {
            BlockState state = getLevel().getBlockState(worldPosition);
            getLevel().sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    public void markBlockForRenderUpdate () {
        if (getLevel() == null)
            return;

        BlockState state = getLevel().getBlockState(worldPosition);
        getLevel().sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
    }
}