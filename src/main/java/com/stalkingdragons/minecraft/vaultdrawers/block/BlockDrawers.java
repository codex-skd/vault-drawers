package com.stalkingdragons.minecraft.vaultdrawers.block;

import com.stalkingdragons.minecraft.vaultdrawers.ModConstants;
import com.stalkingdragons.minecraft.vaultdrawers.api.config.IDrawerConfig;
import com.stalkingdragons.minecraft.vaultdrawers.api.security.ISecurityProvider;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.*;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.DetachedDrawerData;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.UpgradeData;
import com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.DetachedDrawerContents;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModSecurity;
import com.stalkingdragons.minecraft.vaultdrawers.inventory.DrawerInventoryHelper;
import com.stalkingdragons.minecraft.vaultdrawers.item.*;
import com.stalkingdragons.minecraft.vaultdrawers.security.SecurityManager;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.inventory.ContentMenuProvider;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BlockDrawers extends FaceSlotBlock implements INetworked, EntityBlock
{
    private static final VoxelShape AABB_NORTH_FULL = Shapes.join(Shapes.block(), Block.box(1, 1, 0, 15, 15, 1), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_SOUTH_FULL = Shapes.join(Shapes.block(), Block.box(1, 1, 15, 15, 15, 16), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_WEST_FULL = Shapes.join(Shapes.block(), Block.box(0, 1, 1, 1, 15, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_EAST_FULL = Shapes.join(Shapes.block(), Block.box(15, 1, 1, 16, 15, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_NORTH_HALF = Block.box(0, 0, 8, 16, 16, 16);
    private static final VoxelShape AABB_SOUTH_HALF = Block.box(0, 0, 0, 16, 16, 8);
    private static final VoxelShape AABB_WEST_HALF = Block.box(8, 0, 0, 16, 16, 16);
    private static final VoxelShape AABB_EAST_HALF = Block.box(0, 0, 0, 8, 16, 16);
    private static final VoxelShape HOPPER_INSIDE = Block.box(2, 12, 2, 14, 16, 14);
    private static final VoxelShape AABB_NORTH_HOPPER = Shapes.join(AABB_NORTH_FULL, HOPPER_INSIDE, BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_SOUTH_HOPPER = Shapes.join(AABB_SOUTH_FULL, HOPPER_INSIDE, BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_WEST_HOPPER = Shapes.join(AABB_WEST_FULL, HOPPER_INSIDE, BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_EAST_HOPPER = Shapes.join(AABB_EAST_FULL, HOPPER_INSIDE, BooleanOp.ONLY_FIRST);

    private static final Map<UUID, Long> lastLeftClick = new HashMap<>();

    private final int drawerCount;
    private final boolean halfDepth;
    private final IDrawerConfig drawerConfig;

    private int storageUnits;

    public final AABB[] slotGeometry;
    public final AABB[] countGeometry;
    public final AABB[] labelGeometry;
    public final AABB[] indGeometry;
    public final AABB[] indBaseGeometry;

    private long ignoreEventTime;

    public BlockDrawers (int drawerCount, boolean halfDepth, IDrawerConfig drawerConfig, Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH));

        this.drawerCount = drawerCount;
        this.halfDepth = halfDepth;
        this.drawerConfig = drawerConfig;
        this.storageUnits = 0;

        slotGeometry = new AABB[drawerCount];
        countGeometry = new AABB[drawerCount];
        labelGeometry = new AABB[drawerCount];
        indGeometry = new AABB[drawerCount];
        indBaseGeometry = new AABB[drawerCount];

        for (int i = 0; i < drawerCount; i++) {
            slotGeometry[i] = new AABB(0, 0, 0, 0, 0, 0);
            countGeometry[i] = new AABB(0, 0, 0, 0, 0, 0);
            labelGeometry[i] = new AABB(0, 0, 0, 0, 0, 0);
            indGeometry[i] = new AABB(0, 0, 0, 0, 0, 0);
            indBaseGeometry[i] = new AABB(0, 0, 0, 0, 0, 0);
        }
    }

    @Deprecated
    public BlockDrawers (int drawerCount, boolean halfDepth, int storageUnits, Properties properties) {
        this(drawerCount, halfDepth, null, properties);
        this.storageUnits = storageUnits;
    }

    @Override
    protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public boolean retrimBlock (Level world, BlockPos pos, ItemStack prototype) { return false; }
    public BlockType retrimType () { return BlockType.Drawers; }
    public boolean repartitionBlock (Level world, BlockPos pos, ItemStack prototype) { return false; }

    public int getDrawerCount () { return drawerCount; }
    public boolean isHalfDepth () { return halfDepth; }
    public int getStorageUnits () { return drawerConfig != null ? drawerConfig.getUnitsPerSlot() : storageUnits; }

    public String getNameTypeKey () {
        String type = halfDepth ? "half" : "full";
        return "block." + ModConstants.MOD_ID + ".type." + type + "_drawers_" + getDrawerCount();
    }

    @Override
    @NotNull
    public VoxelShape getShape (@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case EAST -> halfDepth ? AABB_EAST_HALF : AABB_EAST_FULL;
            case WEST -> halfDepth ? AABB_WEST_HALF : AABB_WEST_FULL;
            case SOUTH -> halfDepth ? AABB_SOUTH_HALF : AABB_SOUTH_FULL;
            case NORTH -> halfDepth ? AABB_NORTH_HALF : AABB_NORTH_FULL;
            default -> halfDepth ? AABB_NORTH_HALF : AABB_NORTH_FULL;
        };
    }

    @Override
    public boolean isPathfindable (@NotNull BlockState state, @NotNull PathComputationType type) { return false; }

    @Override
    public BlockState getStateForPlacement (BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy (@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) { }

    public Optional<InteractionResult> useSlotInvertible (InteractContext context) { return Optional.empty(); }
    public Optional<InteractionResult> useSlot (InteractContext context) { return Optional.empty(); }
    public InteractionResult putSlot (InteractContext context, boolean altAction) { return InteractionResult.PASS; }
    public InteractionResult takeSlot (InteractContext context, boolean altAction) { return InteractionResult.PASS; }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider (BlockState blockState, Level level, BlockPos blockPos) { return null; }

    @Override
    @NotNull
    public List<ItemStack> getDrops (@NotNull BlockState state, LootParams.Builder builder) { return List.of(new ItemStack(this)); }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource (@NotNull BlockState state) { return false; }

    @Override
    @SuppressWarnings("deprecation")
    public int getSignal (@NotNull BlockState state, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) { return 0; }

    @Override
    public int getDirectSignal (@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull Direction side) { return 0; }

    

    public boolean hasAnalogOutputSignal(BlockState state) { return false; }
    public int getAnalogOutputSignal(BlockState state, Level blockAccess, BlockPos pos) { return 0; }
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) { return true; }
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) { }
    protected VoxelShape getInteractionShape (BlockState state, BlockGetter blockGetter, BlockPos pos) { return super.getInteractionShape(state, blockGetter, pos); }
    protected void tick (@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource rand) { }

    protected ItemStack getMainDrop (BlockState state, BlockEntityDrawers tile) {
        ItemStack drop = new ItemStack(this);
        if (tile == null)
            return drop;

        if (ModCommonConfig.INSTANCE.DRAWERS.storage.dropMode.get() == ModCommonConfig.DropMode.KEEP) {
            boolean hasUpgradeContents = false;
            boolean hasItemContents = false;
            for (int i = 0; i < tile.getGroup().getDrawerCount(); i++) {
                IDrawer drawer = tile.getGroup().getDrawer(i);
                if (!drawer.isEmpty() || drawer.isMissing())
                    hasItemContents = true;
            }
            for (int i = 0; i < tile.upgrades().getSlotCount(); i++) {
                if (!tile.upgrades().getUpgrade(i).isEmpty())
                    hasUpgradeContents = true;
            }
            if (hasItemContents || hasUpgradeContents) {
                if (hasItemContents) {
                    ItemStack item = tile.getGroup().getDrawer(0).getStoredItemPrototype();
                    if (!item.isEmpty())
                        drop = item.copyWithCount(1);
                }
                if (hasUpgradeContents) {
                    for (int i = 0; i < tile.upgrades().getSlotCount(); i++) {
                        ItemStack upgrade = tile.upgrades().getUpgrade(i);
                        if (!upgrade.isEmpty()) {
                            ItemStack copy = upgrade.copyWithCount(1);
                            if (drop.isEmpty())
                                drop = copy;
                            else
                                Containers.dropItemStack(tile.getLevel(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), copy);
                        }
                    }
                }
            }
        }
        return drop;
    }
}