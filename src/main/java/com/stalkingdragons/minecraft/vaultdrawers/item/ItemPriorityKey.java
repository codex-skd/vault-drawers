package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesModifiable;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class ItemPriorityKey extends ItemKey
{
    private final int level;
    private final int nextLevel;
    private static final Map<Integer, ItemPriorityKey> priorityLookup = new HashMap<>();

    public ItemPriorityKey (int level, int nextLevel, Properties properties) {
        super(properties);
        this.level = level;
        this.nextLevel = nextLevel;
        priorityLookup.put(level, this);
    }

    public ItemPriorityKey getNextKey () {
        return priorityLookup.getOrDefault(this.nextLevel, this);
    }

    @Override
    protected void handleDrawerAttributes (IDrawerAttributesModifiable attrs) {
        if (level == 0)
            attrs.setPriority(priorityLookup.getOrDefault(attrs.getPriority(), this).getNextKey().level);
        else {
            if (level == attrs.getPriority())
                attrs.setPriority(0);
            else
                attrs.setPriority(level);
        }
    }

    @Override
    public InteractionResult use (Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!ModCommonConfig.INSTANCE.TOOLS.priorityKey.enable.get())
            return InteractionResult.PASS;

        if (!player.isShiftKeyDown())
            return InteractionResult.PASS;

        ItemStack nextKey = new ItemStack(getNextKey(), 1);
        nextKey.set(DataComponents.CUSTOM_DATA, stack.get(DataComponents.CUSTOM_DATA));

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isEnabled () {
        return ModCommonConfig.INSTANCE.TOOLS.priorityKey.enable.get();
    }
}