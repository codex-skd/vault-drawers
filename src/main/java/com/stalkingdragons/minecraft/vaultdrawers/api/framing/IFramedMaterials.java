package com.stalkingdragons.minecraft.vaultdrawers.api.framing;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IFramedMaterials
{
    @NotNull
    ItemStack getHostBlock ();

    void setHostBlock (@NotNull ItemStack stack);

    @NotNull
    ItemStack getMaterial (FrameMaterial material);

    void setMaterial (FrameMaterial material, @NotNull ItemStack stack);

    default boolean isEmpty() {
        return getMaterial(FrameMaterial.SIDE).isEmpty()
            && getMaterial(FrameMaterial.TRIM).isEmpty()
            && getMaterial(FrameMaterial.FRONT).isEmpty();
    }
}