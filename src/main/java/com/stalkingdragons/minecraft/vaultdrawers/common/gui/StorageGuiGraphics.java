package com.stalkingdragons.minecraft.vaultdrawers.common.gui;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simplified StorageGuiGraphics for common code - actual implementation is client-side only.
 * This is a placeholder for common code compilation.
 */
public class StorageGuiGraphics
{
    @NotNull
    public ItemStack overrideStack = ItemStack.EMPTY;

    public StorageGuiGraphics () {
        this.overrideStack = ItemStack.EMPTY;
    }

    public void renderItemDecorations(Object font, ItemStack item, int x, int y, @Nullable String text) {
        // Simplified - actual implementation is client-side only
    }
}