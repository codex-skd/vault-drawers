package com.stalkingdragons.minecraft.vaultdrawers.block.tile;

import com.stalkingdragons.minecraft.vaultdrawers.api.capabilities.IItemRepository;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlockEntity;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.*;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.ControllerData;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import com.mojang.authlib.GameProfile;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class BlockEntityControllerIO extends BaseBlockEntity implements IDrawerGroup, IControlGroup, IFramedBlockEntity
{
    private static final int[] drawerSlots = new int[]{0};

    public final ControllerData controllerData = new ControllerData();
    public final MaterialData materialData = new MaterialData();

    public BlockEntityControllerIO (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        injectData(controllerData);
        injectPortableData(materialData);
    }

    public BlockEntityControllerIO (BlockPos pos, BlockState state) {
        this(ModBlockEntities.CONTROLLER_IO.get(), pos, state);
    }

    @Override
    public MaterialData material () {
        return materialData;
    }

    @Override
    public int getDrawerCount() {
        return 0;
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
        return null;
    }

    @Override
    public IControlGroup getBoundControlGroup() {
        return null;
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

    public BlockEntityController getController() {
        return controllerData.getController(this);
    }

    public com.stalkingdragons.minecraft.vaultdrawers.api.capabilities.IItemRepository getItemRepository() {
        BlockEntityController controller = getController();
        if (controller == null)
            return new com.stalkingdragons.minecraft.vaultdrawers.capabilities.DrawerItemRepository(this);
        return controller.getItemRepository();
    }

    public static BlockEntityType.BlockEntitySupplier<BlockEntityControllerIO> create() {
        return (pos, state) -> new BlockEntityControllerIO(ModBlockEntities.CONTROLLER_IO.get(), pos, state);
    }
}