package com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.registry;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.RegistryEntry;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NeoforgeRegistryEntry<R, T extends R> implements RegistryEntry<T>
{
    private final DeferredHolder<R, T> holder;

    public NeoforgeRegistryEntry(DeferredHolder<R, T> holder) {
        this.holder = holder;
    }

    @Override
    public Identifier getId () {
        return holder.getId();
    }

    @Override
    public T get () {
        return holder.get();
    }
}