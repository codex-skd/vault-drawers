package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemUpgradeFillLevel extends ItemUpgrade
{
    public ItemUpgradeFillLevel (Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.UPGRADES.fillLevelUpgrade.enableUpgrade.get();
    }
}