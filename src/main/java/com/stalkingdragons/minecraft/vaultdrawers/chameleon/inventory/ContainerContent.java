package com.stalkingdragons.minecraft.vaultdrawers.chameleon.inventory;

public interface ContainerContent<T extends ContainerContent<T>>
{
    ContainerContentSerializer<T> serializer();
}
