package com.stalkingdragons.minecraft.vaultdrawers.chameleon.service;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;
import net.minecraft.resources.Identifier;

public interface ChameleonCapabilities
{
    <T, C> ChameleonCapability<T> create(Identifier location, Class<T> clazz, Class<C> context);
}
