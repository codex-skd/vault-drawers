package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesModifiable;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemQuantifyKey extends ItemKey
{
    public ItemQuantifyKey (Properties properties) {
        super(properties);
    }

    @Override
    protected void handleDrawerAttributes (IDrawerAttributesModifiable attrs) {
        attrs.setIsShowingQuantity(!attrs.isShowingQuantity());
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.TOOLS.quantifyKey.enable.get();
    }
}