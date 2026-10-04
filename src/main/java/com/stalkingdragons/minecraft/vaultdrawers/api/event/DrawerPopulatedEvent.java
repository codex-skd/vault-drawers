package com.stalkingdragons.minecraft.vaultdrawers.api.event;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawer;
import net.neoforged.bus.api.Event;

public class DrawerPopulatedEvent extends Event
{
    public final IDrawer drawer;

    public DrawerPopulatedEvent (IDrawer drawer) {
        this.drawer = drawer;
    }
}