package com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface RegistryEntry<T> extends Supplier<T>
{
    Identifier getId();
}
