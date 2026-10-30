package com.stalkingdragons.minecraft.vaultdrawers.capabilities;

import com.stalkingdragons.minecraft.vaultdrawers.ModConstants;
import com.stalkingdragons.minecraft.vaultdrawers.api.capabilities.IItemHandler;
import com.stalkingdragons.minecraft.vaultdrawers.api.capabilities.IItemRepository;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerAttributes;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.IDrawerGroup;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.capabilities.ChameleonCapability;

public class Capabilities
{
    public static final ChameleonCapability<IDrawerAttributes> DRAWER_ATTRIBUTES =
        ChameleonServices.CAPABILITY.create(ModConstants.loc("drawer_attributes"), IDrawerAttributes.class, Void.TYPE);
    public static final ChameleonCapability<IDrawerGroup> DRAWER_GROUP =
        ChameleonServices.CAPABILITY.create(ModConstants.loc("drawer_group"), IDrawerGroup.class, Void.TYPE);
    public static final ChameleonCapability<IItemRepository> ITEM_REPOSITORY =
        ChameleonServices.CAPABILITY.create(ModConstants.loc("item_repository"), IItemRepository.class, Void.TYPE);
    public static final ChameleonCapability<IItemHandler> ITEM_HANDLER =
        ChameleonServices.CAPABILITY.create(ModConstants.loc("item_handler"), IItemHandler.class, Void.TYPE);
}