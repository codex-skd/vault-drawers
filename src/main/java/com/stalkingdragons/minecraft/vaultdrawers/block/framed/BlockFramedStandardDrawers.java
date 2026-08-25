package com.stalkingdragons.minecraft.vaultdrawers.block.framed;

import com.stalkingdragons.minecraft.vaultdrawers.api.config.IDrawerConfig;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.FrameMaterial;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlock;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlockEntity;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedMaterials;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.BlockType;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockStandardDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityDrawersStandard;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.FrameData;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockFramedStandardDrawers extends BlockStandardDrawers implements IFramedBlock
{
    public BlockFramedStandardDrawers (int drawerCount, boolean halfDepth, IDrawerConfig drawerConfig, BlockBehaviour.Properties properties) {
        super(drawerCount, halfDepth, drawerConfig, properties);
    }

    @Deprecated
    public BlockFramedStandardDrawers (int drawerCount, boolean halfDepth, int storageUnits, BlockBehaviour.Properties properties) {
        super(drawerCount, halfDepth, storageUnits, properties);
    }

    @Deprecated
    public BlockFramedStandardDrawers (int drawerCount, boolean halfDepth, BlockBehaviour.Properties properties) {
        super(drawerCount, halfDepth, properties);
    }

    @Override
    public BlockType retrimType () {
        return null;
    }

    public void setPlacedBy (@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);

        BlockEntityDrawers blockEntity = WorldUtils.getBlockEntity(world, pos, BlockEntityDrawers.class);
        if (blockEntity == null)
            return;

        IFramedMaterials materials = blockEntity.material();
        if (materials instanceof com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData materialData) {
            materialData.read(stack);
        }
        blockEntity.setChanged();
    }

    @Override
    protected ItemStack getMainDrop (BlockState state, BlockEntityDrawers tile) {
        ItemStack drop = super.getMainDrop(state, tile);

        IFramedMaterials materials = tile.material();
        if (!materials.isEmpty())
            drop.set(ModDataComponents.FRAME_DATA.get(), new FrameData(new com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData(materials)));

        return drop;
    }

    @Override
    public ItemStack getCloneItemStack (LevelReader level, BlockPos pos, BlockState state, boolean p_377230_, @Nullable Player player) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, p_377230_, player);

        BlockEntityDrawers tile = WorldUtils.getBlockEntity(level, pos, BlockEntityDrawers.class);
        if (tile != null) {
            IFramedMaterials materials = tile.material();
            if (!materials.isEmpty())
                stack.set(ModDataComponents.FRAME_DATA.get(), new FrameData(new com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData(materials)));
        }

        return stack;
    }

    @Override
    public IFramedBlockEntity getFramedBlockEntity (@NotNull Level world, @NotNull BlockPos pos) {
        return WorldUtils.getBlockEntity(world, pos, BlockEntityDrawers.class);
    }

    @Override
    public boolean supportsFrameMaterial (FrameMaterial material) {
        return true;
    }
}