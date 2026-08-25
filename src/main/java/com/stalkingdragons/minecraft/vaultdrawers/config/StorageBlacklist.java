package com.stalkingdragons.minecraft.vaultdrawers.config;

import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

public class StorageBlacklist extends ConfigItemList
{
    public static final StorageBlacklist INSTANCE = new StorageBlacklist();

    @Override
    protected void innerInitialize () {
        ModCommonConfig.INSTANCE.onLoad(() -> ModCommonConfig.INSTANCE.DRAWERS.storage.storeBlacklist.get().forEach(this::register));
    }

    public boolean isBlacklisted (ItemStack stack) {
        return isListed(stack);
    }

    @Override
    protected void logRegisterNamespace (@NotNull String namespace) {
        if (ModCommonConfig.INSTANCE.GENERAL.logStartupActivity.get())
            VaultDrawers.log.info("New denied storage namespace " + namespace);
    }

    @Override
    protected void logRegisterItem (@NotNull ItemStack item) {
        if (ModCommonConfig.INSTANCE.GENERAL.logStartupActivity.get())
            VaultDrawers.log.info("New denied storage item " + item.getItem());
    }
}