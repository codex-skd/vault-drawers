package com.stalkingdragons.minecraft.vaultdrawers.core;

import com.stalkingdragons.minecraft.vaultdrawers.ModConstants;
import com.stalkingdragons.minecraft.vaultdrawers.core.recipe.*;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.api.ChameleonInit;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.ChameleonRegistry;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.RegistryEntry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes
{
    private static final ChameleonRegistry<RecipeSerializer<?>> RECIPES = ChameleonServices.REGISTRY.create(BuiltInRegistries.RECIPE_SERIALIZER, ModConstants.MOD_ID);

    public static final RegistryEntry<RecipeSerializer<AddUpgradeRecipe>> UPGRADE_RECIPE_SERIALIZER = RECIPES.register("add_upgrade",
        () -> new RecipeSerializer<>(MapCodec.unit(AddUpgradeRecipe::new), StreamCodec.unit(new AddUpgradeRecipe())));
    public static final RegistryEntry<RecipeSerializer<KeyringRecipe>> KEYRING_RECIPE_SERIALIZER = RECIPES.register("keyring",
        () -> new RecipeSerializer<>(MapCodec.unit(KeyringRecipe::new), StreamCodec.unit(new KeyringRecipe())));
    public static final RegistryEntry<RecipeSerializer<RemoteGroupUpgradeRecipe>> REMOTE_GROUP_UPGRADE_SERIALIZER = RECIPES.register("remote_group_upgrade",
        () -> new RecipeSerializer<>(MapCodec.unit(RemoteGroupUpgradeRecipe::new), StreamCodec.unit(new RemoteGroupUpgradeRecipe())));
    public static final RegistryEntry<RecipeSerializer<UpgradeDetachedDrawerRecipe>> DETACHED_UPGRADE_RECIPE_SERIALIZER = RECIPES.register("add_detached_upgrade",
        () -> new RecipeSerializer<>(MapCodec.unit(UpgradeDetachedDrawerRecipe::new), StreamCodec.unit(new UpgradeDetachedDrawerRecipe())));
    public static final RegistryEntry<RecipeSerializer<PersonalKeyRecipe>> PERSONAL_KEY_RECIPE_SERIALIZER = RECIPES.register("personal_key_cycle",
        () -> new RecipeSerializer<>(MapCodec.unit(PersonalKeyRecipe::new), StreamCodec.unit(new PersonalKeyRecipe())));

    public static void init (ChameleonInit.InitContext context) {
        RECIPES.init(context);
    }
}
