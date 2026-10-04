package com.stalkingdragons.minecraft.vaultdrawers.capabilities;

import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlockEntities;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.capabilities.NeoforgeCapability;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

public class PlatformCapabilities
{
    // Distinct id from Capabilities.ITEM_HANDLER (which wraps the mod's own chameleon
    // IItemHandler abstraction) — this one wraps NeoForge's native IItemHandler, replacing
    // the removed shared `Capabilities.ItemHandler.BLOCK` constant from pre-26.2 NeoForge.
    public static final ChameleonCapability<IItemHandler> ITEM_HANDLER =
        new NeoforgeCapability<>(VaultDrawers.rl("platform_item_handler"), IItemHandler.class, Direction.class);

    static <T, C> NeoforgeCapability<T, C> cast(ChameleonCapability<T> cap) {
        return (NeoforgeCapability<T, C>) cap;
    }

    public static void register (RegisterCapabilitiesEvent event) {
        ModBlockEntities.getDrawerTypes().forEach((entity) -> {
            cast(Capabilities.DRAWER_ATTRIBUTES).register(event, entity, (e, c) -> BlockEntityDrawers.getDrawerAttributes(e));
            cast(Capabilities.DRAWER_GROUP).register(event, entity, (e, c) -> BlockEntityDrawers.getGroup(e));
            cast(Capabilities.ITEM_REPOSITORY).register(event, entity, (e, c) -> new DrawerItemRepository(e));
            cast(Capabilities.ITEM_HANDLER).register(event, entity, (e, c) -> new DrawerItemHandler(e));

            cast(ITEM_HANDLER).register(event, entity, (e, c) -> new PlatformDrawerItemHandler(e));
        });

        cast(Capabilities.DRAWER_GROUP).register(event, ModBlockEntities.CONTROLLER.get(), (e, c) -> e);
        cast(Capabilities.ITEM_REPOSITORY).register(event, ModBlockEntities.CONTROLLER.get(), (e, c) -> e.getItemRepository());
        cast(Capabilities.ITEM_HANDLER).register(event, ModBlockEntities.CONTROLLER.get(), (e, c) -> new DrawerItemHandler(e));
        cast(ITEM_HANDLER).register(event, ModBlockEntities.CONTROLLER.get(), (e, c) -> new PlatformDrawerItemHandler(e));

        cast(Capabilities.DRAWER_GROUP).register(event, ModBlockEntities.CONTROLLER_IO.get(), (e, c) -> e);
        cast(Capabilities.ITEM_REPOSITORY).register(event, ModBlockEntities.CONTROLLER_IO.get(), (e, c) -> e.getItemRepository());
        cast(Capabilities.ITEM_HANDLER).register(event, ModBlockEntities.CONTROLLER_IO.get(), (e, c) -> new DrawerItemHandler(e));
        cast(ITEM_HANDLER).register(event, ModBlockEntities.CONTROLLER_IO.get(), (e, c) -> new PlatformDrawerItemHandler(e));
    }
}
