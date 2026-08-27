package com.stalkingdragons.minecraft.vaultdrawers.integration;

import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import net.neoforged.fml.ModList;

public class LocalIntegrationRegistry
{
    private static LocalIntegrationRegistry instance;

    public static void initialize () {
        IntegrationRegistry reg = instance();
        if (ModList.get().isLoaded("jade") && ModCommonConfig.INSTANCE.INTEGRATION.jade.enable.get()) {
            reg.add(new JadeModule());
        }
    }

    private final IntegrationRegistry registry;

    private LocalIntegrationRegistry () {
        registry = new IntegrationRegistry(VaultDrawers.MOD_ID);
    }

    public static boolean isModLoaded (String modid) {
        if (instance == null)
            instance = new LocalIntegrationRegistry();

        return instance.registry.isModLoaded(modid);
    }

    public static IntegrationRegistry instance () {
        if (instance == null)
            instance = new LocalIntegrationRegistry();

        return instance.registry;
    }

    private static class JadeModule extends IntegrationModule {
        @Override
        public String getModID () {
            return "jade";
        }

        @Override
        public void init () throws Throwable {
            // Jade registration happens via @WailaPlugin annotation on Jade class
            // No additional init needed
        }

        @Override
        public void postInit () {
            // No post-init needed
        }
    }
}
