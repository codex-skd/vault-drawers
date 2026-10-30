package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemUpgradeHopper extends ItemUpgrade
{
    public ItemUpgradeHopper (Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.UPGRADES.hopperUpgrade.enableUpgrade.get();
    }
}