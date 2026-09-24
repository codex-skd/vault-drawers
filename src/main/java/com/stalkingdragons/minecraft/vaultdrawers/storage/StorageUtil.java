package com.stalkingdragons.minecraft.vaultdrawers.storage;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.*;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.BlockEntityController;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.util.ItemStackMatcher;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StorageUtil
{
    public static void rebalanceDrawers (IDrawerGroup group, int slot) {
        rebalanceDrawers(group, slot, null);
    }

    public static void rebalanceDrawers (IDrawerGroup group, int slot, Player player) {
        IDrawer drawer = group.getDrawer(slot);
        if (drawer.isEnabled())
            rebalanceDrawers(group, drawer.getStoredItemPrototype(), player);
    }

    public static void rebalanceDrawers (IDrawerGroup group, ItemStack stack) {
        rebalanceDrawers(group, stack, null);
    }

    public static void rebalanceDrawers (IDrawerGroup group, ItemStack stack, Player player) {
        if (stack.isEmpty())
            return;

        if (group instanceof INetworked networked && onNetwork(group)) {
            rebalanceDrawers(getRebalanceDrawers(networked, stack, player));
            return;
        }

        List<IDrawer> drawers = new ArrayList<>();
        for (int i = 0; i < group.getDrawerCount(); i++) {
            IDrawer drawer = group.getDrawer(i);
            if (!drawer.isEnabled())
                continue;

            if (ItemStackMatcher.areItemsEqual(drawer.getStoredItemPrototype(), stack))
                drawers.add(drawer);
        }

        rebalanceDrawers(drawers.stream());
    }

    private static boolean onNetwork (IDrawerGroup group) {
        if (!(group instanceof INetworked node))
            return false;

        IControlGroup bind = node.getBoundControlGroup();
        if (bind != null)
            return true;

        return !node.getSoftBoundControlGroups().isEmpty();
    }

    private static Stream<IDrawer> getRebalanceDrawers (INetworked node, ItemStack stack, Player player) {
        Set<IControlGroup> controllers = node.getSoftBoundControlGroups();
        if (controllers.isEmpty())
            return Stream.empty();

        if (controllers.size() == 1) {
            for (IControlGroup cg : controllers) {
                if (cg instanceof BlockEntityController controller)
                    return controller.getBalanceDrawers(stack, player);
            }
            return Stream.empty();
        }

        return controllers.stream().flatMap(cg -> {
            if (cg instanceof BlockEntityController controller)
                return controller.getBalanceDrawers(stack, player);
            return Stream.empty();
        });
    }

    public static void rebalanceDrawers (Stream<IDrawer> drawers, ItemStack stack) {
        if (stack.isEmpty())
            return;

        rebalanceDrawers(drawers.filter(d -> ItemStackMatcher.areItemsEqual(d.getStoredItemPrototype(), stack)));
    }

    public static void rebalanceDrawers (Stream<IDrawer> drawers) {
        if (!ModCommonConfig.INSTANCE.UPGRADES.balanceUpgrade.enableUpgrade.get())
            return;

        List<IDrawer> balanceDrawers = drawers.filter(IDrawer::isEnabled).toList();
        if (balanceDrawers.size() <= 1)
            return;

        int aggCount = balanceDrawers.stream().mapToInt(IDrawer::getStoredItemCount).sum();

        int drawerCount = balanceDrawers.size();
        int divCount = aggCount / drawerCount;
        int remCount = aggCount - (divCount * drawerCount);

        for (int i = 0; i < drawerCount; i++) {
            int slotCount = divCount;
            if (i < remCount)
                slotCount += 1;
            balanceDrawers.get(i).setStoredItemCount(slotCount);
        }
    }
}