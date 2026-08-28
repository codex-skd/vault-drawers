package com.stalkingdragons.minecraft.vaultdrawers.integration;

import net.neoforged.fml.ModList;

public abstract class IntegrationModule
{
    public abstract String getModID ();

    public boolean versionCheck () {
        return true;
    }

    public abstract void init () throws Throwable;

    public abstract void postInit ();
}
