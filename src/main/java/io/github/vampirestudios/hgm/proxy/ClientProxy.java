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
import io.github.vampirestudios.hgm.item.ColoredBlockItem;
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

        IItemColor handlerItems = (s, t) -> t == 0 ? ((ColoredBlockItem) s.getItem()).color.getId() : 0xFFFFFF;
//        items.register(handlerItems, GadgetItems.FLASH_DRIVES);
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.WHITE_ROOF_LIGHT,
                GadgetBlocks.ORANGE_ROOF_LIGHT,
                GadgetBlocks.MAGENTA_ROOF_LIGHT,
                GadgetBlocks.LIGHT_BLUE_ROOF_LIGHT,
                GadgetBlocks.YELLOW_ROOF_LIGHT,
                GadgetBlocks.LIME_ROOF_LIGHT,
                GadgetBlocks.PINK_ROOF_LIGHT,
                GadgetBlocks.GRAY_ROOF_LIGHT,
                GadgetBlocks.LIGHT_GRAY_ROOF_LIGHT,
                GadgetBlocks.CYAN_ROOF_LIGHT,
                GadgetBlocks.PURPLE_ROOF_LIGHT,
                GadgetBlocks.BLUE_ROOF_LIGHT,
                GadgetBlocks.BROWN_ROOF_LIGHT,
                GadgetBlocks.GREEN_ROOF_LIGHT,
                GadgetBlocks.RED_ROOF_LIGHT,
                GadgetBlocks.BLACK_ROOF_LIGHT
        );
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.WHITE_ROUTER,
                GadgetBlocks.ORANGE_ROUTER,
                GadgetBlocks.MAGENTA_ROUTER,
                GadgetBlocks.LIGHT_BLUE_ROUTER,
                GadgetBlocks.YELLOW_ROUTER,
                GadgetBlocks.LIME_ROUTER,
                GadgetBlocks.PINK_ROUTER,
                GadgetBlocks.GRAY_ROUTER,
                GadgetBlocks.LIGHT_GRAY_ROUTER,
                GadgetBlocks.CYAN_ROUTER,
                GadgetBlocks.PURPLE_ROUTER,
                GadgetBlocks.BLUE_ROUTER,
                GadgetBlocks.BROWN_ROUTER,
                GadgetBlocks.GREEN_ROUTER,
                GadgetBlocks.RED_ROUTER,
                GadgetBlocks.BLACK_ROUTER
        );
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.WHITE_PRINTER,
                GadgetBlocks.ORANGE_PRINTER,
                GadgetBlocks.MAGENTA_PRINTER,
                GadgetBlocks.LIGHT_BLUE_PRINTER,
                GadgetBlocks.YELLOW_PRINTER,
                GadgetBlocks.LIME_PRINTER,
                GadgetBlocks.PINK_PRINTER,
                GadgetBlocks.GRAY_PRINTER,
                GadgetBlocks.LIGHT_GRAY_PRINTER,
                GadgetBlocks.CYAN_PRINTER,
                GadgetBlocks.PURPLE_PRINTER,
                GadgetBlocks.BLUE_PRINTER,
                GadgetBlocks.BROWN_PRINTER,
                GadgetBlocks.GREEN_PRINTER,
                GadgetBlocks.RED_PRINTER,
                GadgetBlocks.BLACK_PRINTER
        );
        items.register((stack, tintIndex) -> blocks.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                GadgetBlocks.WHITE_LAPTOP,
                GadgetBlocks.ORANGE_LAPTOP,
                GadgetBlocks.MAGENTA_LAPTOP,
                GadgetBlocks.LIGHT_BLUE_LAPTOP,
                GadgetBlocks.YELLOW_LAPTOP,
                GadgetBlocks.LIME_LAPTOP,
                GadgetBlocks.PINK_LAPTOP,
                GadgetBlocks.GRAY_LAPTOP,
                GadgetBlocks.LIGHT_GRAY_LAPTOP,
                GadgetBlocks.CYAN_LAPTOP,
                GadgetBlocks.PURPLE_LAPTOP,
                GadgetBlocks.BLUE_LAPTOP,
                GadgetBlocks.BROWN_LAPTOP,
                GadgetBlocks.GREEN_LAPTOP,
                GadgetBlocks.RED_LAPTOP,
                GadgetBlocks.BLACK_LAPTOP
        );

        IBlockColor handlerBlocks = (s, w, p, t) -> t == 0 ? ((BlockColored) s.getBlock()).color.getId() : 0xFFFFFF;
        blocks.register(handlerBlocks,
                GadgetBlocks.WHITE_ROOF_LIGHT,
                GadgetBlocks.ORANGE_ROOF_LIGHT,
                GadgetBlocks.MAGENTA_ROOF_LIGHT,
                GadgetBlocks.LIGHT_BLUE_ROOF_LIGHT,
                GadgetBlocks.YELLOW_ROOF_LIGHT,
                GadgetBlocks.LIME_ROOF_LIGHT,
                GadgetBlocks.PINK_ROOF_LIGHT,
                GadgetBlocks.GRAY_ROOF_LIGHT,
                GadgetBlocks.LIGHT_GRAY_ROOF_LIGHT,
                GadgetBlocks.CYAN_ROOF_LIGHT,
                GadgetBlocks.PURPLE_ROOF_LIGHT,
                GadgetBlocks.BLUE_ROOF_LIGHT,
                GadgetBlocks.BROWN_ROOF_LIGHT,
                GadgetBlocks.GREEN_ROOF_LIGHT,
                GadgetBlocks.RED_ROOF_LIGHT,
                GadgetBlocks.BLACK_ROOF_LIGHT
        );
        blocks.register(handlerBlocks,
                GadgetBlocks.WHITE_ROUTER,
                GadgetBlocks.ORANGE_ROUTER,
                GadgetBlocks.MAGENTA_ROUTER,
                GadgetBlocks.LIGHT_BLUE_ROUTER,
                GadgetBlocks.YELLOW_ROUTER,
                GadgetBlocks.LIME_ROUTER,
                GadgetBlocks.PINK_ROUTER,
                GadgetBlocks.GRAY_ROUTER,
                GadgetBlocks.LIGHT_GRAY_ROUTER,
                GadgetBlocks.CYAN_ROUTER,
                GadgetBlocks.PURPLE_ROUTER,
                GadgetBlocks.BLUE_ROUTER,
                GadgetBlocks.BROWN_ROUTER,
                GadgetBlocks.GREEN_ROUTER,
                GadgetBlocks.RED_ROUTER,
                GadgetBlocks.BLACK_ROUTER
        );
        blocks.register(handlerBlocks,
                GadgetBlocks.WHITE_PRINTER,
                GadgetBlocks.ORANGE_PRINTER,
                GadgetBlocks.MAGENTA_PRINTER,
                GadgetBlocks.LIGHT_BLUE_PRINTER,
                GadgetBlocks.YELLOW_PRINTER,
                GadgetBlocks.LIME_PRINTER,
                GadgetBlocks.PINK_PRINTER,
                GadgetBlocks.GRAY_PRINTER,
                GadgetBlocks.LIGHT_GRAY_PRINTER,
                GadgetBlocks.CYAN_PRINTER,
                GadgetBlocks.PURPLE_PRINTER,
                GadgetBlocks.BLUE_PRINTER,
                GadgetBlocks.BROWN_PRINTER,
                GadgetBlocks.GREEN_PRINTER,
                GadgetBlocks.RED_PRINTER,
                GadgetBlocks.BLACK_PRINTER
        );
        blocks.register(handlerBlocks,
                GadgetBlocks.WHITE_LAPTOP,
                GadgetBlocks.ORANGE_LAPTOP,
                GadgetBlocks.MAGENTA_LAPTOP,
                GadgetBlocks.LIGHT_BLUE_LAPTOP,
                GadgetBlocks.YELLOW_LAPTOP,
                GadgetBlocks.LIME_LAPTOP,
                GadgetBlocks.PINK_LAPTOP,
                GadgetBlocks.GRAY_LAPTOP,
                GadgetBlocks.LIGHT_GRAY_LAPTOP,
                GadgetBlocks.CYAN_LAPTOP,
                GadgetBlocks.PURPLE_LAPTOP,
                GadgetBlocks.BLUE_LAPTOP,
                GadgetBlocks.BROWN_LAPTOP,
                GadgetBlocks.GREEN_LAPTOP,
                GadgetBlocks.RED_LAPTOP,
                GadgetBlocks.BLACK_LAPTOP
        );

        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityPaper.class, new PaperRenderer());
        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityPrinter.class, new PrinterRenderer());
        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityLaptop.class, new LaptopRenderer());
        TileEntityRendererDispatcher.instance.setSpecialRenderer(TileEntityRouter.class, new RouterRenderer());
    }

}
