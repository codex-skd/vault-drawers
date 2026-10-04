package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesModifiable;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemDrawerKey extends ItemKey
{
    public ItemDrawerKey (Properties properties) {
        super(properties);
    }

    @Override
    protected void handleDrawerAttributes (IDrawerAttributesModifiable attrs) {
        boolean locked = attrs.isItemLocked(LockAttribute.LOCK_POPULATED);
        attrs.setItemLocked(LockAttribute.LOCK_EMPTY, !locked);
        attrs.setItemLocked(LockAttribute.LOCK_POPULATED, !locked);
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.TOOLS.drawerKey.enable.get();
    }
}