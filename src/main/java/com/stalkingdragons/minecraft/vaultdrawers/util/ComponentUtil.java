package com.stalkingdragons.minecraft.vaultdrawers.util;

import net.minecraft.world.item.Item;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ComponentUtil
{
    public static void appendSplitDescription (List<Component> tooltip, Item item) {
        String desc = item.getDescriptionId() + ".desc";
        Component component = Component.translatable(desc);
        String text = component.getString();

        if (text.equals(desc))
            return;

        String[] lines = text.split("\\|");
        for (String line : lines) {
            tooltip.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }
    }

    public static void appendSplitDescription (Consumer<Component> tooltip, Item item) {
        String desc = item.getDescriptionId() + ".desc";
        Component component = Component.translatable(desc);
        String text = component.getString();

        if (text.equals(desc))
            return;

        String[] lines = text.split("\\|");
        for (String line : lines) {
            tooltip.accept(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }
    }

    public static MutableComponent wrapInBrackets (Component component) {
        return Component.literal("[").append(component).append("]");
    }
}