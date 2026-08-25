package com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.service;

import com.stalkingdragons.minecraft.vaultdrawers.chameleon.api.ChameleonInit;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.network.ChameleonPacket;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.service.ChameleonNetworking;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon_neoforge.registry.NeoforgeRegistryContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Consumer;

public class NeoforgeNetworking implements ChameleonNetworking
{
    private static PayloadRegistrar registrar = null;

    public static void init (String modId, ChameleonInit init, NeoforgeRegistryContext context) {
        context.getEventBus().addListener((Consumer<RegisterPayloadHandlersEvent>) event -> {
            registrar = event.registrar(modId);
            init.init(context);
            registrar = null;
        });
    }

    @Override
    public <B extends FriendlyByteBuf, P extends ChameleonPacket> void registerPacketInternal (CustomPacketPayload.Type<P> payloadType, StreamCodec<B, P> codec, boolean clientBound) {
        IPayloadHandler<P> handler = (packet, context) -> {
            packet.handleMessage(context.player(), context::enqueueWork);
        };

        if (clientBound)
            registrar.playToClient(payloadType, (StreamCodec<RegistryFriendlyByteBuf, P>) codec, handler);
        else
            registrar.playToServer(payloadType, (StreamCodec<RegistryFriendlyByteBuf, P>) codec, handler);
    }

    @Override
    public void sendToPlayer (ChameleonPacket packet, ServerPlayer player) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, packet);
    }

    @Override
    public void sendToPlayersNear(ChameleonPacket packet, ServerLevel level, double x, double y, double z, double radius) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayersNear(level, null, x, y, z, radius, packet);
    }

    @Override
    public void sendToServer (ChameleonPacket packet) {
        ClientPacketDistributor.sendToServer(packet);
    }
}
