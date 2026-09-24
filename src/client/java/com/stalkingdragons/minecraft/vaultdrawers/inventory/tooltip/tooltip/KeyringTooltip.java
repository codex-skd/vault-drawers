package com.stalkingdragons.minecraft.vaultdrawers.inventory.tooltip;

import com.stalkingdragons.minecraft.vaultdrawers.components.item.KeyringContents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class KeyringTooltip implements TooltipComponent
{
    private final KeyringContents contents;

    public KeyringTooltip (KeyringContents contents) {
        this.contents = contents;
    }

    public KeyringContents getContents() {
        return contents;
    }

    @Override
    public int getWidth (net.minecraft.client.gui.Font font) {
        return 16 * contents.size();
    }

    @Override
    public int getHeight (net.minecraft.client.gui.Font font) {
        return 16;
    }

    @Override
    public void renderImage (net.minecraft.client.gui.Font font, int x, int y, net.minecraft.client.gui.GuiGraphics guiGraphics) {
        int i = 0;
        for (ItemStack stack : contents.items()) {
            guiGraphics.renderItem(stack, x + i * 16, y);
            i++;
        }
    }
}