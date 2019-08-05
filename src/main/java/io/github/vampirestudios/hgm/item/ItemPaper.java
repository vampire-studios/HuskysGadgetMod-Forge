package io.github.vampirestudios.hgm.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class ItemPaper extends BaseItem {
    public ItemPaper() {
        super("paper", new Properties().maxStackSize(1));
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            CompoundNBT copy = tag.copy();
            copy.remove("BlockEntityTag");
            return copy;
        }
        return null;
    }
}
