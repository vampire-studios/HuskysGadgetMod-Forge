package io.github.vampirestudios.hgm.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemDevice extends CustomBlockItem {

    public ItemDevice(Block blockIn, ItemGroup itemGroup) {
        super(blockIn, new Properties().maxStackSize(1), itemGroup);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT tag = new CompoundNBT();
        if (stack.getTag() != null && stack.getTag().contains("display", Constants.NBT.TAG_COMPOUND)) {
            tag.put("display", Objects.requireNonNull(stack.getTag().get("display")));
        }
        return tag;
    }

}
