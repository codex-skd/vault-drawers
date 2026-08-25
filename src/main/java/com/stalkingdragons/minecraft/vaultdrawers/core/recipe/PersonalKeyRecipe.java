package com.stalkingdragons.minecraft.vaultdrawers.core.recipe;

import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModRecipes;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModSecurity;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemPersonalKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PersonalKeyRecipe extends CustomRecipe
{
    public PersonalKeyRecipe () {
        super();
    }

    @Override
    public boolean matches (@NotNull CraftingInput craftingInput, @NotNull Level level) {
        ItemStack pkey = findPersonalKey(craftingInput);
        return !pkey.isEmpty();
    }

    private ItemStack findPersonalKey (CraftingInput craftingInput) {
        ItemStack pkey = ItemStack.EMPTY;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack item = craftingInput.getItem(i);
            if (item == ItemStack.EMPTY)
                continue;
            if (!pkey.isEmpty())
                return ItemStack.EMPTY;

            if (item.getItem() instanceof ItemPersonalKey pitem) {
                if (checkPersonalKey(pitem)) {
                    pkey = item;
                    continue;
                }
            }

            return ItemStack.EMPTY;
        }

        return pkey;
    }

    private boolean checkPersonalKey (ItemPersonalKey item) {
        String provider = item.getSecurityProviderKey();
        if (provider == null)
            provider = "default";

        if (provider.equals("default"))
            return true;
        if (provider.equals("ftb") && ftbEnabled())
            return true;

        return false;
    }

    private boolean ftbEnabled () {
        return ModSecurity.registry.getProvider("ftb") != null
            && ModCommonConfig.INSTANCE.INTEGRATION.ftbTeams.enableCycleRecipe.get();
    }

    @Override
    @NotNull
    public ItemStack assemble (@NotNull CraftingInput inv) {
        ItemStack pkey = findPersonalKey(inv);

        List<Item> cycle = new ArrayList<>();
        cycle.add(ModItems.PERSONAL_KEY.get());
        if (ftbEnabled())
            cycle.add(ModItems.PERSONAL_KEY_FTB.get());

        int index = cycle.indexOf(pkey.getItem());
        if (index == -1)
            return ItemStack.EMPTY;

        index += 1;
        if (index >= cycle.size())
            index = 0;

        return new ItemStack(cycle.get(index), 1);
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends PersonalKeyRecipe> getSerializer () {
        return ModRecipes.PERSONAL_KEY_RECIPE_SERIALIZER.get();
    }
}
