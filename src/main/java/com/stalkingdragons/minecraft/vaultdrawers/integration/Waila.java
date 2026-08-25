package com.stalkingdragons.minecraft.vaultdrawers.integration;

import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModClientConfig;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.impl.ui.ItemStackElement;

@WailaPlugin(VaultDrawers.MOD_ID)
public class Waila implements IWailaPlugin
{
    @Override
    public void registerClient (IWailaClientRegistration registration) {
        if (!ModCommonConfig.INSTANCE.INTEGRATION.waila.enable.get()
            || !ModClientConfig.INSTANCE.INTEGRATION.enableWaila.get())
            return;

        registration.addConfig(VaultDrawers.rl("display.content"), true);
        registration.addConfig(VaultDrawers.rl("display.stacklimit"), true);
        registration.addConfig(VaultDrawers.rl("display.status"), true);

        WailaDrawer provider = new WailaDrawer();
        registration.registerBlockComponent(provider, BlockDrawers.class);
    }

    public static class WailaDrawer implements IBlockComponentProvider
    {
        @Override
        @NotNull
        public Element getIcon (BlockAccessor accessor, IPluginConfig config, Element currentIcon) {
            return ItemStackElement.of(new ItemStack(accessor.getBlock()));
        }

        @Override
        public void appendTooltip (ITooltip currenttip, BlockAccessor accessor, IPluginConfig config) {
            BlockEntityDrawers blockEntityDrawers = (BlockEntityDrawers) accessor.getBlockEntity();

            DrawerOverlay overlay = new DrawerOverlay();
            overlay.showContent = config.get(VaultDrawers.rl("display.content"));
            overlay.showStackLimit = config.get(VaultDrawers.rl("display.stacklimit"));
            overlay.showStatus = config.get(VaultDrawers.rl("display.status"));

            currenttip.addAll(overlay.getOverlay(blockEntityDrawers));
        }

        @Override
        public Identifier getUid () {
            return Identifier.fromNamespaceAndPath(VaultDrawers.MOD_ID, "main");
        }
    }
}
