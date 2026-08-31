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
import com.stalkingdragons.minecraft.vaultdrawers.inventory.ContainerDrawers1;
import com.stalkingdragons.minecraft.vaultdrawers.inventory.ContainerDrawers2;
import com.stalkingdragons.minecraft.vaultdrawers.inventory.ContainerDrawers4;
import com.stalkingdragons.minecraft.vaultdrawers.inventory.ContainerDrawersComp3;
import com.stalkingdragons.minecraft.vaultdrawers.storage.StorageUtil;
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

    public static IDrawerAttributes getDrawerAttributes(BlockEntityDrawers be) {
        if (be == null)
            return null;
        return be.getDrawerAttributes();
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
        Block block = getBlockState().getBlock();
        if (!(block instanceof BlockDrawers bd))
            return 0;

        return bd.getStorageUnits();
    }

    public int getEffectiveDrawerCapacity() {
        if (upgradeData.hasOneStackUpgrade())
            return 1;

        return getDrawerCapacity() * ModCommonConfig.INSTANCE.DRAWERS.baseStackStorage.get();
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

    protected boolean emptySlotCanBeCleared(int slot) {
        IDrawer drawer = BlockEntityDrawers.this.getGroup().getDrawer(slot);
        return !drawer.isEmpty() && drawer.getStoredItemCount() == 0;
    }

    public void onAttributeChanged() {
        setChanged();
        markBlockForUpdate();
    }
    public void entityInside(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (level.isClientSide())
            return;

        if (!(entity instanceof ItemEntity itementity))
            return;

        if (itementity.getItem().isEmpty() || !entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()).intersects(SUCK_AABB))
            return;

        addItemEntity(itementity);
    }

    public boolean isRedstone() {
        return upgradeData.getRedstoneType() != null;
    }

    public int getRedstoneLevel() {
        EnumUpgradeRedstone type = upgradeData.getRedstoneType();
        if (type == null)
            return 0;

        return switch (type) {
            case COMBINED -> getCombinedRedstoneLevel();
            case MAX -> getMaxRedstoneLevel();
            case MIN -> getMinRedstoneLevel();
        };
    }

    protected int getCombinedRedstoneLevel() {
        int active = 0;
        float fillRatio = 0;

        for (int i = 0; i < getDrawerCount(); i++) {
            IDrawer drawer = getDrawer(i);
            if (!drawer.isEnabled())
                continue;

            if (drawer.getMaxCapacity() > 0)
                fillRatio += ((float)drawer.getStoredItemCount() / drawer.getMaxCapacity());

            active++;
        }

        if (active == 0)
            return 0;

        if (fillRatio == active)
            return 15;

        return (int)Math.ceil((fillRatio / active) * 14);
    }

    protected int getMinRedstoneLevel() {
        float minRatio = 2;

        for (int i = 0; i < getDrawerCount(); i++) {
            IDrawer drawer = getDrawer(i);
            if (!drawer.isEnabled())
                continue;

            if (drawer.getMaxCapacity() > 0)
                minRatio = Math.min(minRatio, (float)drawer.getStoredItemCount() / drawer.getMaxCapacity());
            else
                minRatio = 0;
        }

        if (minRatio > 1)
            return 0;
        if (minRatio == 1)
            return 15;

        return (int)Math.ceil(minRatio * 14);
    }

    protected int getMaxRedstoneLevel() {
        float maxRatio = 0;

        for (int i = 0; i < getDrawerCount(); i++) {
            IDrawer drawer = getDrawer(i);
            if (!drawer.isEnabled())
                continue;

            if (drawer.getMaxCapacity() > 0)
                maxRatio = Math.max(maxRatio, (float)drawer.getStoredItemCount() / drawer.getMaxCapacity());
        }

        if (maxRatio == 1)
            return 15;

        return (int)Math.ceil(maxRatio * 14);
    }

    public boolean pushItemsTick(Level level, BlockPos pos, BlockState state) {
        IDrawerAttributes attr = getDrawerAttributes();
        if (attr.isSuspended())
            return false;
        if (!attr.isHopper() && !attr.isMagnet())
            return false;

        boolean added = suckInItems(level);
        if (added)
            setChanged(level, pos, state);

        return added;
    }

    private boolean suckInItems(Level level) {
        BlockPos pos = getBlockPos();
        BlockPos blockpos = BlockPos.containing(pos.getX(), pos.getY() + 1.0, pos.getZ());
        BlockState blockstate = level.getBlockState(blockpos);

        if (!upgradeData.hasMagnetUpgrade()) {
            if (blockstate.isCollisionShapeFullBlock(level, blockpos) && !blockstate.is(BlockTags.DOES_NOT_BLOCK_HOPPERS))
                return false;
        }

        for (ItemEntity item : getItemEntitiesInRange(level)) {
            if (addItemEntity(item))
                return true;
        }

        return false;
    }

    private List<ItemEntity> getItemEntitiesInRange(Level level) {
        BlockPos pos = getBlockPos();
        AABB aabb = (upgradeData.hasMagnetUpgrade() ? MAGNET_AABB : SUCK_AABB).move(pos);
        return level.getEntitiesOfClass(ItemEntity.class, aabb, EntitySelector.ENTITY_STILL_ALIVE);
    }

    private boolean addItemEntity(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem().copy();

        IDrawerGroup group = getGroup(this);

        for (int i = 0; i < group.getDrawerCount(); i++) {
            if (group.getDrawer(i).isEmpty()) {
                IDrawerAttributes attr = group.getCapability(Capabilities.DRAWER_ATTRIBUTES);
                if (attr != null && attr.isItemLocked(LockAttribute.LOCK_EMPTY))
                    continue;
            }

            putItemsIntoSlot(i, itemstack, itemstack.getCount(), null);
            if (itemstack.isEmpty())
                break;
        }

        if (itemstack.isEmpty()) {
            itemEntity.setItem(ItemStack.EMPTY);
            itemEntity.discard();
            return true;
        }

        if (itemEntity.getItem().getCount() != itemstack.getCount()) {
            itemEntity.setItem(itemstack);
            return true;
        }

        return false;
    }

    public int interactPutItemsIntoSlot(int slot, Player player) {
        if (getLevel() == null)
            return 0;

        int count;
        if (getLevel().getGameTime() - lastClickTime < 10 && player.getUUID().equals(lastClickUUID))
            count = interactPutCurrentInventoryIntoSlot(slot, player);
        else
            count = interactPutCurrentItemIntoSlot(slot, player);

        lastClickTime = getLevel().getGameTime();
        lastClickUUID = player.getUUID();

        return count;
    }

    public int putItemsIntoSlot(int slot, @NotNull ItemStack stack, int count, Player player) {
        IDrawer drawer = getGroup().getDrawer(slot);
        if (!drawer.isEnabled())
            return 0;

        if (!drawer.canItemBeStoredManual(stack, null))
            return 0;

        if (drawer.isEmpty())
            drawer = drawer.setStoredItem(stack);

        int countAdded = Math.min(count, stack.getCount());
        if (!drawerAttributes.isVoid())
            countAdded = Math.min(countAdded, drawer.getRemainingCapacity());

        drawer.setStoredItemCount(drawer.getStoredItemCount() + countAdded);
        stack.shrink(countAdded);

        if (upgradeData.hasbalancedFillUpgrade() && !upgradeData.hasVendingUpgrade() && !drawerAttributes.isSuspended())
            StorageUtil.rebalanceDrawers(getGroup(), slot, player);

        return countAdded;
    }

    public int interactPutCurrentItemIntoSlot(int slot, Player player) {
        IDrawer drawer = getDrawer(slot);
        if (!drawer.isEnabled())
            return 0;

        int count = 0;
        ItemStack playerStack = player.getInventory().getItem(player.getInventory().getSelectedSlot());
        if (!playerStack.isEmpty())
            count = putItemsIntoSlot(slot, playerStack, playerStack.getCount(), player);

        return count;
    }

    public int interactPutCurrentInventoryIntoSlot(int slot, Player player) {
        IDrawer drawer = getGroup().getDrawer(slot);
        if (!drawer.isEnabled())
            return 0;

        int count = 0;
        if (!drawer.isEmpty()) {
            for (int i = 0, n = Inventory.INVENTORY_SIZE; i < n; i++) {
                ItemStack subStack = player.getInventory().getItem(i);
                if (!subStack.isEmpty()) {
                    int subCount = putItemsIntoSlot(slot, subStack, subStack.getCount(), player);
                    if (subCount > 0 && subStack.getCount() == 0)
                        player.getInventory().setItem(i, ItemStack.EMPTY);

                    count += subCount;
                }
            }
        }

        return count;
    }

    public void clientUpdateCount (final int slot, final int count) {
        if (getLevel() == null || !getLevel().isClientSide())
            return;

        IDrawer drawer = getDrawer(slot);
        if (drawer.isEnabled() && drawer.getStoredItemCount() != count)
            drawer.setStoredItemCount(count);
    }

    protected void syncClientCount (int slot, int count) {
        if (!(getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel))
            return;

        com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices.NETWORK.sendToPlayersNear(
            new com.stalkingdragons.minecraft.vaultdrawers.network.CountUpdateMessage(getBlockPos(), slot, count),
            serverLevel, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 500);
    }

    public ItemStack takeItemsFromSlot(int slot, int amount, Player player) {
        IDrawer drawer = getGroup().getDrawer(slot);
        if (!drawer.isEnabled() || drawer.isEmpty())
            return ItemStack.EMPTY;

        ItemStack stack = drawer.getStoredItemPrototype().copy();
        stack.setCount(Math.min(amount, drawer.getStoredItemCount()));

        drawer.setStoredItemCount(drawer.getStoredItemCount() - stack.getCount());

        if (upgradeData.hasbalancedFillUpgrade() && !upgradeData.hasVendingUpgrade() && !drawerAttributes.isSuspended())
            StorageUtil.rebalanceDrawers(getGroup(), slot, player);

        if (isRedstone() && getLevel() != null) {
            getLevel().updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
            getLevel().updateNeighborsAt(getBlockPos().below(), getBlockState().getBlock());
        }

        return stack;
    }

    public boolean interactReplaceDrawer(int slot, ItemStack detachedDrawer, Player player) {
        IDrawer drawer = getDrawer(slot);
        if (!drawer.isMissing())
            return false;

        if (detachedDrawer.isEmpty())
            return false;

        DetachedDrawerContents contents = detachedDrawer.getOrDefault(ModDataComponents.DETACHED_DRAWER_CONTENTS.get(),
            DetachedDrawerContents.EMPTY);

        int count = contents.getItemCount();
        ItemStack proto = contents.getItemPrototype();

        if (count > drawer.getMaxCapacity(proto))
            return false;

        if (ModCommonConfig.INSTANCE.DRAWERS.detached.forceMaxCapacityCheck.get()) {
            int cap = getEffectiveDrawerCapacity() * upgradeData.getStorageMultiplier();
            if (contents.getStackLimit() < cap)
                return false;
        }

        drawer.setDetached(false);
        drawer.setStoredItem(proto, count);

        if (drawerAttributes.isBalancedFill() && !drawerAttributes.isSuspended())
            StorageUtil.rebalanceDrawers(getGroup(), slot, player);

        return true;
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

    public static IDrawerGroup getGroup(BlockEntityDrawers be) {
        if (be == null)
            return null;
        return be.getGroup();
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
            return switch (drawerEntity.getGroup().getDrawerCount()) {
                case 1 -> new ContainerDrawers1(containerId, playerInventory, drawerEntity);
                case 2 -> new ContainerDrawers2(containerId, playerInventory, drawerEntity);
                case 4 -> new ContainerDrawers4(containerId, playerInventory, drawerEntity);
                case 3 -> new ContainerDrawersComp3(containerId, playerInventory, drawerEntity);
                default -> null;
            };
        }
        
        @Override
        public Component getDisplayName() {
            return Component.translatable("container.vaultdrawers.drawers");
        }
    }
}