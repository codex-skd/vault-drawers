package com.stalkingdragons.minecraft.vaultdrawers.api.storage;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface IDrawer
{
    @NotNull
    ItemStack getStoredItemPrototype ();

    @NotNull
    IDrawer setStoredItem (@NotNull ItemStack itemPrototype);

    @NotNull
    default IDrawer setStoredItem (@NotNull ItemStack itemPrototype, int amount) {
        IDrawer drawer = setStoredItem(itemPrototype);
        drawer.setStoredItemCount(amount);
        return drawer;
    }

    int getStoredItemCount ();

    void setStoredItemCount (int amount);

    default int adjustStoredItemCount (int amount) {
        if (amount > 0) {
            int insert = Math.min(amount, getRemainingCapacity());
            setStoredItemCount(getStoredItemCount() + insert);
            return amount - insert;
        } else if (amount < 0) {
            int stored = getStoredItemCount();
            int destroy = Math.min(Math.abs(amount), getStoredItemCount());
            setStoredItemCount(stored - destroy);
            return Math.abs(amount + destroy);
        } else {
            return 0;
        }
    }

    default int getMaxCapacity () {
        return getMaxCapacity(getStoredItemPrototype());
    }

    int getMaxCapacity (@NotNull ItemStack itemPrototype);

    default int getAcceptingMaxCapacity (@NotNull ItemStack itemPrototype) {
        return getMaxCapacity(itemPrototype);
    }

    int getRemainingCapacity ();

    default int getAcceptingRemainingCapacity () {
        return getRemainingCapacity();
    }

    default int getStoredItemStackSize () {
        @NotNull ItemStack protoStack = getStoredItemPrototype();
        if (protoStack.isEmpty())
            return 0;

        return protoStack.getItem().getDefaultMaxStackSize();
    }

    boolean canItemBeStored (@NotNull ItemStack itemPrototype, Predicate<ItemStack> matchPredicate);

    default boolean canItemBeStored (@NotNull ItemStack itemPrototype) {
        return canItemBeStored(itemPrototype, null);
    }

    default boolean canItemBeStoredManual (@NotNull ItemStack itemPrototype, Predicate<ItemStack> matchPredicate) {
        return canItemBeStored(itemPrototype, matchPredicate);
    }

    boolean canItemBeExtracted (@NotNull ItemStack itemPrototype, Predicate<ItemStack> matchPredicate);

    default boolean canItemBeExtracted (@NotNull ItemStack itemPrototype) {
        return canItemBeExtracted(itemPrototype, null);
    }

    boolean isEmpty ();

    default boolean isEnabled () {
        return !isMissing();
    }

    default boolean isMissing () {
        return false;
    }

    default boolean canDetach () {
        return false;
    }

    default void setDetached (boolean state) { }

    @NotNull
    default IDrawerAttributes getAttributes () {
        return EmptyDrawerAttributes.EMPTY;
    }

    IDrawer copy ();
}