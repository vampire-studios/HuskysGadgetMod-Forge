package io.github.vampirestudios.hgm.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;

public class CustomBlockItem extends BlockItem {

    public CustomBlockItem(Block blockIn, ItemGroup itemGroup) {
        super(blockIn, new Properties().group(itemGroup));
        setRegistryName(blockIn.getRegistryName());
    }

}
