package com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute;

import com.stalkingdragons.minecraft.vaultdrawers.api.security.ISecurityProvider;

import java.util.UUID;

public interface IProtectable
{
    UUID getOwner ();

    boolean setOwner (UUID owner);

    ISecurityProvider getSecurityProvider ();

    boolean setSecurityProvider (ISecurityProvider provder);
}