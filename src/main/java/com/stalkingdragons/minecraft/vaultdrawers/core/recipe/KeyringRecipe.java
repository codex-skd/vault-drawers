package com.stalkingdragons.minecraft.vaultdrawers.core.recipe;

import com.stalkingdragons.minecraft.vaultdrawers.components.item.KeyringContents;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModRecipes;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemKey;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemKeyring;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KeyringRecipe extends CustomRecipe
{
    public KeyringRecipe () {
        super();
    }

    @Override
    public boolean matches (@NotNull CraftingInput inv, @NotNull Level level) {
        if (inv.size() < 9)
            return false;

        ItemStack center = inv.getItem(4);
        if (center.isEmpty() || !(center.getItem() instanceof ItemKey))
            return false;

        for (int i = 0; i < inv.size(); i++) {
            if (i == 4) continue;
            ItemStack stack = inv.getItem(i);
            if (i == 1 || i == 3 || i == 5 || i == 7) {
                if (!stack.is(Items.IRON_NUGGET))
                    return false;
            } else {
                if (!stack.isEmpty())
                    return false;
            }
        }

        return true;
    }

    @Override
    @NotNull
    public ItemStack assemble (@NotNull CraftingInput inv) {
        ItemStack center = inv.getItem(4);
        if (center.isEmpty() || !(center.getItem() instanceof ItemKey))
            return ItemStack.EMPTY;

        ItemStack result = ItemKeyring.getKeyring(center);
        if (result.isEmpty())
            return ItemStack.EMPTY;

        KeyringContents contents = result.get(ModDataComponents.KEYRING_CONTENTS.get());
        if (contents == null)
            contents = new KeyringContents(new ArrayList<>());

        KeyringContents.Mutable mutable = new KeyringContents.Mutable(contents);
        mutable.tryInsert(center.copy());
        result.set(ModDataComponents.KEYRING_CONTENTS.get(), mutable.toImmutable());

        return result;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends KeyringRecipe> getSerializer () {
        return ModRecipes.KEYRING_RECIPE_SERIALIZER.get();
    }
}
