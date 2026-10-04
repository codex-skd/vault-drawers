package com.stalkingdragons.minecraft.vaultdrawers.core.recipe;

import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RemoteGroupUpgradeRecipe extends CustomRecipe
{
    public RemoteGroupUpgradeRecipe () {
        super();
    }

    @Override
    public boolean matches (@NotNull CraftingInput inv, @NotNull Level level) {
        if (inv.size() < 3)
            return false;

        boolean foundUpgrade = false;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty())
                continue;
            if (stack.getItem() == ModItems.REMOTE_UPGRADE_BOUND.get()) {
                if (foundUpgrade)
                    return false;
                foundUpgrade = true;
            } else if (stack.is(Items.ENDER_PEARL)) {
                continue;
            } else {
                return false;
            }
        }

        return foundUpgrade;
    }

    @Override
    @NotNull
    public ItemStack assemble (@NotNull CraftingInput inv) {
        ItemStack center = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() == ModItems.REMOTE_UPGRADE_BOUND.get()) {
                center = stack;
                break;
            }
        }

        if (center.isEmpty() || center.getItem() != ModItems.REMOTE_UPGRADE_BOUND.get())
            return ItemStack.EMPTY;

        ItemStack result = new ItemStack(ModItems.REMOTE_GROUP_UPGRADE_BOUND.get());
        result.set(ModDataComponents.CONTROLLER_BINDING.get(), center.get(ModDataComponents.CONTROLLER_BINDING.get()));

        return result;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends RemoteGroupUpgradeRecipe> getSerializer () {
        return ModRecipes.REMOTE_GROUP_UPGRADE_SERIALIZER.get();
    }
}
