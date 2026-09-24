package com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.service;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.service.ChameleonCapabilities;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.capabilities.NeoforgeCapability;
import net.minecraft.resources.Identifier;

public class NeoforgeCapabilities implements ChameleonCapabilities
{
    @Override
    public <T, C> ChameleonCapability<T> create (Identifier location, Class<T> clazz, Class<C> context) {
        return new NeoforgeCapability<>(location, clazz, context);
    }
}
