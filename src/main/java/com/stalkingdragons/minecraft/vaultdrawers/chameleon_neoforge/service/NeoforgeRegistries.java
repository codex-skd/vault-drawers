package com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.service;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.ChameleonRegistry;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.service.ChameleonRegistries;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.registry.NeoforgeRegistry;
import net.minecraft.core.Registry;

public class NeoforgeRegistries implements ChameleonRegistries
{
    @Override
    public <T> ChameleonRegistry<T> create (Registry<T> registry, String id) {
        return new NeoforgeRegistry<>(registry, id);
    }
}
