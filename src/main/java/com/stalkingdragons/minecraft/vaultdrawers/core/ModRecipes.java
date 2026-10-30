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

    private static final AddUpgradeRecipe ADD_UPGRADE = new AddUpgradeRecipe();
    private static final KeyringRecipe KEYRING = new KeyringRecipe();
    private static final RemoteGroupUpgradeRecipe REMOTE_GROUP_UPGRADE = new RemoteGroupUpgradeRecipe();
    private static final UpgradeDetachedDrawerRecipe DETACHED_UPGRADE = new UpgradeDetachedDrawerRecipe();
    private static final PersonalKeyRecipe PERSONAL_KEY = new PersonalKeyRecipe();

    public static final RegistryEntry<RecipeSerializer<AddUpgradeRecipe>> UPGRADE_RECIPE_SERIALIZER = RECIPES.register("add_upgrade",
        () -> new RecipeSerializer<>(MapCodec.unit(ADD_UPGRADE), StreamCodec.unit(ADD_UPGRADE)));
    public static final RegistryEntry<RecipeSerializer<KeyringRecipe>> KEYRING_RECIPE_SERIALIZER = RECIPES.register("keyring",
        () -> new RecipeSerializer<>(MapCodec.unit(KEYRING), StreamCodec.unit(KEYRING)));
    public static final RegistryEntry<RecipeSerializer<RemoteGroupUpgradeRecipe>> REMOTE_GROUP_UPGRADE_SERIALIZER = RECIPES.register("remote_group_upgrade",
        () -> new RecipeSerializer<>(MapCodec.unit(REMOTE_GROUP_UPGRADE), StreamCodec.unit(REMOTE_GROUP_UPGRADE)));
    public static final RegistryEntry<RecipeSerializer<UpgradeDetachedDrawerRecipe>> DETACHED_UPGRADE_RECIPE_SERIALIZER = RECIPES.register("add_detached_upgrade",
        () -> new RecipeSerializer<>(MapCodec.unit(DETACHED_UPGRADE), StreamCodec.unit(DETACHED_UPGRADE)));
    public static final RegistryEntry<RecipeSerializer<PersonalKeyRecipe>> PERSONAL_KEY_RECIPE_SERIALIZER = RECIPES.register("personal_key_cycle",
        () -> new RecipeSerializer<>(MapCodec.unit(PERSONAL_KEY), StreamCodec.unit(PERSONAL_KEY)));

    public static void init (ChameleonInit.InitContext context) {
        RECIPES.init(context);
    }
}
