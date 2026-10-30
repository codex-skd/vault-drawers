package com.stalkingdragons.minecraft.vaultdrawers.inventory.tooltip;

import com.stalkingdragons.minecraft.vaultdrawers.components.item.DetachedDrawerContents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class DetachedDrawerTooltip implements TooltipComponent
{
    private final DetachedDrawerContents contents;

    public DetachedDrawerTooltip (DetachedDrawerContents contents) {
        this.contents = contents;
    }

    public DetachedDrawerContents getContents() {
        return contents;
    }

    @Override
    public int getWidth (net.minecraft.client.gui.Font font) {
        return 16;
    }

    @Override
    public int getHeight (net.minecraft.client.gui.Font font) {
        return 16;
    }

    @Override
    public void renderImage (net.minecraft.client.gui.Font font, int x, int y, net.minecraft.client.gui.GuiGraphics guiGraphics) {
        ItemStack stack = contents.getItem();
        if (!stack.isEmpty()) {
            guiGraphics.renderItem(stack, x, y);
        }
    }
}