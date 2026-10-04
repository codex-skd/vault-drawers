package com.stalkingdragons.minecraft.vaultdrawers.api.capabilities;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;

public interface IDrawerCapabilityProvider
{
    default <T> T getCapability(ChameleonCapability<T> capability) {
        return null;
    }
}