package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemUpgradeIllumination extends ItemUpgrade
{
    public ItemUpgradeIllumination (Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.UPGRADES.illuminationUpgrade.enableUpgrade.get();
    }
}