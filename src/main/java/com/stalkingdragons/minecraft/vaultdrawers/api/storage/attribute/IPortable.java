package com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IPortable
{
    boolean isHeavy (HolderLookup.Provider provider, ItemStack stack);
}