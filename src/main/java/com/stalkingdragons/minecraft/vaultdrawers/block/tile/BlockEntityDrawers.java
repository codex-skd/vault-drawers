package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.ModServices;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlockEntity;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedMaterials;
import com.stalkingdragons.minecraft.vaultdrawers.api.security.ISecurityProvider;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.*;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IProtectable;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.MagnetDim;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.ControllerData;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.UpgradeData;
import com.stalkingdragons.minecraft.vaultdrawers.capabilities.BasicDrawerAttributes;
import com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.DetachedDrawerContents;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModSecurity;
import com.stalkingdragons.minecraft.vaultdrawers.inventory.*;
import com.stalkingdragons.minecraft.vaultdrawers.item.EnumUpgradeRedstone;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemUpgradeRemote;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemUpgradeStorage;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.inventory.ContentMenuProvider;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.inventory.content.PositionContent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.UUID;

public abstract class BlockEntityDrawers extends BaseBlockEntity implements IDrawerGroup, IProtectable, INetworked, IFramedBlockEntity, Nameable
{
    private MaterialData materialData = new MaterialData();
    private final UpgradeData upgradeData = new DrawerUpgradeData();
    private final ControllerData controllerData = new ControllerData();

    private final Set<IControlGroup> softBoundControlGroups = new HashSet<>();

    private UUID owner;
    private String securityKey;
    private Component name;

    protected final IDrawerAttributesModifiable drawerAttributes;

    private long lastClickTime;
    private UUID lastClickUUID;
    private boolean loading;

    private AABB SUCK_AABB = Block.box(0.0, 11.0, 0.0, 16.0, 32.0, 16.0).toAabbs().getFirst();
    private AABB MAGNET_AABB = AABB.of(BoundingBox.fromCorners(Vec3i.ZERO, Vec3i.ZERO));

    private class DrawerAttributes extends BasicDrawerAttributes
    {
        @Override
        protected void onAttributeChanged () {
            if (!loading && !BlockEntityDrawers.this.drawerAttributes.isItemLocked(LockAttribute.LOCK_POPULATED)) {
                for (int slot = 0; slot < BlockEntityDrawers.this.getGroup().getDrawerCount(); slot++) {
                    if (BlockEntityDrawers.this.emptySlotCanBeCleared(slot)) {
                        IDrawer drawer = BlockEntityDrawers.this.getGroup().getDrawer(slot);
                        drawer.setStoredItem(ItemStack.EMPTY);
                    }
                }
            }

            BlockEntityDrawers.this.onAttributeChanged();
            if (getLevel() != null && !getLevel().isClientSide()) {
                setChanged();
                markBlockForUpdate();
            }
        }
    }

    private class DrawerUpgradeData extends UpgradeData
    {
        DrawerUpgradeData () {
            super(7);
        }

        @Override
        public boolean canAddUpgrade (@NotNull ItemStack upgrade) {
            if (!super.canAddUpgrade(upgrade))
                return false;

            if (upgrade.getItem() == ModItems.ONE_STACK_UPGRADE.get()) {
                int currentUpgradeMult = upgradeData.getStorageMultiplier();
                return stackCapacityCheck(currentUpgradeMult);
            }

            return true;
        }

        @Override
        public boolean canRemoveUpgrade (int slot) {
            if (!super.canRemoveUpgrade(slot))
                return false;

            ItemStack upgrade = getUpgrade(slot);
            if (upgrade.getItem() instanceof ItemUpgradeStorage) {
                int currentUpgradeMult = upgradeData.getStorageMultiplier();

                int remLevel = ((ItemUpgradeStorage) upgrade.getItem()).level.getLevel();
                int remMult = ModCommonConfig.INSTANCE.UPGRADES.getLevelMult(remLevel);

                return stackCapacityCheck(getDrawerCapacity() * (currentUpgradeMult - remMult));
            }

            return true;
        }

        @Override
        public boolean canSwapUpgrade(int slot, @NotNull ItemStack add) {
            if (!(add.getItem() instanceof ItemUpgradeStorage))
                return false;

            ItemStack upgrade = getUpgrade(slot);
            if (upgrade.getItem() == ModItems.ONE_STACK_UPGRADE.get())
                return true;

            if (!(upgrade.getItem() instanceof ItemUpgradeStorage))
                return false;

            int currentLevel = ((ItemUpgradeStorage) upgrade.getItem()).level.getLevel();
            int newLevel = ((ItemUpgradeStorage) add.getItem()).level.getLevel();

            if (newLevel <= currentLevel)
                return false;

            int currentUpgradeMult = upgradeData.getStorageMultiplier();
            int newMult = ModCommonConfig.INSTANCE.UPGRADES.getLevelMult(newLevel);
            int remMult = ModCommonConfig.INSTANCE.UPGRADES.getLevelMult(currentLevel);

            return stackCapacityCheck(getDrawerCapacity() * (currentUpgradeMult - remMult + newMult));
        }
    }

    protected BlockEntityDrawers (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        drawerAttributes = new DrawerAttributes();

        injectData(materialData);
        injectData(upgradeData);
        injectData(controllerData);
        upgradeData.setDrawerAttributes(drawerAttributes);
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public UpgradeData upgrades() {
        return upgradeData;
    }

    public ControllerData controller() {
        return controllerData;
    }

    public IDrawerAttributesModifiable getDrawerAttributes() {
        return drawerAttributes;
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

    public int getDrawerCapacity() {
        return getGroup().getDrawer(0).getMaxCapacity();
    }

    public int getEffectiveDrawerCapacity() {
        return getDrawerCapacity() * upgrades().getStorageMultiplier();
    }

    public int getDrawerCount() {
        return getGroup().getDrawerCount();
    }

    private boolean stackCapacityCheck(int stackCapacity) {
        for (int i = 0; i < getDrawerCount(); i++) {
            IDrawer drawer = getDrawer(i);
            if (!drawer.isEnabled() || drawer.isEmpty())
                continue;

            int currentCount = drawer.getStoredItemCount();
            int newMaxCapacity = stackCapacity * drawer.getStoredItemStackSize();
            if (currentCount > newMaxCapacity)
                return false;
        }
        return true;
    }

    public boolean emptySlotCanBeCleared(int slot) {
        return false;
    }

    public void onAttributeChanged() {
        setChanged();
        markBlockForUpdate();
    }
    public void entityInside(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof ItemEntity itemEntity && !itemEntity.getItem().isEmpty() && !getDrawerAttributes().isSuspended()) {
            ItemStack stack = itemEntity.getItem().copy();
            interactPutItemsIntoSlot(-1, null);
        }
    }

    public boolean pushItemsTick(Level level, BlockPos pos, BlockState state) {
        return false;
    }

    public void interactPutItemsIntoSlot(int slot, Player player) {
        // Simplified
    }

    public ItemStack takeItemsFromSlot(int slot, int amount, Player player) {
        return ItemStack.EMPTY;
    }

    public boolean interactReplaceDrawer(int slot, ItemStack detachedDrawer, Player player) {
        return false;
    }

    public void validateBoundController() {
    }

    public void readPortable(HolderLookup.Provider registries, CompoundTag tag) {
        super.readPortable(registries, tag);
    }

    @Override
    public boolean setOwner(UUID owner) {
        this.owner = owner;
        return true;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public boolean setSecurityProvider(ISecurityProvider provider) {
        this.securityKey = provider != null ? provider.getProviderID() : null;
        return true;
    }

    public ISecurityProvider getSecurityProvider() {
        if (securityKey == null)
            return null;
        return ModSecurity.registry.getProvider(securityKey);
    }

    @Override
    public IFramedMaterials material() {
        return materialData;
    }

    @Override
    public boolean hasCustomName() {
        return name != null;
    }
    public Component getCustomName() {
        return name;
    }

    @Override
    public Component getName() {
        return getCustomName() != null ? getCustomName() : Component.translatable("block.vaultdrawers.drawers");
    }

    public void setCustomName(@Nullable Component name) {
        this.name = name;
    }

    public IDrawerGroup getGroup() {
        return this;
    }

    @Override
    public IDrawer getDrawer(int slot) {
        return getGroup().getDrawer(slot);
    }

    public IControlGroup getControlGroup() {
        return null;
    }

    @Override
    public int[] getAccessibleDrawerSlots() {
        int count = getGroup().getDrawerCount();
        int[] slots = new int[count];
        for (int i = 0; i < count; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public Set<IControlGroup> getSoftBoundControlGroups() {
        return softBoundControlGroups;
    }

    @Override
    public void softBindControlGroup(IControlGroup group) {
        softBoundControlGroups.add(group);
    }

    @Override
    public void unbindControlGroup() {
        softBoundControlGroups.clear();
    }

    @Override
    public boolean supportsDirectControllerLink() {
        return true;
    }

    public IControlGroup getBoundControlGroup() {
        BlockEntityController controller = controllerData.getController(this);
        if (controller != null)
            return controller.getControlGroup();
        return null;
    }

    public class ContentProvider implements ContentMenuProvider<PositionContent>
    {
        private final BlockEntityDrawers drawerEntity;

        public ContentProvider(BlockEntityDrawers drawerEntity) {
            this.drawerEntity = drawerEntity;
        }

        @Override
        public PositionContent createContent(ServerPlayer player) {
            return new PositionContent(drawerEntity.getBlockPos());
        }

        @Override
        public void openMenu(ServerPlayer player) {
            player.openMenu(this);
        }
        
        @Override
        public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
            return new com.stalkingdragons.minecraft.vaultdrawers.container.ContainerDrawers(null, containerId, playerInventory, drawerEntity);
        }
        
        @Override
        public Component getDisplayName() {
            return Component.translatable("container.vaultdrawers.drawers");
        }
    }
}