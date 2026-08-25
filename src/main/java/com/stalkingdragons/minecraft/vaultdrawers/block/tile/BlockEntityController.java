package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.api.capabilities.IItemRepository;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlockEntity;
import com.stalkingdragons.minecraft.vaultdrawers.api.security.ISecurityProvider;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.*;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IProtectable;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockController;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.ControllerHostData;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.capabilities.BasicDrawerAttributes;
import com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities;
import com.stalkingdragons.minecraft.vaultdrawers.capabilities.DrawerItemRepository;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import com.stalkingdragons.minecraft.vaultdrawers.security.SecurityManager;
import com.stalkingdragons.minecraft.vaultdrawers.storage.StorageUtil;
import com.stalkingdragons.minecraft.vaultdrawers.util.ItemCollectionRegistry;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BlockEntityController extends BaseBlockEntity implements IDrawerGroup, IControlGroup, IFramedBlockEntity
{
    private final ControllerHostData controllerHostData = new ControllerHostData();
    private final MaterialData materialData = new MaterialData();

    protected final DrawerAttributes drawerAttributes;

    @Override
    public MaterialData material () {
        return materialData;
    }

    @Override
    public int getDrawerCount() {
        return 0;
    }

    @Override
    public <T> T getCapability(com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability<T> capability) {
        if (capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.DRAWER_ATTRIBUTES
            || capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.DRAWER_GROUP
            || capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.ITEM_REPOSITORY
            || capability == com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities.ITEM_HANDLER) {
            return (T) drawerAttributes;
        }
        return null;
    }

    @Override
    public IDrawer getDrawer(int slot) {
        return Drawers.DISABLED;
    }

    @Override
    public int[] getAccessibleDrawerSlots() {
        return new int[0];
    }

    @Override
    public IDrawerGroup getDrawerGroup() {
        return this;
    }

    @Override
    public IDrawerAttributesGroupControl getGroupControllableAttributes(Player player) {
        return drawerAttributes;
    }

    @Override
    public IControlGroup getBoundControlGroup() {
        return this;
    }

    @Override
    public List<INetworked> getBoundRemoteNodes() {
        return List.of();
    }

    @Override
    public void validateRemoteNode(INetworked node) {
    }

    @Override
    public void invalidateRemoteNode(INetworked node) {
    }

    @Override
    public boolean addRemoteNode(INetworked node) {
        return false;
    }

    @Override
    public boolean isSoftBindingValid(BlockPos pos, IDrawerGroup node) {
        return false;
    }

    public IDrawerAttributesModifiable getDrawerAttributes() {
        return drawerAttributes;
    }

    protected void onAttributeChanged() {
        setChanged();
        markBlockForUpdate();
    }

    public IControlGroup getControlGroup() {
        return this;
    }

    public void validateBoundController() {
    }

    public Stream<IDrawer> getBalanceDrawers(@NotNull ItemStack stack, Player player) {
        return controllerHostData.getRemoteNodes().flatMap(node -> {
            if (node instanceof IDrawerGroup group) {
                List<IDrawer> drawers = new ArrayList<>();
                for (int i = 0; i < group.getDrawerCount(); i++) {
                    IDrawer drawer = group.getDrawer(i);
                    if (drawer.isEnabled() && !drawer.isEmpty()
                        && ItemStack.isSameItemSameComponents(stack, drawer.getStoredItemPrototype())) {
                        IDrawerAttributes attr = drawer.getAttributes();
                        if (attr.isBalancedFill() && !attr.isSuspended()) {
                            if (player != null && group instanceof IProtectable prot) {
                                if (!SecurityManager.hasAccess(player, prot))
                                    continue;
                            }
                            drawers.add(drawer);
                        }
                    }
                }
                return drawers.stream();
            }
            return Stream.empty();
        });
    }

    public void printDebugInfo() {
        ModServices.log.info("Controller at " + worldPosition);
        ModServices.log.info("  Range: " + ModCommonConfig.INSTANCE.CONTROLLER.controllerRange.get() + " blocks");
        ModServices.log.info("  Stored records: " + controllerHostData.getRemoteNodes().count() + ", slot list: " + controllerHostData.getRemoteNodes().count());
        ModServices.log.info("  Ticks since last update: " + (getLevel() == null ? "null" : (getLevel().getGameTime() - 0)));
    }

    public static BlockEntityType.BlockEntitySupplier<BlockEntityController> create() {
        return (pos, state) -> new BlockEntityController(ModBlockEntities.CONTROLLER.get(), pos, state);
    }

    public BlockController getBlock() {
        Block block = getLevel().getBlockState(getBlockPos()).getBlock();
        if (block instanceof BlockController)
            return (BlockController) block;
        return null;
    }

    private class DrawerAttributes extends BasicDrawerAttributes implements com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesGroupControl
    {
        @Override
        protected void onAttributeChanged () {
            BlockEntityController.this.onAttributeChanged();
            if (getLevel() != null && !getLevel().isClientSide()) {
                setChanged();
                markBlockForUpdate();
            }
        }
    }

    protected BlockEntityController (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        drawerAttributes = new DrawerAttributes();

        injectData(materialData);
        injectData(controllerHostData);
    }
}