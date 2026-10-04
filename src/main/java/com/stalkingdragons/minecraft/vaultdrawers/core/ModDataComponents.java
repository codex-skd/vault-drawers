package com.stalkingdragons.minecraft.vaultdrawers.core;

import com.stalkingdragons.minecraft.vaultdrawers.ModConstants;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.*;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.ChameleonServices;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.api.ChameleonInit;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.ChameleonRegistry;
import com.stalkingdragons.minecraft.vaultdrawers.chameleon.registry.RegistryEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModDataComponents
{
    public static final ChameleonRegistry<DataComponentType<?>> COMPONENTS = ChameleonServices.REGISTRY.create(BuiltInRegistries.DATA_COMPONENT_TYPE, ModConstants.MOD_ID);

    public static final RegistryEntry<DataComponentType<ControllerBinding>> CONTROLLER_BINDING =
        COMPONENTS.register("controller_binding", () -> DataComponentType.<ControllerBinding>builder()
            .persistent(ControllerBinding.CODEC).networkSynchronized(ControllerBinding.STREAM_CODEC).build());

    public static final RegistryEntry<DataComponentType<DrawerCountData>> DRAWER_COUNT =
        COMPONENTS.register("drawer_count", () -> DataComponentType.<DrawerCountData>builder().persistent(DrawerCountData.CODEC).build());

    public static final RegistryEntry<DataComponentType<KeyringContents>> KEYRING_CONTENTS =
        COMPONENTS.register("keyring_content", () -> DataComponentType.<KeyringContents>builder().persistent(KeyringContents.CODEC).build());

    public static final RegistryEntry<DataComponentType<FrameData>> FRAME_DATA =
        COMPONENTS.register("frame_data", () -> DataComponentType.<FrameData>builder()
            .persistent(FrameData.CODEC).networkSynchronized(FrameData.STREAM_CODEC).build());

    public static final RegistryEntry<DataComponentType<DetachedDrawerContents>> DETACHED_DRAWER_CONTENTS =
        COMPONENTS.register("detached_drawer_content", () -> DataComponentType.<DetachedDrawerContents>builder().persistent(DetachedDrawerContents.CODEC).build());

    public static void init (ChameleonInit.InitContext context) {
        COMPONENTS.init(context);
    }
}