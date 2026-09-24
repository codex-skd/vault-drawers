package com.stalkingdragons.minecraft.vaultdrawers.chameleon.inventory;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface ContentMenuProvider<C extends ContainerContent<C>> extends MenuProvider
{
    C createContent(ServerPlayer player);

    default void openMenu(ServerPlayer player) {
        ChameleonServices.CONTAINER.openMenu(player, this);
    }
}
