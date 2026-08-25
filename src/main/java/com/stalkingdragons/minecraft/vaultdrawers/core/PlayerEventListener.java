package com.stalkingdragons.minecraft.vaultdrawers.core;

import com.stalkingdragons.minecraft.vaultdrawers.api.storage.attribute.IPortable;
import com.stalkingdragons.minecraft.vaultdrawers.config.ModCommonConfig;
import com.stalkingdragons.minecraft.vaultdrawers.item.ItemUpgradeRemote;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class PlayerEventListener
{
	private void applyDebuff(Player plr)
	{
		plr.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 3, true, true));
	}

	@SubscribeEvent
	public void onPlayerPickup(ItemEntityPickupEvent.Post event) {
		if (!ModCommonConfig.INSTANCE.DRAWERS.anyHeavyDrawers())
			return;

		checkItemDebuf(event.getItemEntity().getItem(), event.getPlayer());
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent.Post event) {
		if(event.getEntity().tickCount % 60 != 0)
			return;

		if (event.getEntity() instanceof ServerPlayer)
			ItemUpgradeRemote.validateInventory(event.getEntity().getInventory(), event.getEntity().level());

		if (!ModCommonConfig.INSTANCE.DRAWERS.anyHeavyDrawers())
			return;

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (checkItemDebuf(event.getEntity().getItemBySlot(slot), event.getEntity()))
				return;
		}

		Inventory inv = event.getEntity().getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (checkItemDebuf(inv.getItem(i), event.getEntity()))
				return;
		}
	}

	private boolean checkItemDebuf (ItemStack stack, Player player) {
		Item item = stack.getItem();
		if (item instanceof IPortable ip) {
			if (ip.isHeavy(player.level().registryAccess(), stack)) {
				applyDebuff(player);
				return true;
			}
		}

		return false;
	}
}
