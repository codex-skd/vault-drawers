package com.stalkingdragons.minecraft.vaultdrawers.api.storage;

public interface IFractionalDrawer extends IDrawer
{
    int getConversionRate ();

    int getStoredItemRemainder ();

    boolean isSmallestUnit ();

    IFractionalDrawer copy ();
}