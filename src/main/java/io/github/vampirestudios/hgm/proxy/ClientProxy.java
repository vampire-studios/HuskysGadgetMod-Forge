package io.github.vampirestudios.hgm.proxy;

import io.github.vampirestudios.hgm.block.entity.TileEntityEasterEgg;
import io.github.vampirestudios.hgm.init.GadgetBlocks;
import io.github.vampirestudios.hgm.init.GadgetItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ItemColors itemColors = Minecraft.getInstance().getItemColors();
        IItemColor easterEgg = (stack, tintIndex) -> tintIndex < 2 && stack.hasTag() ? Objects.requireNonNull(stack.getTag()).getInt("color" + tintIndex) : 0xFFFFFF;
        itemColors.register(easterEgg, GadgetItems.EASTER_EGG_ITEM);

        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        IBlockColor easterEggBlock = (state, worldIn, pos, tintIndex) -> {
            TileEntity te = Objects.requireNonNull(worldIn).getTileEntity(Objects.requireNonNull(pos));
            if (te instanceof TileEntityEasterEgg) {
                return ((TileEntityEasterEgg) te).getColor(tintIndex);
            }
            return 0xFFFFFF;
        };
        blockColors.register(easterEggBlock, GadgetBlocks.EASTER_EGG);
    }

}
