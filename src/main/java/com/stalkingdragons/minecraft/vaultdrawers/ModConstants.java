package com.stalkingdragons.minecraft.vaultdrawers;

import net.minecraft.resources.Identifier;

public final class ModConstants
{
    public static final String MOD_ID = "vault_drawers";

    public static Identifier loc(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
