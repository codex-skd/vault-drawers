package com.stalkingdragons.minecraft.vaultdrawers.block;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityFramingTable;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import com.mojang.serialization.MapCodec;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.inventory.ContentMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockFramingTable extends HorizontalDirectionalBlock implements EntityBlock
{
    public static final EnumProperty<EnumFramingTablePart> PART = EnumProperty.create("part", EnumFramingTablePart.class);

    public static final MapCodec<BlockFramingTable> CODEC = simpleCodec(BlockFramingTable::new);

    protected static final VoxelShape TABLE_TOP = Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape TABLE_BOTTOM_NORTH = Block.box(1.0D, 0.0D, 0.0D, 15.0D, 16.0D, 15.0D);
    protected static final VoxelShape TABLE_BOTTOM_SOUTH = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 16.0D);
    protected static final VoxelShape TABLE_BOTTOM_WEST = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 16.0D, 15.0D);
    protected static final VoxelShape TABLE_BOTTOM_EAST = Block.box(0.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    protected static final VoxelShape TABLE_SHAPE_NORTH = Shapes.or(TABLE_TOP, TABLE_BOTTOM_NORTH);
    protected static final VoxelShape TABLE_SHAPE_SOUTH = Shapes.or(TABLE_TOP, TABLE_BOTTOM_SOUTH);
    protected static final VoxelShape TABLE_SHAPE_WEST = Shapes.or(TABLE_TOP, TABLE_BOTTOM_WEST);
    protected static final VoxelShape TABLE_SHAPE_EAST = Shapes.or(TABLE_TOP, TABLE_BOTTOM_EAST);

    public BlockFramingTable (BlockBehaviour.Properties properties) {
        super(properties);

        this.registerDefaultState(getStateDefinition().any().setValue(PART, EnumFramingTablePart.RIGHT));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec () {
        return CODEC;
    }

    public static Direction getTableDirection (BlockGetter getter, BlockPos pos) {
        BlockState state = getter.getBlockState(pos);
        return state.getBlock() instanceof BlockFramingTable ? state.getValue(FACING) : null;
    }

    private static Direction getNeighborDirection (EnumFramingTablePart part, Direction direction) {
        return part == EnumFramingTablePart.LEFT ? direction.getClockWise() : direction.getCounterClockWise();
    }

    @Override
    public VoxelShape getShape (BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        EnumFramingTablePart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);

        return switch (facing) {
            case NORTH -> part == EnumFramingTablePart.LEFT ? TABLE_SHAPE_WEST : TABLE_SHAPE_EAST;
            case SOUTH -> part == EnumFramingTablePart.LEFT ? TABLE_SHAPE_EAST : TABLE_SHAPE_WEST;
            case WEST -> part == EnumFramingTablePart.LEFT ? TABLE_SHAPE_NORTH : TABLE_SHAPE_SOUTH;
            case EAST -> part == EnumFramingTablePart.LEFT ? TABLE_SHAPE_SOUTH : TABLE_SHAPE_NORTH;
            default -> TABLE_TOP;
        };
    }

    @Override
    public BlockState playerWillDestroy (Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() ) {
            preventCreativeDropFromLeft(level, pos, state, player);
            if (!player.isCreative() && state.getValue(PART) != EnumFramingTablePart.RIGHT)
                dropResources(state.setValue(PART, EnumFramingTablePart.RIGHT), level, pos, null, player, player.getMainHandItem());
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    protected static void preventCreativeDropFromLeft (Level level, BlockPos pos, BlockState state, Player player) {
        EnumFramingTablePart part = state.getValue(PART);
        if (part == EnumFramingTablePart.RIGHT) {
            BlockPos pos2 = pos.relative(getNeighborDirection(part, state.getValue(FACING)));
            BlockState state2 = level.getBlockState(pos2);
        }
    }

    @Override
    protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART);
    }

    @Override
    public BlockState getStateForPlacement (BlockPlaceContext context) {
        Direction dir = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        BlockPos pos2 = pos.relative(dir.getClockWise());

        BlockState state = this.defaultBlockState()
            .setValue(FACING, dir)
            .setValue(PART, EnumFramingTablePart.RIGHT);

        BlockState state2 = this.defaultBlockState()
            .setValue(FACING, dir)
            .setValue(PART, EnumFramingTablePart.LEFT);

        if (context.getLevel().isClientSide())
            return state;

        if (context.getLevel().isEmptyBlock(pos2))
            return state;

        return state;
    }

    @Override
    public void setPlacedBy (@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        if (world.isClientSide())
            return;

        BlockPos pos2 = pos.relative(getNeighborDirection(state.getValue(PART), state.getValue(FACING)));
        if (world.getBlockState(pos2).isAir())
            world.setBlockAndUpdate(pos2, state.setValue(PART, EnumFramingTablePart.LEFT));
    }

    public void onRemove (BlockState state, Level level, BlockPos pos, BlockState newState, boolean flag) {
        if (!state.is(newState.getBlock())) {
            EnumFramingTablePart part = state.getValue(PART);
            if (part == EnumFramingTablePart.RIGHT) {
                BlockPos pos2 = pos.relative(getNeighborDirection(part, state.getValue(FACING)));
                level.destroyBlock(pos2, false);
            }
        }
    }

    @Override
    public InteractionResult useWithoutItem (@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        MenuProvider provider = state.getMenuProvider(level, pos);
        if (provider != null) {
            player.openMenu(provider);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider (BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntityFramingTable blockEntity = WorldUtils.getBlockEntity(level, blockPos, BlockEntityFramingTable.class);
        if (blockEntity == null)
            return null;

        return new BlockEntityFramingTable.ContentProvider(blockEntity);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity (@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModServices.RESOURCE_FACTORY.createBlockEntityFramingTable().create(pos, state);
    }
}