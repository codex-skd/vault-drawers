package com.stalkingdragons.minecraft.vaultdrawers.core.recipe;

import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.UpgradeData;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModItems;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModRecipes;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemUpgrade;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AddUpgradeRecipe extends CustomRecipe
{
    private HolderLookup.Provider lastRegistries;

    public AddUpgradeRecipe () {
        super();
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
        lastRegistries = world.registryAccess();
        return findContext(inv, world.registryAccess()) != null;
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv) {
        HolderLookup.Provider registries = lastRegistries;
        Context ctx = findContext(inv, registries);
        if (ctx == null)
            return ItemStack.EMPTY;
        ItemStack ret = ctx.drawer.copy();

        TypedEntityData<BlockEntityType<?>> blockData = ret.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockData != null) {
            CompoundTag tag = blockData.getUnsafe();
            CompoundTag written = ctx.data.write(registries, tag);
            ret.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(blockData.type(), written));
            return ret;
        }

        CustomData upgradeData = ret.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CustomData data = CustomData.of(ctx.data.write(registries, upgradeData.copyTag()));
        ret.set(DataComponents.CUSTOM_DATA, data);

        return ret;
    }

    private static class Context {
        ItemStack drawer = ItemStack.EMPTY;
        List<ItemStack> upgrades = new ArrayList<>();
        UpgradeData data = null;
    }

    @Nullable
    private Context findContext(CraftingInput inv, HolderLookup.Provider registries) {
        Context ret = new Context();
        for (int x = 0; x < inv.size(); x++) {
            ItemStack stack = inv.getItem(x);
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof ItemDrawers) {
                if (!ret.drawer.isEmpty())
                    return null;
                ret.drawer = stack;
            } else if (stack.getItem() instanceof ItemUpgrade)
                ret.upgrades.add(stack);
            else
                return null;
        }

        if (ret.drawer.isEmpty() || ret.upgrades.isEmpty())
            return null;

        ret.data = new UpgradeData(7) {
            @Override
            public boolean setUpgrade(int slot, @NotNull ItemStack upgrade) {
                if (upgrade.isEmpty())
                    return false;
                upgrade = upgrade.copy();
                upgrade.setCount(1);
                super.upgrades[slot] = upgrade;
                return true;
            }
        };

        TypedEntityData<BlockEntityType<?>> blockEntityData = ret.drawer.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData != null)
            ret.data.read(registries, blockEntityData.getUnsafe());
        else {
            CustomData customData = ret.drawer.get(DataComponents.CUSTOM_DATA);
            if (customData != null)
                ret.data.read(registries, customData.copyTag());
        }

        for (ItemStack upgrade : ret.upgrades) {
            if (upgrade.getItem() == ModItems.ONE_STACK_UPGRADE.get())
                return null;
            if (!ret.data.hasEmptySlot() || !ret.data.canAddUpgrade(upgrade))
                return null;
            ret.data.addUpgrade(upgrade);
        }

        return ret;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends AddUpgradeRecipe> getSerializer() {
        return ModRecipes.UPGRADE_RECIPE_SERIALIZER.get();
    }
}
