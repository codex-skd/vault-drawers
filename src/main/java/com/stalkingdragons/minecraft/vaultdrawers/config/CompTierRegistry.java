package com.stalkingdragons.minecraft.vaultdrawers.config;

import com.stalkingdragons.minecraft.vaultdrawers.VaultDrawers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompTierRegistry
{
    public static final CompTierRegistry INSTANCE = new CompTierRegistry();

    public static class Record {
        @NotNull
        private final Item upperItem;
        @NotNull
        private final Item lowerItem;
        public final int convRate;
        private ItemStack upperStack;
        private ItemStack lowerStack;

        public Record (@NotNull Item upper, @NotNull Item lower, int convRate) {
            this.upperItem = upper;
            this.lowerItem = lower;
            this.convRate = convRate;
        }

        @NotNull
        public ItemStack getUpper () {
            if (upperStack == null) {
                upperStack = new ItemStack(upperItem);
                upperStack.setCount(1);
            }
            return upperStack;
        }

        @NotNull
        public ItemStack getLower () {
            if (lowerStack == null) {
                lowerStack = new ItemStack(lowerItem);
                lowerStack.setCount(1);
            }
            return lowerStack;
        }

        @NotNull
        public Item getUpperItem () {
            return upperItem;
        }

        @NotNull
        public Item getLowerItem () {
            return lowerItem;
        }
    }

    private final List<Record> records = new ArrayList<>();
    private List<String> pendingRules = new ArrayList<>();
    private boolean initialized;

    public CompTierRegistry () { }

    public void initialize () {
        initialized = true;

        if (ModCommonConfig.INSTANCE.DRAWERS.compacting.enableExtraCompactingRules.get()) {
            register(Blocks.CLAY.asItem(), Items.CLAY_BALL, 4);
            register(Blocks.SNOW_BLOCK.asItem(), Items.SNOWBALL, 4);
            register(Blocks.GLOWSTONE.asItem(), Items.GLOWSTONE_DUST, 4);
            register(Blocks.BRICKS.asItem(), Items.BRICK, 4);
            register(Blocks.NETHER_BRICKS.asItem(), Items.NETHER_BRICK, 4);
            register(Blocks.NETHER_WART_BLOCK.asItem(), Items.NETHER_WART, 9);
            register(Blocks.QUARTZ_BLOCK.asItem(), Items.QUARTZ, 4);
            register(Blocks.MELON.asItem(), Items.MELON_SLICE, 9);
            register(Blocks.BAMBOO_BLOCK.asItem(), Items.BAMBOO, 9);
        }

        ModCommonConfig.INSTANCE.onLoad(() -> ModCommonConfig.INSTANCE.DRAWERS.compacting.compRules.get().forEach(this::register));

        for (String rule : pendingRules) {
            register(rule);
        }

        pendingRules = null;
    }

    public boolean register (@NotNull Item upper, @NotNull Item lower, int convRate) {
        unregisterUpperTarget(upper);
        unregisterLowerTarget(lower);

        Record r = new Record(upper, lower, convRate);

        records.add(r);

        if (ModCommonConfig.INSTANCE.GENERAL.logStartupActivity.get())
            VaultDrawers.log.info("New compacting rule " + convRate + " " + lower + " = 1 " + upper);

        return true;
    }

    public static boolean validateRuleSyntax (String rule) {
        String[] parts = rule.split("\\s*,\\s*");
        if (parts.length != 3)
            return false;

        Identifier upperResource = Identifier.tryParse(parts[0]);
        Identifier lowerResource = Identifier.tryParse(parts[1]);
        if (upperResource == null || lowerResource == null)
            return false;

        try {
            int conv = Integer.parseInt(parts[2]);
            return conv >= 1;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public void register (List<String> rules) {
        rules.forEach(this::register);
    }

    public boolean register (String rule) {
        if (!initialized) {
            pendingRules.add(rule);
            return true;
        }

        String[] parts = rule.split("\\s*,\\s*");
        if (parts.length != 3)
            return false;

        Identifier upperResource = Identifier.parse(parts[0]);
        Item upperItem = BuiltInRegistries.ITEM.get(upperResource).orElse(null).value();

        Identifier lowerResource = Identifier.parse(parts[1]);
        Item lowerItem = BuiltInRegistries.ITEM.get(lowerResource).orElse(null).value();

        try {
            int conv = Integer.parseInt(parts[2]);
            return register(upperItem, lowerItem, conv);
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean unregisterUpperTarget (@NotNull Item item) {
        Iterator<Record> it = records.iterator();
        while (it.hasNext()) {
            if (it.next().getUpperItem() == item) {
                it.remove();
                return true;
            }
        }

        return false;
    }

    public boolean unregisterLowerTarget (@NotNull Item item) {
        Iterator<Record> it = records.iterator();
        while (it.hasNext()) {
            if (it.next().getLowerItem() == item) {
                it.remove();
                return true;
            }
        }

        return false;
    }

    public Record findHigherTier (@NotNull ItemStack stack) {
        if (stack.isEmpty())
            return null;

        Item item = stack.getItem();
        for (Record r : records) {
            if (item == r.getLowerItem())
                return r;
        }

        return null;
    }

    public Record findLowerTier (@NotNull ItemStack stack) {
        if (stack.isEmpty())
            return null;

        Item item = stack.getItem();
        for (Record r : records) {
            if (item == r.getUpperItem())
                return r;
        }

        return null;
    }
}