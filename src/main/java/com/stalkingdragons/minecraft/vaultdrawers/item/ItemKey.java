package com.stalkingdragons.minecraft.vaultdrawers.item;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.EmptyDrawerAttributes;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributes;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributesModifiable;
import com.stalkingdragons.minecraft.vaultdrawers.capabilities.Capabilities;
import com.stalkingdragons.minecraft.vaultdrawers.util.ComponentUtil;
import com.stalkingdragons.minecraft.vaultdrawers.util.WorldUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemKey extends Item
{
    public ItemKey (Properties properties) {
        super(properties.attributes(createAttributes()));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                    BASE_ATTACK_DAMAGE_ID,
                    2,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .build();
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player) {
        return !player.isCreative();
    }

    @Override
    public void appendHoverText (ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ComponentUtil.appendSplitDescription(tooltip, this);

        if (!isEnabled())
            tooltip.add(Component.translatable("itemConfig.storagedrawers.disabled_tool")
                .withStyle(ChatFormatting.YELLOW));
    }

    @NotNull
    public Component getDescription() {
        return isEnabled()
            ? Component.translatable(this.getDescriptionId() + ".desc") : Component.empty();
    }

    @Override
    @NotNull
    public InteractionResult useOn (UseOnContext context) {
        if (!isEnabled())
            return InteractionResult.PASS;

        BlockEntity blockEntity = WorldUtils.getBlockEntity(context.getLevel(), context.getClickedPos(), BlockEntity.class);
        if (blockEntity == null)
            return InteractionResult.PASS;

        IDrawerAttributes attrs = Capabilities.DRAWER_ATTRIBUTES.getCapability(blockEntity.getLevel(), blockEntity.getBlockPos());
        if (attrs == null)
            attrs = EmptyDrawerAttributes.EMPTY;

        if (!(attrs instanceof IDrawerAttributesModifiable))
            return InteractionResult.PASS;

        handleDrawerAttributes((IDrawerAttributesModifiable)attrs);

        if (context.getPlayer() != null)
            context.getPlayer().getCooldowns().addCooldown(this, 5);

        return InteractionResult.SUCCESS;
    }

    protected void handleDrawerAttributes (IDrawerAttributesModifiable attrs) { }

    public boolean isEnabled () {
        return true;
    }
}