package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ItemDrawerPuller extends ItemKey
{
    public ItemDrawerPuller (Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.DRAWERS.detached.enable.get();
    }
}