package com.stalkingdragons.minecraft.vaultdrawers;

import com.stalkingdragons.minecraft.vaultdrawers.capabilities.PlatformCapabilities;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.registry.NeoforgeRegistryContext;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.service.ChameleonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.service.NeoforgeConfig;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.service.NeoforgeNetworking;
import com.stalkingdragons.minecraft.vaultdrawers.config.*;
import com.stalkingdragons.minecraft.vaultdrawers.core.*;
import com.stalkingdragons.minecraft.vaultdrawers.integration.LocalIntegrationRegistry;
import com.stalkingdragons.minecraft.vaultdrawers.network.PlayerBoolConfigMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Mod(VaultDrawers.MOD_ID)
public class VaultDrawers
{
    public static final String MOD_ID = "vault_drawers";
    public static Logger log = LogManager.getLogger();

    public VaultDrawers (ModContainer modContainer, IEventBus modEventBus) {
        ModCommonConfig.INSTANCE.context().init(ModConstants.MOD_ID, ChameleonConfig.Type.COMMON);
        ModClientConfig.INSTANCE.context().init(ModConstants.MOD_ID, ChameleonConfig.Type.CLIENT);
        modContainer.registerConfig(ModConfig.Type.COMMON, ((NeoforgeConfig) ModCommonConfig.INSTANCE.context()).neoSpec, "vault-drawers-common.v2.toml");
        modContainer.registerConfig(ModConfig.Type.CLIENT, ((NeoforgeConfig) ModClientConfig.INSTANCE.context()).neoSpec);

        NeoforgeRegistryContext regContext = new NeoforgeRegistryContext(modEventBus);

        ModBlocks.init(regContext);
        ModItems.init(regContext);
        ModBlockEntities.init(regContext);
        ModContainers.init(regContext);
        ModDataComponents.init(regContext);
        ModRecipes.init(regContext);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::loadComplete);
        modEventBus.addListener(this::onModConfigEvent);
        modEventBus.addListener(ModCreativeTabs::init);
        modEventBus.addListener(PlatformCapabilities::register);

        NeoforgeNetworking.init(MOD_ID, ModNetworking.INSTANCE, regContext);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new PlayerEventListener());
    }

    private void setup (final FMLCommonSetupEvent event) {
    }

    private void loadComplete (final FMLLoadCompleteEvent event) {
        // Runs on FMLLoadCompleteEvent + enqueueWork as an extra safety margin: the real fix
        // for the "Components not bound yet" crash these registries used to trigger was making
        // CompTierRegistry (and friends) store Item references instead of eagerly constructing
        // ItemStacks of vanilla items during mod loading — vanilla item Holders aren't
        // guaranteed to have their DataComponentMap bound until later in the lifecycle, so
        // ItemStack construction is now deferred (lazily, inside Record) until actual use.
        event.enqueueWork(() -> {
            CompTierRegistry.INSTANCE.initialize();
            StorageBlacklist.INSTANCE.initialize();
            MaterialBlacklist.INSTANCE.initialize();
            ConversionRegistry.INSTANCE.initialize();

            LocalIntegrationRegistry.initialize();
            LocalIntegrationRegistry.instance().init();
            LocalIntegrationRegistry.instance().postInit();
        });
    }

    private void onModConfigEvent(final ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON)
            ModCommonConfig.INSTANCE.setLoaded();
        if (event.getConfig().getType() == ModConfig.Type.CLIENT)
            ModClientConfig.INSTANCE.setLoaded();
    }

    @SubscribeEvent
    public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() || !(event.getEntity() instanceof Player))
            return;

        if (Minecraft.getInstance().player == null)
            return;

        UUID playerId = Minecraft.getInstance().player.getUUID();
        if (event.getEntity().getUUID() == playerId) {
            ChameleonServices.NETWORK.sendToServer(new PlayerBoolConfigMessage(playerId.toString(), "invertShift", ModClientConfig.INSTANCE.GENERAL.invertShift.get()));
            ChameleonServices.NETWORK.sendToServer(new PlayerBoolConfigMessage(playerId.toString(), "invertClick", ModClientConfig.INSTANCE.GENERAL.invertClick.get()));
        }
    }

    public static Identifier rl(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
