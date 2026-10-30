package com.stalkingdragons.minecraft.vaultdrawers.chameleon.service;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.ChameleonRegistry;
import net.minecraft.core.Registry;

public interface ChameleonRegistries
{
    <T> ChameleonRegistry<T> create(Registry<T> registry, String id);
}
