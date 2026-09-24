package com.stalkingdragons.minecraft.vaultdrawers.api.storage;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;

import java.util.EnumSet;

public interface IDrawerAttributesGroupControl
{
    default boolean toggleConcealed () {
        return false;
    }

    default boolean setIsConcealed (boolean state) {
        return false;
    }

    default boolean toggleItemLocked (EnumSet<LockAttribute> attributes, LockAttribute attr) {
        return false;
    }

    default boolean setItemLocked (EnumSet<LockAttribute> attributes, LockAttribute attr, boolean isLocked) {
        return false;
    }

    default boolean toggleIsShowingQuantity () {
        return false;
    }

    default boolean setIsShowingQuantity (boolean state) {
        return false;
    }

    default boolean toggleIsSuspended () {
        return false;
    }

    default boolean setIsSuspended (boolean state) {
        return false;
    }
}