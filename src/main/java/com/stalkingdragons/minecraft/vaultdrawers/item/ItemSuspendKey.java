package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesModifiable;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemSuspendKey extends ItemKey
{
    public ItemSuspendKey (Properties properties) {
        super(properties);
    }

    @Override
    protected void handleDrawerAttributes (IDrawerAttributesModifiable attrs) {
        attrs.setIsSuspended(!attrs.isSuspended());
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.TOOLS.suspendKey.enable.get();
    }
}