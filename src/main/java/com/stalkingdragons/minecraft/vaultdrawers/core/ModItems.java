package com.stalkingdragons.minecraft.vaultdrawers.core;

import com.stalkingdragons.minecraft.vaultdrawers.ModConstants;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.BlockTrim;
import com.stalkingdragons.minecraft.vaultdrawers.block.framed.BlockFramedStandardDrawers;
import com.stalkingdragons.minecraft.vaultdrawers.block.framed.BlockFramedTrim;
import com.stalkingdragons.minecraft.vaultdrawers.block.meta.BlockMeta;
import com.stalkingdragons.minecraft.vaultdrawers.item.*;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.api.ChameleonInit;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.ChameleonRegistry;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ModItems
{
    public static final ChameleonRegistry<Item> ITEMS = ChameleonServices.REGISTRY.create(BuiltInRegistries.ITEM, ModConstants.MOD_ID);

    public static final List<RegistryEntry<? extends Item>> EXCLUDE_ITEMS_CREATIVE_TAB = new ArrayList<>();

    public static final RegistryEntry<? extends Item>
        OBSIDIAN_STORAGE_UPGRADE = ITEMS.register("obsidian_storage_upgrade", () -> new ItemUpgradeStorage(EnumUpgradeStorage.OBSIDIAN, setId(new Item.Properties(), "obsidian_storage_upgrade"))),
        COPPER_STORAGE_UPGRADE = ITEMS.register("copper_storage_upgrade", () -> new ItemUpgradeStorage(EnumUpgradeStorage.COPPER, setId(new Item.Properties(), "copper_storage_upgrade"))),
        IRON_STORAGE_UPGRADE = ITEMS.register("iron_storage_upgrade", () -> new ItemUpgradeStorage(EnumUpgradeStorage.IRON, setId(new Item.Properties(), "iron_storage_upgrade"))),
        GOLD_STORAGE_UPGRADE = ITEMS.register("gold_storage_upgrade", () -> new ItemUpgradeStorage(EnumUpgradeStorage.GOLD, setId(new Item.Properties(), "gold_storage_upgrade"))),
        EMERALD_STORAGE_UPGRADE = ITEMS.register("emerald_storage_upgrade", () -> new ItemUpgradeStorage(EnumUpgradeStorage.EMERALD, setId(new Item.Properties(), "emerald_storage_upgrade"))),
        DIAMOND_STORAGE_UPGRADE = ITEMS.register("diamond_storage_upgrade", () -> new ItemUpgradeStorage(EnumUpgradeStorage.DIAMOND, setId(new Item.Properties(), "diamond_storage_upgrade"))),
        NETHERITE_STORAGE_UPGRADE = ITEMS.register("netherite_storage_upgrade", () -> new ItemUpgradeStorage(EnumUpgradeStorage.NETHERITE, setId(new Item.Properties(), "netherite_storage_upgrade"))),
        ONE_STACK_UPGRADE = ITEMS.register("one_stack_upgrade", () -> new ItemUpgradeOneStack(setId(new Item.Properties(), "one_stack_upgrade"))),
        VOID_UPGRADE = ITEMS.register("void_upgrade", () -> new ItemUpgradeVoid(setId(new Item.Properties(), "void_upgrade"))),
        CREATIVE_STORAGE_UPGRADE = ITEMS.register("creative_storage_upgrade", () -> new ItemUpgradeCreative(EnumUpgradeCreative.STORAGE, setId(new Item.Properties(), "creative_storage_upgrade"))),
        CREATIVE_VENDING_UPGRADE = ITEMS.register("creative_vending_upgrade", () -> new ItemUpgradeCreative(EnumUpgradeCreative.VENDING, setId(new Item.Properties(), "creative_vending_upgrade"))),
        CONVERSION_UPGRADE = ITEMS.register("conversion_upgrade", () -> new ItemUpgradeConversion(setId(new Item.Properties(), "conversion_upgrade"))),
        REDSTONE_UPGRADE = ITEMS.register("redstone_upgrade", () -> new ItemUpgradeRedstone(EnumUpgradeRedstone.COMBINED, setId(new Item.Properties(), "redstone_upgrade"))),
        MIN_REDSTONE_UPGRADE = ITEMS.register("min_redstone_upgrade", () -> new ItemUpgradeRedstone(EnumUpgradeRedstone.MIN, setId(new Item.Properties(), "min_redstone_upgrade"))),
        MAX_REDSTONE_UPGRADE = ITEMS.register("max_redstone_upgrade", () -> new ItemUpgradeRedstone(EnumUpgradeRedstone.MAX, setId(new Item.Properties(), "max_redstone_upgrade"))),
        ILLUMINATION_UPGRADE = ITEMS.register("illumination_upgrade", () -> new ItemUpgradeIllumination(setId(new Item.Properties(), "illumination_upgrade"))),
        FILL_LEVEL_UPGRADE = ITEMS.register("fill_level_upgrade", () -> new ItemUpgradeFillLevel(setId(new Item.Properties(), "fill_level_upgrade"))),
        BALANCE_FILL_UPGRADE = ITEMS.register("balance_fill_upgrade", () -> new ItemUpgradeBalance(setId(new Item.Properties(), "balance_fill_upgrade"))),
        PORTABILITY_UPGRADE = ITEMS.register("portability_upgrade", () -> new ItemUpgradePortability(setId(new Item.Properties(), "portability_upgrade"))),
        HOPPER_UPGRADE = ITEMS.register("hopper_upgrade", () -> new ItemUpgradeHopper(setId(new Item.Properties(), "hopper_upgrade"))),
        MAGNET_UPGRADE = ITEMS.register("magnet_upgrade", () -> new ItemUpgradeMagnet(EnumUpgradeMagnet.LEVEL1, setId(new Item.Properties(), "magnet_upgrade"))),
        MAGNET_UPGRADE_2 = ITEMS.register("magnet_upgrade_2", () -> new ItemUpgradeMagnet(EnumUpgradeMagnet.LEVEL2, setId(new Item.Properties(), "magnet_upgrade_2"))),
        MAGNET_UPGRADE_3 = ITEMS.register("magnet_upgrade_3", () -> new ItemUpgradeMagnet(EnumUpgradeMagnet.LEVEL3, setId(new Item.Properties(), "magnet_upgrade_3"))),
        REMOTE_UPGRADE = ITEMS.register("remote_upgrade", () -> new ItemUpgradeRemote(false, false, setId(new Item.Properties(), "remote_upgrade"))),
        REMOTE_UPGRADE_BOUND = ITEMS.register("remote_upgrade_bound", () -> new ItemUpgradeRemote(false, true, setId(new Item.Properties(), "remote_upgrade_bound"))),
        REMOTE_GROUP_UPGRADE = ITEMS.register("remote_group_upgrade", () -> new ItemUpgradeRemote(true, false, setId(new Item.Properties(), "remote_group_upgrade"))),
        REMOTE_GROUP_UPGRADE_BOUND = ITEMS.register("remote_group_upgrade_bound", () -> new ItemUpgradeRemote(true, true, setId(new Item.Properties(), "remote_group_upgrade_bound"))),
        UPGRADE_TEMPLATE = ITEMS.register("upgrade_template", () -> new Item(setId(new Item.Properties(), "upgrade_template"))),
        DETACHED_DRAWER = ITEMS.register("detached_drawer", () -> new ItemDetachedDrawer(setId(new Item.Properties(), "detached_drawer"))),
        DETACHED_DRAWER_FULL = ITEMS.register("detached_drawer_full", () -> new ItemDetachedDrawer(setId(new Item.Properties().stacksTo(1), "detached_drawer_full")));

    public static final RegistryEntry<? extends ItemKey>
        DRAWER_KEY = ITEMS.register("drawer_key", () -> new ItemDrawerKey(setId(new Item.Properties(), "drawer_key"))),
        QUANTIFY_KEY = ITEMS.register("quantify_key", () -> new ItemQuantifyKey(setId(new Item.Properties(), "quantify_key"))),
        SHROUD_KEY = ITEMS.register("shroud_key", () -> new ItemShroudKey(setId(new Item.Properties(), "shroud_key"))),
        PERSONAL_KEY = ITEMS.register("personal_key", () -> new ItemPersonalKey(null, setId(new Item.Properties(), "personal_key"))),
        PERSONAL_KEY_COFH = ITEMS.register("personal_key_cofh", () -> new ItemPersonalKey("cofh", setId(new Item.Properties(), "personal_key_cofh"))),
        PERSONAL_KEY_FTB = ITEMS.register("personal_key_ftb", () -> new ItemPersonalKey("ftb", setId(new Item.Properties(), "personal_key_ftb"))),
        PERSONAL_KEY_UNLOCK = ITEMS.register("personal_key_unlock", () -> new ItemPersonalKey("unlock", setId(new Item.Properties(), "personal_key_unlock"))),
        SUSPEND_KEY = ITEMS.register("suspend_key", () -> new ItemSuspendKey(setId(new Item.Properties(), "suspend_key"))),
        PRIORITY_KEY = ITEMS.register("priority_key", () -> new ItemPriorityKey(0, 1, setId(new Item.Properties(), "priority_key"))),
        PRIORITY_KEY_P1 = ITEMS.register("priority_key_p1", () -> new ItemPriorityKey(1, 2, setId(new Item.Properties(), "priority_key_p1"))),
        PRIORITY_KEY_P2 = ITEMS.register("priority_key_p2", () -> new ItemPriorityKey(2, -1, setId(new Item.Properties(), "priority_key_p2"))),
        PRIORITY_KEY_N1 = ITEMS.register("priority_key_n1", () -> new ItemPriorityKey(-1, -2, setId(new Item.Properties(), "priority_key_n1"))),
        PRIORITY_KEY_N2 = ITEMS.register("priority_key_n2", () -> new ItemPriorityKey(-2, 0, setId(new Item.Properties(), "priority_key_n2"))),
        DRAWER_PULLER = ITEMS.register("drawer_puller", () -> new ItemDrawerPuller(setId(new Item.Properties(), "drawer_puller")));

    public static final RegistryEntry<? extends ItemKeyring>
        KEYRING = ITEMS.register("keyring", () -> new ItemKeyring(null, setId(new Item.Properties().stacksTo(1), "keyring"))),
        KEYRING_DRAWER = ITEMS.register("keyring_drawer", () -> new ItemKeyring(DRAWER_KEY, setId(new Item.Properties().stacksTo(1), "keyring_drawer"))),
        KEYRING_QUANTIFY = ITEMS.register("keyring_quantify", () -> new ItemKeyring(QUANTIFY_KEY, setId(new Item.Properties().stacksTo(1), "keyring_quantify"))),
        KEYRING_SHROUD = ITEMS.register("keyring_shroud", () -> new ItemKeyring(SHROUD_KEY, setId(new Item.Properties().stacksTo(1), "keyring_shroud"))),
        KEYRING_PERSONAL = ITEMS.register("keyring_personal", () -> new ItemKeyring(PERSONAL_KEY, setId(new Item.Properties().stacksTo(1), "keyring_personal"))),
        KEYRING_PERSONAL_COFH = ITEMS.register("keyring_personal_cofh", () -> new ItemKeyring(PERSONAL_KEY_COFH, setId(new Item.Properties().stacksTo(1), "keyring_personal_cofh"))),
        KEYRING_PERSONAL_FTB = ITEMS.register("keyring_personal_ftb", () -> new ItemKeyring(PERSONAL_KEY_FTB, setId(new Item.Properties().stacksTo(1), "keyring_personal_ftb"))),
        KEYRING_PERSONAL_UNLOCK = ITEMS.register("keyring_personal_unlock", () -> new ItemKeyring(PERSONAL_KEY_UNLOCK, setId(new Item.Properties().stacksTo(1), "keyring_personal_unlock"))),
        KEYRING_SUSPEND = ITEMS.register("keyring_suspend", () -> new ItemKeyring(SUSPEND_KEY, setId(new Item.Properties().stacksTo(1), "keyring_suspend"))),
        KEYRING_PRIORITY = ITEMS.register("keyring_priority", () -> new ItemKeyring(PRIORITY_KEY, setId(new Item.Properties().stacksTo(1), "keyring_priority"))),
        KEYRING_PRIORITY_P1 = ITEMS.register("keyring_priority_p1", () -> new ItemKeyring(PRIORITY_KEY_P1, setId(new Item.Properties().stacksTo(1), "keyring_priority_p1"))),
        KEYRING_PRIORITY_P2 = ITEMS.register("keyring_priority_p2", () -> new ItemKeyring(PRIORITY_KEY_P2, setId(new Item.Properties().stacksTo(1), "keyring_priority_p2"))),
        KEYRING_PRIORITY_N1 = ITEMS.register("keyring_priority_n1", () -> new ItemKeyring(PRIORITY_KEY_N1, setId(new Item.Properties().stacksTo(1), "keyring_priority_n1"))),
        KEYRING_PRIORITY_N2 = ITEMS.register("keyring_priority_n2", () -> new ItemKeyring(PRIORITY_KEY_N2, setId(new Item.Properties().stacksTo(1), "keyring_priority_n2"))),
        KEYRING_PULLER = ITEMS.register("keyring_puller", () -> new ItemKeyring(DRAWER_PULLER, setId(new Item.Properties().stacksTo(1), "keyring_puller")));

    private ModItems () { }

    private static Item.Properties setId(Item.Properties props, String name) {
        return props.setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, name)));
    }

    public static void init (ChameleonInit.InitContext context) {
        EXCLUDE_ITEMS_CREATIVE_TAB.add(PRIORITY_KEY_N1);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(PRIORITY_KEY_N2);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(PRIORITY_KEY_P1);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(PRIORITY_KEY_P2);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_DRAWER);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_QUANTIFY);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_SHROUD);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PERSONAL);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PERSONAL_COFH);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PERSONAL_FTB);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PERSONAL_UNLOCK);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_SUSPEND);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PRIORITY);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PRIORITY_P1);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PRIORITY_P2);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PRIORITY_N1);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PRIORITY_N2);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(KEYRING_PULLER);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(DETACHED_DRAWER_FULL);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(REMOTE_UPGRADE_BOUND);
        EXCLUDE_ITEMS_CREATIVE_TAB.add(REMOTE_GROUP_UPGRADE_BOUND);

        for (RegistryEntry<Block> ro : ModBlocks.BLOCKS.getEntries()) {
            if (ModBlocks.EXCLUDE_ITEMS.contains(ro.getId().getPath()))
                continue;

            registerBlock(ITEMS, ro);
        }

        ITEMS.init(context);
    }

    static void registerBlock (ChameleonRegistry<Item> register, RegistryEntry<? extends Block> blockHolder) {
        if (blockHolder == null)
            return;

        register.register(blockHolder.getId().getPath(), () -> {
            String path = blockHolder.getId().getPath();
            Block block = blockHolder.get();
            if (block instanceof BlockMeta)
                return null;
            if (block instanceof BlockFramedStandardDrawers) {
                return new ItemFramedDrawers(block, setId(new Item.Properties(), path));
            } else if (block instanceof BlockDrawers) {
                return new ItemDrawers(block, setId(new Item.Properties(), path));
            } else if (block instanceof BlockFramedTrim) {
                return new ItemFramedTrim(block, setId(new Item.Properties(), path));
            } else if (block instanceof BlockTrim) {
                return new ItemTrim(block, setId(new Item.Properties(), path));
            } else {
                return new BlockItem(block, setId(new Item.Properties(), path));
            }
        });
    }

    private static <B extends Item> Stream<B> getItemsOfType (Class<B> itemClass) {
        return BuiltInRegistries.ITEM.stream().filter(itemClass::isInstance).map(itemClass::cast);
    }

    public static Stream<ItemKey> getKeys () {
        return getItemsOfType(ItemKey.class);
    }

    public static Stream<ItemKeyring> getKeyrings () {
        return getItemsOfType(ItemKeyring.class);
    }
}