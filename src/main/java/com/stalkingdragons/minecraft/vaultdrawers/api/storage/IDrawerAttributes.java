package com.stalkingdragons.minecraft.vaultdrawers.api.storage;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.LockAttribute;

public interface IDrawerAttributes
{
    default boolean canItemLock (LockAttribute attr) {
        return false;
    }

    default boolean isItemLocked (LockAttribute attr) {
        return false;
    }

    default boolean isConcealed () {
        return false;
    }

    default boolean isSealed () {
        return false;
    }

    default boolean isShowingQuantity () {
        return false;
    }

    default int getPriority () {
        return 0;
    }

    default boolean isVoid () {
        return false;
    }

    default boolean hasFillLevel() {
        return false;
    }

    default boolean isUnlimitedStorage () {
        return false;
    }

    default boolean isUnlimitedVending () {
        return false;
    }

    default boolean isDictConvertible () {
        return false;
    }

    default boolean isBalancedFill () {
        return false;
    }

    default boolean isHopper () { return false; }

    default boolean isMagnet () { return false; }

    default boolean isSuspended () { return false; }
}