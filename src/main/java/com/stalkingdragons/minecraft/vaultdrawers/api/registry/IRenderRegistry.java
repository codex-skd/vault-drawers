package com.stalkingdragons.minecraft.vaultdrawers.api.registry;

import com.stalkingdragons.minecraft.vaultdrawers.api.render.IRenderLabel;

public interface IRenderRegistry
{
    void registerPreLabelRenderHandler (IRenderLabel renderHandler);
}