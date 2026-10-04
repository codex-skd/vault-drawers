package com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.service;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.service.ChameleonPlatform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class NeoforgePlatform implements ChameleonPlatform
{
    @Override
    public boolean isPhysicalClient () {
        // In Minecraft 26.2, FMLEnvironment.dist may not exist or may have changed
        // Just return true (client) for now - this is mainly used for client-side logic
        return true;
    }
}
