package com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.registry;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.api.ChameleonInit;
import net.neoforged.bus.api.IEventBus;

public class NeoforgeRegistryContext extends ChameleonInit.InitContext
{
    private final IEventBus eventBus;

    public NeoforgeRegistryContext (IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public IEventBus getEventBus () {
        return eventBus;
    }
}
