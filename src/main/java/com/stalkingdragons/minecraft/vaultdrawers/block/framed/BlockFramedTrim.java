package com.stalkingdragons.minecraft.vaultdrawers.block.framed;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.FrameMaterial;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlock;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockTrim;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlockEntity;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedMaterials;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityTrim;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.FrameData;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockFramedTrim extends BlockTrim implements EntityBlock, IFramedBlock
{
    public BlockFramedTrim (Properties properties) {
        super(properties);
    }

    public void setPlacedBy (@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);

        BlockEntityTrim blockEntity = WorldUtils.getBlockEntity(world, pos, BlockEntityTrim.class);
        if (blockEntity == null)
            return;

        IFramedMaterials materials = blockEntity.material();
        if (materials instanceof com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData materialData) {
            materialData.read(stack);
        }
        blockEntity.setChanged();
    }

    @Override
    public boolean canUseForRetrim () {
        return false;
    }

    @Override
    @NotNull
    public List<ItemStack> getDrops (@NotNull BlockState state, LootParams.Builder builder) {
        List<ItemStack> items = new ArrayList<>();
        items.add(getMainDrop(state, (com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityTrim)builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY)));
        return items;
    }

    protected ItemStack getMainDrop (BlockState state, com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityTrim tile) {
        ItemStack drop = new ItemStack(this);
        if (tile == null)
            return drop;

        IFramedMaterials materials = tile.material();
        if (!materials.isEmpty())
            drop.set(com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents.FRAME_DATA.get(), new com.stalkingdragons.minecraft.vaultdrawers.components.item.FrameData(new com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData(materials)));

        return drop;
    }

    @Override
    public ItemStack getCloneItemStack (LevelReader level, BlockPos pos, BlockState state, boolean p_377230_, @Nullable Player player) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, p_377230_, player);

        com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityTrim tile = com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils.getBlockEntity(level, pos, com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityTrim.class);
        if (tile != null && !tile.material().isEmpty())
            stack.set(com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents.FRAME_DATA.get(), new com.stalkingdragons.minecraft.vaultdrawers.components.item.FrameData(new com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData(tile.material())));

        return stack;
    }

    @Override
    public com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityTrim newBlockEntity (@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModServices.RESOURCE_FACTORY.createBlockEntityTrim().create(pos, state);
    }

    @Override
    public com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlockEntity getFramedBlockEntity (@NotNull Level world, @NotNull BlockPos pos) {
        return com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils.getBlockEntity(world, pos, com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityTrim.class);
    }

    @Override
    public boolean supportsFrameMaterial (FrameMaterial material) {
        return switch (material) {
            case SIDE, TRIM -> true;
            case FRONT -> false;
        };
    }
}