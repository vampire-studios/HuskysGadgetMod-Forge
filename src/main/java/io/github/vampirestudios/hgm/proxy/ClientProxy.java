package io.github.vampirestudios.hgm.proxy;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.BlockColored;
import io.github.vampirestudios.hgm.block.entity.*;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.core.client.LaptopRenderer;
import io.github.vampirestudios.hgm.core.client.PaperRenderer;
import io.github.vampirestudios.hgm.core.client.PrinterRenderer;
import io.github.vampirestudios.hgm.core.client.RouterRenderer;
import io.github.vampirestudios.hgm.init.GadgetBlocks;
import io.github.vampirestudios.hgm.init.GadgetItems;
import io.github.vampirestudios.hgm.item.ItemColored;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Objects;

public class ClientProxy implements IProxy {

    @Override
    public void init(FMLCommonSetupEvent event) {

        BaseDevice.addWallpaper(new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/wallpapers/default.png"));
        for(int i = 1; i > 17; i++) {
            BaseDevice.addWallpaper(new ResourceLocation(HuskysGadgetMod.MOD_ID, String.format("textures/gui/wallpapers/wallpaper_%d.png", i)));
        }

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

        ItemColors items = Minecraft.getInstance().getItemColors();
        BlockColors blocks = Minecraft.getInstance().getBlockColors();

        IItemColor handlerItems = (s, t) -> t == 0 ? ((ItemColored) s.getItem()).color.getId() : 0xFFFFFF;
        items.register(handlerItems, GadgetItems.FLASH_DRIVES);
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.ROOF_LIGHTS);
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.ROUTERS);
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.PRINTERS);
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.LAPTOPS);

        IBlockColor handlerBlocks = (s, w, p, t) -> t == 0 ? ((BlockColored) s.getBlock()).color.getId() : 0xFFFFFF;
        blocks.register(handlerBlocks, GadgetBlocks.ROOF_LIGHTS);
        blocks.register(handlerBlocks, GadgetBlocks.ROUTERS);
        blocks.register(handlerBlocks, GadgetBlocks.PRINTERS);
        blocks.register(handlerBlocks, GadgetBlocks.LAPTOPS);

        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityPaper.class, new PaperRenderer());
        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityPrinter.class, new PrinterRenderer());
        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityLaptop.class, new LaptopRenderer());
        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityRouter.class, new RouterRenderer());
    }

}
