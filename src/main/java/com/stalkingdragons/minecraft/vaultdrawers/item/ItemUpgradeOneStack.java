package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;

public class ItemUpgradeOneStack extends ItemUpgrade
{
    public ItemUpgradeOneStack (Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.UPGRADES.oneStackUpgrade.enableUpgrade.get();
    }
}