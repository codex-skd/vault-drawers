package com.stalkingdragons.minecraft.vaultdrawers.core;

import com.stalkingdragons.minecraft.vaultdrawers.api.IVaultDrawersApi;

public class Api implements IVaultDrawersApi
{
    public static IVaultDrawersApi instance;

    public Api () {
        instance = this;
    }
}