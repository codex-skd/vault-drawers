package com.stalkingdragons.minecraft.vaultdrawers.capabilities;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerGroup;
import net.neoforged.neoforge.items.IItemHandler;

public class PlatformDrawerItemHandler extends DrawerItemHandler implements IItemHandler
{
    public PlatformDrawerItemHandler (IDrawerGroup group) {
        super(group);
    }
}
