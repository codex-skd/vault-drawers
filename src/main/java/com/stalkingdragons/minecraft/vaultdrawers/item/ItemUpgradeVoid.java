package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemUpgradeVoid extends ItemUpgrade
{
    public ItemUpgradeVoid (Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.UPGRADES.voidUgrade.enableUpgrade.get();
    }
}