package com.stalkingdragons.minecraft.vaultdrawers.core;

import com.stalkingdragons.minecraft.vaultdrawers.network.CountUpdateMessage;
import com.stalkingdragons.minecraft.vaultdrawers.network.PlayerBoolConfigMessage;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.api.ChameleonInit;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.service.ChameleonNetworking;

public class ModNetworking implements ChameleonInit
{
    public static final ModNetworking INSTANCE = new ModNetworking();

    @Override
    public void init (ChameleonInit.InitContext context) {
        ChameleonNetworking.registerPacket(CountUpdateMessage.TYPE, CountUpdateMessage.STREAM_CODEC, true);
        ChameleonNetworking.registerPacket(PlayerBoolConfigMessage.TYPE, PlayerBoolConfigMessage.STREAM_CODEC, false);
    }
}
