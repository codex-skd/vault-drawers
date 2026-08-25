package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesModifiable;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemShroudKey extends ItemKey
{
    public ItemShroudKey (Properties properties) {
        super(properties);
    }

    @Override
    protected void handleDrawerAttributes (IDrawerAttributesModifiable attrs) {
        attrs.setIsConcealed(!attrs.isConcealed());
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.TOOLS.concealmentKey.enable.get();
    }
}