package com.stalkingdragons.minecraft.vaultdrawers.api;

/**
 * Entry point for the public API.
 */
public class VaultDrawersApi
{
    private static IVaultDrawersApi instance;

    public static final String VERSION = "2.2.0";

    /**
     * API entry point.
     *
     * @return The {@link IVaultDrawersApi} instance or null if the API or Vault Drawers is unavailable.
     */
    public static IVaultDrawersApi instance () {
        if (instance == null) {
            try {
                Class<?> classApi = Class.forName( "com.stalkingdragons.minecraft.vaultdrawers.core.Api" );
                instance = (IVaultDrawersApi) classApi.getField("instance").get(null);
            }
            catch (Throwable t) {
                return null;
            }
        }

        return instance;
    }
}