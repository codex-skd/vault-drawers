package com.stalkingdragons.minecraft.vaultdrawers.inventory;

import com.stalkingdragons.minecraft.vaultdrawers.components.item.DrawerCountData;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemStackHelper
{
    @NotNull
    public static ItemStack getItemPrototype (@NotNull ItemStack stack) {
        return stack.copy();
    }

    public static int getMaxStackSize(ItemStack stack) {
        return stack.getOrDefault(DataComponents.MAX_STACK_SIZE, stack.getItem().getDefaultMaxStackSize());
    }

    @NotNull
    public static ItemStack encodeItemStack (@NotNull ItemStack stack) {
        if (!stack.isEmpty())
            return stack;

        ItemStack proto = getItemPrototype(stack);
        if (proto.isEmpty())
            return stack;

        proto.set(ModDataComponents.DRAWER_COUNT.get(), new DrawerCountData(stack.getCount()));
        return proto;
    }

    public static ItemStack encodeItemStack (@NotNull ItemStack proto, int count) {
        if (!proto.isEmpty() && count > 0 && count < 128) {
            ItemStack stack = proto.copy();
            stack.setCount(count);
            return stack;
        }

        if (count == 0 || count >= 128) {
            ItemStack stack = proto.copy();
            stack.set(ModDataComponents.DRAWER_COUNT.get(), new DrawerCountData(count));
            return stack;
        }

        return proto.copy();
    }

    public static ItemStack decodeItemStack (@NotNull ItemStack stack) {
        int count = ItemStackHelper.decodedCount(stack);
        ItemStack decode = ItemStackHelper.stripDecoding(stack);
        decode.setCount(count);
        return decode;
    }

    public static ItemStack decodeItemStackPrototype (@NotNull ItemStack stack) {
        ItemStack decode = ItemStackHelper.stripDecoding(stack);
        decode.setCount(1);
        return decode;
    }

    public static int decodedCount (@NotNull ItemStack stack) {
        DrawerCountData data = stack.get(ModDataComponents.DRAWER_COUNT.get());
        if (data != null)
            return data.count();

        return stack.getCount();
    }

    public static ItemStack stripDecoding (@NotNull ItemStack stack) {
        ItemStack decode = stack.copy();
        decode.remove(ModDataComponents.DRAWER_COUNT.get());

        return decode;
    }

    public static boolean isStackEncoded (@NotNull ItemStack stack) {
        DrawerCountData data = stack.get(ModDataComponents.DRAWER_COUNT.get());
        return data != null;
    }

    @NotNull
    public static ItemStack parseOptional(HolderLookup.Provider provider, CompoundTag tag) {
        return ItemStack.OPTIONAL_CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
            .result().orElse(ItemStack.EMPTY);
    }

    @NotNull
    public static Optional<ItemStack> parseOptional(HolderLookup.Provider provider, Optional<CompoundTag> optTag) {
        return optTag.map(tag -> ItemStack.OPTIONAL_CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
            .result().orElse(ItemStack.EMPTY));
    }

    @NotNull
    public static ItemStack parse(HolderLookup.Provider provider, CompoundTag tag) {
        return ItemStack.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
            .result().orElse(ItemStack.EMPTY);
    }

    @NotNull
    public static Optional<ItemStack> parse(HolderLookup.Provider provider, Optional<CompoundTag> optTag) {
        return optTag.map(tag -> ItemStack.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
            .result().orElse(ItemStack.EMPTY));
    }
}