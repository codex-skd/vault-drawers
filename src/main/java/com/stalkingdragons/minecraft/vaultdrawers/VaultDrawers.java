package com.stalkingdragons.minecraft.vaultdrawers;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.registry.NeoforgeRegistryContext;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.service.ChameleonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.service.NeoforgeConfig;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(VaultDrawers.MOD_ID)
public class VaultDrawers
{
    public static final String MOD_ID = "vault_drawers";
    public static Logger log = LogManager.getLogger();

    public VaultDrawers (ModContainer modContainer, IEventBus modEventBus) {
        // Register configs (using chameleon's config system)
        // TODO: Add back config registration when config classes are ported

        NeoforgeRegistryContext regContext = new NeoforgeRegistryContext(modEventBus);

        // TODO: Add back registry initialization when classes are ported
        // ModBlocks.init(regContext);
        // ModItems.init(regContext);
        // ModBlockEntities.init(regContext);
        // ModContainers.init(regContext);
        // ModDataComponents.init(regContext);
        // ModRecipes.init(regContext);

        modEventBus.addListener(this::setup);

        NeoForge.EVENT_BUS.register(this);
    }

    private void setup (final FMLCommonSetupEvent event) {
        // TODO: Add back initialization when classes are ported
    }

    public static Identifier rl(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}