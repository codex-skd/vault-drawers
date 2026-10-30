package com.stalkingdragons.minecraft.vaultdrawers.api.security;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IProtectable;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public interface IInteractionProvider
{
    String getProviderID ();

    boolean canInteract (Player player, InteractionHand hand, BlockPos pos);
}