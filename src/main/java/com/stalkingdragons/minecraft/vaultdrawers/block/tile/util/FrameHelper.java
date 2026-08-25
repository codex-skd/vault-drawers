package com.stalkingdragons.minecraft.vaultdrawers.block.tile.util;

import com.stalkingdragons.minecraft.vaultdrawers.api.framing.FrameMaterial;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedBlock;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedSourceBlock;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.tiledata.MaterialData;
import com.stalkingdragons.minecraft.vaultdrawers.components.item.FrameData;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModDataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FrameHelper
{
    public static ItemStack makeFramedItem (IFramedBlock resultBlock, ItemStack source, ItemStack matSide, ItemStack matTrim, ItemStack matFront) {
        if (!(resultBlock instanceof Block))
            return ItemStack.EMPTY;

        if (source.isEmpty())
            return ItemStack.EMPTY;

        if (source.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (!(block instanceof IFramedSourceBlock))
                return ItemStack.EMPTY;
        } else
            return ItemStack.EMPTY;

        MaterialData data = new MaterialData();
        data.setFrameBase(new ItemStack(source.getItem(), 1));
        if (resultBlock.supportsFrameMaterial(FrameMaterial.SIDE))
            data.setSide(matSide.copyWithCount(1));
        if (resultBlock.supportsFrameMaterial(FrameMaterial.TRIM))
            data.setTrim(matTrim.copyWithCount(1));
        if (resultBlock.supportsFrameMaterial(FrameMaterial.FRONT))
            data.setFront(matFront.copyWithCount(1));

        ItemStack stack = source.transmuteCopy(((Block) resultBlock).asItem());
        stack.set(ModDataComponents.FRAME_DATA.get(), new FrameData(data));

        return stack;
    }
}