package com.stalkingdragons.minecraft.vaultdrawers.api.capabilities;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface IItemRepository
{
    @NotNull
    NonNullList<ItemRecord> getAllItems ();

    @NotNull
    ItemStack insertItem (@NotNull ItemStack stack, boolean simulate, Predicate<ItemStack> predicate);

    @NotNull
    default ItemStack insertItem (@NotNull ItemStack stack, boolean simulate) {
        return insertItem(stack, simulate, null);
    }

    @NotNull
    ItemStack extractItem (@NotNull ItemStack stack, int amount, boolean simulate, Predicate<ItemStack> predicate);

    @NotNull
    default ItemStack extractItem (@NotNull ItemStack stack, int amount, boolean simulate) {
        return extractItem(stack, amount, simulate, null);
    }

    default int getStoredItemCount (@NotNull ItemStack stack, Predicate<ItemStack> predicate) {
        ItemStack amount = extractItem(stack, Integer.MAX_VALUE, true, predicate);
        return amount.getCount();
    }

    default int getStoredItemCount (@NotNull ItemStack stack) {
        return getStoredItemCount(stack, null);
    }

    default int getRemainingItemCapacity (@NotNull ItemStack stack, Predicate<ItemStack> predicate) {
        stack = stack.copy();
        stack.setCount(Integer.MAX_VALUE);
        ItemStack remainder = insertItem(stack, true, predicate);
        return Integer.MAX_VALUE - remainder.getCount();
    }

    default int getRemainingItemCapacity (@NotNull ItemStack stack) {
        return getRemainingItemCapacity(stack, null);
    }

    default int getItemCapacity (@NotNull ItemStack stack, Predicate<ItemStack> predicate) {
        long capacity = (long) getStoredItemCount(stack, predicate) + getRemainingItemCapacity(stack, predicate);
        if (capacity > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (int)capacity;
    }

    default int getItemCapacity (@NotNull ItemStack stack) {
        return getItemCapacity(stack, null);
    }

    class ItemRecord
    {
        @NotNull
        public final ItemStack itemPrototype;
        public final int count;

        public ItemRecord (@NotNull ItemStack itemPrototype, int count) {
            this.itemPrototype = itemPrototype;
            this.count = count;
        }
    }

    interface DefaultPredicate<T> extends Predicate<T> { }
}