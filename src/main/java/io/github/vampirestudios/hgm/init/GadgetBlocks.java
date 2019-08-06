package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.*;
import io.github.vampirestudios.hgm.item.CustomBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hgm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetBlocks {

    public static final Block SERVER_TERMINAL = new BlockServerTerminal();
    public static final Block ELECTRIC_SECURITY_FENCE = new BlockElectricSecurityFence();
    public static final Block LASER_GATE = new BlockElectricSecurityGate();
    public static final Block EASTER_EGG = new BlockEasterEgg();

    public static final Block WHITE_ROOF_LIGHT = new BlockRoofLights(DyeColor.WHITE);
    public static final Block ORANGE_ROOF_LIGHT = new BlockRoofLights(DyeColor.ORANGE);
    public static final Block MAGENTA_ROOF_LIGHT = new BlockRoofLights(DyeColor.MAGENTA);
    public static final Block LIGHT_BLUE_ROOF_LIGHT = new BlockRoofLights(DyeColor.LIGHT_BLUE);
    public static final Block YELLOW_ROOF_LIGHT = new BlockRoofLights(DyeColor.YELLOW);
    public static final Block LIME_ROOF_LIGHT = new BlockRoofLights(DyeColor.LIME);
    public static final Block PINK_ROOF_LIGHT = new BlockRoofLights(DyeColor.PINK);
    public static final Block GRAY_ROOF_LIGHT = new BlockRoofLights(DyeColor.GRAY);
    public static final Block LIGHT_GRAY_ROOF_LIGHT = new BlockRoofLights(DyeColor.LIGHT_GRAY);
    public static final Block CYAN_ROOF_LIGHT = new BlockRoofLights(DyeColor.CYAN);
    public static final Block PURPLE_ROOF_LIGHT = new BlockRoofLights(DyeColor.PURPLE);
    public static final Block BLUE_ROOF_LIGHT = new BlockRoofLights(DyeColor.BLUE);
    public static final Block BROWN_ROOF_LIGHT = new BlockRoofLights(DyeColor.BROWN);
    public static final Block GREEN_ROOF_LIGHT = new BlockRoofLights(DyeColor.GREEN);
    public static final Block RED_ROOF_LIGHT = new BlockRoofLights(DyeColor.RED);
    public static final Block BLACK_ROOF_LIGHT = new BlockRoofLights(DyeColor.BLACK);

    public static final Block WHITE_ROUTER = new BlockRoofLights(DyeColor.WHITE);
    public static final Block ORANGE_ROUTER = new BlockRoofLights(DyeColor.ORANGE);
    public static final Block MAGENTA_ROUTER = new BlockRoofLights(DyeColor.MAGENTA);
    public static final Block LIGHT_BLUE_ROUTER = new BlockRoofLights(DyeColor.LIGHT_BLUE);
    public static final Block YELLOW_ROUTER = new BlockRoofLights(DyeColor.YELLOW);
    public static final Block LIME_ROUTER = new BlockRoofLights(DyeColor.LIME);
    public static final Block PINK_ROUTER = new BlockRoofLights(DyeColor.PINK);
    public static final Block GRAY_ROUTER = new BlockRoofLights(DyeColor.GRAY);
    public static final Block LIGHT_GRAY_ROUTER = new BlockRoofLights(DyeColor.LIGHT_GRAY);
    public static final Block CYAN_ROUTER = new BlockRoofLights(DyeColor.CYAN);
    public static final Block PURPLE_ROUTER = new BlockRoofLights(DyeColor.PURPLE);
    public static final Block BLUE_ROUTER = new BlockRoofLights(DyeColor.BLUE);
    public static final Block BROWN_ROUTER = new BlockRoofLights(DyeColor.BROWN);
    public static final Block GREEN_ROUTER = new BlockRoofLights(DyeColor.GREEN);
    public static final Block RED_ROUTER = new BlockRoofLights(DyeColor.RED);
    public static final Block BLACK_ROUTER = new BlockRoofLights(DyeColor.BLACK);

    public static final Block WHITE_PRINTER = new BlockRoofLights(DyeColor.WHITE);
    public static final Block ORANGE_PRINTER = new BlockRoofLights(DyeColor.ORANGE);
    public static final Block MAGENTA_PRINTER = new BlockRoofLights(DyeColor.MAGENTA);
    public static final Block LIGHT_BLUE_PRINTER = new BlockRoofLights(DyeColor.LIGHT_BLUE);
    public static final Block YELLOW_PRINTER = new BlockRoofLights(DyeColor.YELLOW);
    public static final Block LIME_PRINTER = new BlockRoofLights(DyeColor.LIME);
    public static final Block PINK_PRINTER = new BlockRoofLights(DyeColor.PINK);
    public static final Block GRAY_PRINTER = new BlockRoofLights(DyeColor.GRAY);
    public static final Block LIGHT_GRAY_PRINTER = new BlockRoofLights(DyeColor.LIGHT_GRAY);
    public static final Block CYAN_PRINTER = new BlockRoofLights(DyeColor.CYAN);
    public static final Block PURPLE_PRINTER = new BlockRoofLights(DyeColor.PURPLE);
    public static final Block BLUE_PRINTER = new BlockRoofLights(DyeColor.BLUE);
    public static final Block BROWN_PRINTER = new BlockRoofLights(DyeColor.BROWN);
    public static final Block GREEN_PRINTER = new BlockRoofLights(DyeColor.GREEN);
    public static final Block RED_PRINTER = new BlockRoofLights(DyeColor.RED);
    public static final Block BLACK_PRINTER = new BlockRoofLights(DyeColor.BLACK);

    public static final Block PAPER = new BlockPaper();

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                SERVER_TERMINAL,
                EASTER_EGG,
                ELECTRIC_SECURITY_FENCE,
                LASER_GATE,
                WHITE_ROOF_LIGHT,
                ORANGE_ROOF_LIGHT,
                MAGENTA_ROOF_LIGHT,
                LIGHT_BLUE_ROOF_LIGHT,
                YELLOW_ROOF_LIGHT,
                LIME_ROOF_LIGHT,
                PINK_ROOF_LIGHT,
                GRAY_ROOF_LIGHT,
                LIGHT_GRAY_ROOF_LIGHT,
                CYAN_ROOF_LIGHT,
                PURPLE_ROOF_LIGHT,
                BLUE_ROOF_LIGHT,
                BROWN_ROOF_LIGHT,
                GREEN_ROOF_LIGHT,
                RED_ROOF_LIGHT,
                BLACK_ROOF_LIGHT,

                WHITE_ROUTER,
                ORANGE_ROUTER,
                MAGENTA_ROUTER,
                LIGHT_BLUE_ROUTER,
                YELLOW_ROUTER,
                LIME_ROUTER,
                PINK_ROUTER,
                GRAY_ROUTER,
                LIGHT_GRAY_ROUTER,
                CYAN_ROUTER,
                PURPLE_ROUTER,
                BLUE_ROUTER,
                BROWN_ROUTER,
                GREEN_ROUTER,
                RED_ROUTER,
                BLACK_ROUTER,

                WHITE_PRINTER,
                ORANGE_PRINTER,
                MAGENTA_PRINTER,
                LIGHT_BLUE_PRINTER,
                YELLOW_PRINTER,
                LIME_PRINTER,
                PINK_PRINTER,
                GRAY_PRINTER,
                LIGHT_GRAY_PRINTER,
                CYAN_PRINTER,
                PURPLE_PRINTER,
                BLUE_PRINTER,
                BROWN_PRINTER,
                GREEN_PRINTER,
                RED_PRINTER,
                BLACK_PRINTER,

                PAPER
        );
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new CustomBlockItem(SERVER_TERMINAL, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(ELECTRIC_SECURITY_FENCE, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(LASER_GATE, HuskysGadgetMod.deviceDecoration),

                new CustomBlockItem(WHITE_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(ORANGE_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(MAGENTA_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(LIGHT_BLUE_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(YELLOW_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(LIME_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(PINK_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(GRAY_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(LIGHT_GRAY_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(CYAN_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(PURPLE_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(BLUE_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(BROWN_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(GREEN_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(RED_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(BLACK_ROOF_LIGHT, HuskysGadgetMod.deviceDecoration),

                new CustomBlockItem(WHITE_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(ORANGE_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(MAGENTA_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(LIGHT_BLUE_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(YELLOW_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(LIME_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(PINK_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(GRAY_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(LIGHT_GRAY_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(CYAN_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(PURPLE_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(BLUE_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(BROWN_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(GREEN_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(RED_ROUTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(BLACK_ROUTER, HuskysGadgetMod.deviceBlocks),

                new CustomBlockItem(WHITE_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(ORANGE_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(MAGENTA_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(LIGHT_BLUE_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(YELLOW_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(LIME_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(PINK_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(GRAY_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(LIGHT_GRAY_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(CYAN_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(PURPLE_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(BLUE_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(BROWN_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(GREEN_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(RED_PRINTER, HuskysGadgetMod.deviceBlocks),
                new CustomBlockItem(BLACK_PRINTER, HuskysGadgetMod.deviceBlocks),

                new CustomBlockItem(PAPER, HuskysGadgetMod.deviceBlocks)
        );
    }

}
