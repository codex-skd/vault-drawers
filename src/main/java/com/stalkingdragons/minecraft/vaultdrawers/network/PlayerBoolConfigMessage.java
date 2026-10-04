package com.stalkingdragons.minecraft.vaultdrawers.network;

import com.google.common.collect.Maps;
import com.stalkingdragons.minecraft.vaultdrawers.ModConstants;
import com.stalkingdragons.minecraft.vaultdrawers.config.PlayerConfig;
import com.stalkingdragons.minecraft.vaultdrawers.config.PlayerConfigSetting;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.network.ChameleonPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public record PlayerBoolConfigMessage(String uuid, String key, boolean value) implements ChameleonPacket
{
    public static final Type<PlayerBoolConfigMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "player_bool_config"));

    public static final StreamCodec<FriendlyByteBuf, PlayerBoolConfigMessage> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        PlayerBoolConfigMessage::uuid,
        ByteBufCodecs.STRING_UTF8,
        PlayerBoolConfigMessage::key,
        ByteBufCodecs.BOOL,
        PlayerBoolConfigMessage::value,
        PlayerBoolConfigMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type () {
        return TYPE;
    }

    @Override
    public void handleMessage (Player player, Consumer<Runnable> workQueue) {
        if (player instanceof ServerPlayer) {
            workQueue.accept(() -> {
                UUID playerUniqueId;
                try {
                    playerUniqueId = UUID.fromString(uuid);
                } catch (IllegalArgumentException e) {
                    return;
                }

                Map<String, PlayerConfigSetting<?>> clientMap = PlayerConfig.serverPlayerConfigSettings.get(playerUniqueId);
                if (clientMap == null) {
                    clientMap = Maps.newHashMap();
                }

                clientMap.put(key, new PlayerConfigSetting<>(key, value, playerUniqueId));
                PlayerConfig.serverPlayerConfigSettings.put(playerUniqueId, clientMap);
            });
        }
    }
}
