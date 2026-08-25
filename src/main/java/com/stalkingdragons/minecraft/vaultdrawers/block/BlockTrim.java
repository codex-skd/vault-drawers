package com.stalkingdragons.minecraft.vaultdrawers.block;

import com.stalkingdragons.minecraft.vaultdrawers.ModConstants;
import com.stalkingdragons.minecraft.vaultdrawers.api.framing.IFramedSourceBlock;
import com.stalkingdragons.minecraft.vaultdrawers.api.storage.INetworked;
import com.stalkingdragons.minecraft.vaultdrawers.block.tile.util.FrameHelper;
import com.stalkingdragons.minecraft.vaultdrawers.core.ModBlocks;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class BlockTrim extends Block implements INetworked, IFramedSourceBlock
{
    private String matKey = null;
    private String matNamespace = ModConstants.MOD_ID;

    public BlockTrim (Properties properties) {
        super(properties);
    }

    public BlockTrim setMatKey (Identifier material) {
        this.matNamespace = material.getNamespace();
        this.matKey = material.getPath();
        return this;
    }

    public BlockTrim setMatKey (@Nullable String matKey) {
        this.matKey = matKey;
        return this;
    }

    public String getMatKey () {
        return matKey;
    }

    public String getNameMatKey () {
        return "block." + matNamespace + ".mat." + matKey;
    }

    public String getNameTypeKey() {
        return "block." + ModConstants.MOD_ID + ".type.trim";
    }

    public boolean canUseForRetrim () {
        return true;
    }

    @Override
    public ItemStack makeFramedItem (ItemStack source, ItemStack matSide, ItemStack matTrim, ItemStack matFront) {
        return FrameHelper.makeFramedItem(ModBlocks.FRAMED_TRIM.get(), source, matSide, matTrim, matFront);
    }
}