package com.stalkingdragons.minecraft.vaultdrawers.config;

import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MaterialBlacklist extends ConfigItemList
{
    public static final MaterialBlacklist INSTANCE = new MaterialBlacklist();

    @Override
    protected void innerInitialize () {
        ModCommonConfig.INSTANCE.onLoad(() -> ModCommonConfig.INSTANCE.DRAWERS.framed.materialBlacklist.get().forEach(this::register));
    }

    public boolean isBlacklisted (ItemStack stack) {
        return isListed(stack);
    }

    @Override
    protected void logRegisterNamespace (@NotNull String namespace) {
        if (ModCommonConfig.INSTANCE.GENERAL.logStartupActivity.get())
            VaultDrawers.log.info("New denied framing material namespace " + namespace);
    }

    @Override
    protected void logRegisterItem (@NotNull ItemStack item) {
        if (ModCommonConfig.INSTANCE.GENERAL.logStartupActivity.get())
            VaultDrawers.log.info("New denied framing material item " + item.getItem());
    }

}