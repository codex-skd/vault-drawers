package com.stalkingdragons.minecraft.vaultdrawers.api.storage;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;

public interface IDrawerAttributesModifiable extends IDrawerAttributes
{
    default boolean setIsConcealed (boolean state) {
        return false;
    }

    default boolean setItemLocked (LockAttribute attr, boolean isLocked) {
        return false;
    }

    default boolean setIsShowingQuantity (boolean state) {
        return false;
    }

    default boolean setIsSealed (boolean state) {
        return false;
    }

    default boolean setPriority (int priority) {
        return false;
    }

    default boolean setIsVoid (boolean state) {
        return false;
    }

    default boolean setHasFillLevel (boolean state) {
        return false;
    }

    default boolean setIsUnlimitedStorage (boolean state) {
        return false;
    }

    default boolean setIsUnlimitedVending (boolean state) {
        return false;
    }

    default boolean setIsDictConvertible (boolean state) {
        return false;
    }

    default boolean setIsBalancedFill (boolean state) {
        return false;
    }

    default boolean setIsHopper (boolean state) {
        return false;
    }

    default boolean setIsMagnet (boolean state) {
        return false;
    }

    default boolean setIsSuspended (boolean state) { return false; }
}