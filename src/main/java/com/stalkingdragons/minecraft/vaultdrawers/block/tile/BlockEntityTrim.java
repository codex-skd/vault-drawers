package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlockEntity;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTrim extends BaseBlockEntity implements IFramedBlockEntity
{
    private MaterialData materialData = new MaterialData();

    protected BlockEntityTrim (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        injectPortableData(materialData);
    }

    public BlockEntityTrim (BlockPos pos, BlockState state) {
        this(ModBlockEntities.TRIM.get(), pos, state);
    }

    @Override
    public MaterialData material () {
        return materialData;
    }

    @Override
    public boolean dataPacketRequiresRenderUpdate () {
        return true;
    }
}