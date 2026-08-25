package com.stalkingdragons.minecraft.vaultdrawers.capabilities;

import com.stalkingdragons.minecraft.vaultdrawers.api.capabilities.IItemRepository;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerGroup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class DrawerItemRepository implements IItemRepository
{
    private final IDrawerGroup group;

    public DrawerItemRepository (IDrawerGroup group) {
        this.group = group;
    }

    @NotNull
    @Override
    public NonNullList<ItemRecord> getAllItems () {
        NonNullList<ItemRecord> list = NonNullList.create();
        for (int i = 0; i < group.getDrawerCount(); i++) {
            var drawer = group.getDrawer(i);
            if (!drawer.isEmpty()) {
                list.add(new ItemRecord(drawer.getStoredItemPrototype(), drawer.getStoredItemCount()));
            }
        }
        return list;
    }

    @NotNull
    @Override
    public ItemStack insertItem (@NotNull ItemStack stack, boolean simulate, Predicate<ItemStack> predicate) {
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem (@NotNull ItemStack stack, int amount, boolean simulate, Predicate<ItemStack> predicate) {
        return ItemStack.EMPTY;
    }
}