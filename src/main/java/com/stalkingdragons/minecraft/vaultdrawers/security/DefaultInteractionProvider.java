package com.stalkingdragons.minecraft.vaultdrawers.security;

import com.stalkingdragons.minecraft.vaultdrawers.api.security.IInteractionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class DefaultInteractionProvider implements IInteractionProvider
{
    @Override
    public String getProviderID () {
        return null;
    }

    @Override
    public boolean canInteract (Player player, InteractionHand hand, BlockPos pos) {
        return true;
    }

}