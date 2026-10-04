package com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata;

import com.stalkingdragons.minecraft.vaultdrawers.api.framing.FrameMaterial;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedMaterials;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.FrameData;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import com.stalkingdragons.minecraft.vaultdrawers.inventory.ItemStackHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MaterialData extends BlockEntityDataShim implements IFramedMaterials
{
    public static final MaterialData EMPTY = new MaterialData();

    public static final Codec<MaterialData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            ItemStack.CODEC.fieldOf("frameBase").forGetter(MaterialData::getFrameBase),
            ItemStack.CODEC.fieldOf("materialSide").forGetter(MaterialData::getSide),
            ItemStack.CODEC.fieldOf("materialFront").forGetter(MaterialData::getFront),
            ItemStack.CODEC.fieldOf("materialTrim").forGetter(MaterialData::getTrim)
        ).apply(instance, MaterialData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MaterialData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC,
        MaterialData::getFrameBase,
        ItemStack.OPTIONAL_STREAM_CODEC,
        MaterialData::getSide,
        ItemStack.OPTIONAL_STREAM_CODEC,
        MaterialData::getFront,
        ItemStack.OPTIONAL_STREAM_CODEC,
        MaterialData::getTrim,
        MaterialData::new
    );

    @NotNull
    private ItemStack frameBase;
    @NotNull
    private ItemStack materialSide;
    @NotNull
    private ItemStack materialFront;
    @NotNull
    private ItemStack materialTrim;

    public MaterialData () {
        this(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public MaterialData (@NotNull ItemStack frameBase, @NotNull ItemStack side, @NotNull ItemStack front, @NotNull ItemStack trim) {
        this.frameBase = frameBase;
        materialSide = side;
        materialFront = front;
        materialTrim = trim;
    }

    public MaterialData (IFramedMaterials materials) {
        this();

        if (materials != null) {
            frameBase = materials.getHostBlock();
            materialSide = materials.getMaterial(FrameMaterial.SIDE);
            materialFront = materials.getMaterial(FrameMaterial.FRONT);
            materialTrim = materials.getMaterial(FrameMaterial.TRIM);
        }
    }

    @NotNull
    public ItemStack getFrameBase() {
        return frameBase;
    }

    @NotNull
    public ItemStack getSide () {
        return materialSide;
    }

    @NotNull
    public ItemStack getFront () {
        return materialFront;
    }

    @NotNull
    public ItemStack getTrim () {
        return materialTrim;
    }

    @NotNull
    public ItemStack getEffectiveSide () {
        return materialSide;
    }

    public boolean isMatOpaque (ItemStack mat) {
        if (mat.getItem() instanceof BlockItem blockItem)
            return blockItem.getBlock().defaultBlockState().canOcclude();
        return false;
    }

    public boolean allMatOpaque () {
        return isMatOpaque(materialSide)
            && (materialFront.isEmpty() || isMatOpaque(materialFront))
            && (materialTrim.isEmpty() || isMatOpaque(materialTrim));
    }

    @NotNull
    public ItemStack getEffectiveFront () {
        return !materialFront.isEmpty() ? materialFront : materialSide;
    }

    @NotNull
    public ItemStack getEffectiveTrim () {
        return !materialTrim.isEmpty() ? materialTrim : materialSide;
    }

    public void setFrameBase (@NotNull ItemStack frameBase) {
        this.frameBase = frameBase;
    }

    public void setSide (@NotNull ItemStack material) {
        materialSide = material;
    }

    public void setFront (@NotNull ItemStack material) {
        materialFront = material;
    }

    public void setTrim (@NotNull ItemStack material) {
        materialTrim = material;
    }

    public void clear () {
        materialSide = ItemStack.EMPTY;
        materialFront = ItemStack.EMPTY;
        materialTrim = ItemStack.EMPTY;
    }

    public boolean isEmpty () {
        return materialFront.isEmpty() && materialSide.isEmpty() && materialTrim.isEmpty();
    }

    public void read (ItemStack stack) {
        FrameData data = stack.getOrDefault(ModDataComponents.FRAME_DATA.get(), FrameData.EMPTY);

        frameBase = data.base();
        materialSide = data.side();
        materialFront = data.front();
        materialTrim = data.trim();
    }

    @Override
    public void read (HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains("MatB")) {
            Optional<CompoundTag> matB = tag.getCompound("MatB");
            if (matB.isPresent() && matB.get().contains("Item")) {
                frameBase = ItemStackHelper.parseOptional(provider, matB.get().getCompound("Item")).orElse(ItemStack.EMPTY);
            } else {
                frameBase = ItemStack.EMPTY;
            }
        } else {
            frameBase = ItemStack.EMPTY;
        }

        if (tag.contains("MatS")) {
            Optional<CompoundTag> matS = tag.getCompound("MatS");
            if (matS.isPresent() && matS.get().contains("Item")) {
                materialSide = ItemStackHelper.parseOptional(provider, matS.get().getCompound("Item")).orElse(ItemStack.EMPTY);
            } else {
                materialSide = ItemStack.EMPTY;
            }
        } else {
            materialSide = ItemStack.EMPTY;
        }

        if (tag.contains("MatF")) {
            Optional<CompoundTag> matF = tag.getCompound("MatF");
            if (matF.isPresent() && matF.get().contains("Item")) {
                materialFront = ItemStackHelper.parseOptional(provider, matF.get().getCompound("Item")).orElse(ItemStack.EMPTY);
            } else {
                materialFront = ItemStack.EMPTY;
            }
        } else {
            materialFront = ItemStack.EMPTY;
        }

        if (tag.contains("MatT")) {
            Optional<CompoundTag> matT = tag.getCompound("MatT");
            if (matT.isPresent() && matT.get().contains("Item")) {
                materialTrim = ItemStackHelper.parseOptional(provider, matT.get().getCompound("Item")).orElse(ItemStack.EMPTY);
            } else {
                materialTrim = ItemStack.EMPTY;
            }
        } else {
            materialTrim = ItemStack.EMPTY;
        }
    }

@Override
    public CompoundTag write (HolderLookup.Provider provider, CompoundTag tag) {
        if (!frameBase.isEmpty()) {
            CompoundTag matB = new CompoundTag();
            // Use ItemStack.CODEC to serialize
            com.mojang.serialization.DataResult<net.minecraft.nbt.Tag> result = ItemStack.CODEC.encodeStart(
                net.minecraft.nbt.NbtOps.INSTANCE, frameBase);
            result.resultOrPartial(error -> {}).ifPresent(itemTag -> {
                matB.put("Item", itemTag);
            });
            tag.put("MatB", matB);
        } else if (tag.contains("MatB"))
            tag.remove("MatB");

        if (!materialSide.isEmpty()) {
            CompoundTag matS = new CompoundTag();
            // Use ItemStack.CODEC to serialize
            com.mojang.serialization.DataResult<net.minecraft.nbt.Tag> result = ItemStack.CODEC.encodeStart(
                net.minecraft.nbt.NbtOps.INSTANCE, materialSide);
            result.resultOrPartial(error -> {}).ifPresent(itemTag -> {
                matS.put("Item", itemTag);
            });
            tag.put("MatS", matS);
        } else if (tag.contains("MatS"))
            tag.remove("MatS");

        if (!materialFront.isEmpty()) {
            CompoundTag matF = new CompoundTag();
            // Use ItemStack.CODEC to serialize
            com.mojang.serialization.DataResult<net.minecraft.nbt.Tag> result = ItemStack.CODEC.encodeStart(
                net.minecraft.nbt.NbtOps.INSTANCE, materialFront);
            result.resultOrPartial(error -> {}).ifPresent(itemTag -> {
                matF.put("Item", itemTag);
            });
            tag.put("MatF", matF);
        } else if (tag.contains("MatF"))
            tag.remove("MatF");

        if (!materialTrim.isEmpty()) {
            CompoundTag matT = new CompoundTag();
            // Use ItemStack.CODEC to serialize
            com.mojang.serialization.DataResult<net.minecraft.nbt.Tag> result = ItemStack.CODEC.encodeStart(
                net.minecraft.nbt.NbtOps.INSTANCE, materialTrim);
            result.resultOrPartial(error -> {}).ifPresent(itemTag -> {
                matT.put("Item", itemTag);
            });
            tag.put("MatT", matT);
        } else if (tag.contains("MatT"))
            tag.remove("MatT");

        return tag;
    }

    @Override
    public @NotNull ItemStack getHostBlock () {
        return frameBase;
    }

    @Override
    public void setHostBlock (@NotNull ItemStack stack) {
        frameBase = stack;
    }

    @Override
    public @NotNull ItemStack getMaterial (FrameMaterial material) {
        return switch (material) {
            case SIDE -> materialSide;
            case TRIM -> materialTrim;
            case FRONT -> materialFront;
        };
    }

    @Override
    public void setMaterial (FrameMaterial material, @NotNull ItemStack stack) {
        switch (material) {
            case SIDE -> materialSide = stack;
            case TRIM -> materialTrim = stack;
            case FRONT -> materialFront = stack;
        }
    }
}