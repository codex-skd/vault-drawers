package com.stalkingdragons.minecraft.vaultdrawers.api.storage;

import com.stalkingdragons.minecraft.vaultdrawers.api.capabilities.IDrawerCapabilityProvider;
import org.jetbrains.annotations.NotNull;

public interface IDrawerGroup extends IDrawerCapabilityProvider
{
    int getDrawerCount ();

    @NotNull
    IDrawer getDrawer (int slot);

    int[] getAccessibleDrawerSlots ();

    default boolean isGroupValid () {
        return true;
    };

    default boolean hasMissingDrawers () {
        for (int i = 0; i < getDrawerCount(); i++) {
            if (getDrawer(i).isMissing())
                return true;
        }

        return false;
    }
}