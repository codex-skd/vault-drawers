package com.stalkingdragons.minecraft.vaultdrawers.api.registry;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawer;

public interface IWailaTooltipHandler
{
    String transformItemName (IDrawer drawer, String defaultName);
}