package com.stalkingdragons.minecraft.vaultdrawers.api.render;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerGroup;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IRenderLabel
{
    void render (BlockEntity blockEntity, IDrawerGroup drawerGroup, int slot, float brightness, float partialTickTime);
}