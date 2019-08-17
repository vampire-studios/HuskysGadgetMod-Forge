package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = HuskysGadgetMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetBlocks {

    private static final List<Block> BLOCKS = new ArrayList<>();
    private static final List<Item> ITEMS = new ArrayList<>();

    public static final Block SERVER_TERMINAL = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "server_terminal"), new BlockServerTerminal());
    public static final Block ELECTRIC_SECURITY_FENCE = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "electric_security_fence"), new BlockElectricSecurityFence());
    public static final Block LASER_GATE = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "laser_gate"),new BlockElectricSecurityGate());
    public static final Block EASTER_EGG = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "easter_egg"),new BlockEasterEgg());

    public static final Block WHITE_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "white_roof_light"), new BlockRoofLights(DyeColor.WHITE));
    public static final Block ORANGE_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "orange_roof_light"), new BlockRoofLights(DyeColor.ORANGE));
    public static final Block MAGENTA_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "magenta_roof_light"), new BlockRoofLights(DyeColor.MAGENTA));
    public static final Block LIGHT_BLUE_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_blue_roof_light"), new BlockRoofLights(DyeColor.LIGHT_BLUE));
    public static final Block YELLOW_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "yellow_roof_light"), new BlockRoofLights(DyeColor.YELLOW));
    public static final Block LIME_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "lime_roof_light"), new BlockRoofLights(DyeColor.LIME));
    public static final Block PINK_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "pink_roof_light"), new BlockRoofLights(DyeColor.PINK));
    public static final Block GRAY_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "gray_roof_light"), new BlockRoofLights(DyeColor.GRAY));
    public static final Block LIGHT_GRAY_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_gray_roof_light"), new BlockRoofLights(DyeColor.LIGHT_GRAY));
    public static final Block CYAN_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "cyan_roof_light"), new BlockRoofLights(DyeColor.CYAN));
    public static final Block PURPLE_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "purple_roof_light"), new BlockRoofLights(DyeColor.PURPLE));
    public static final Block BLUE_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "blue_roof_light"), new BlockRoofLights(DyeColor.BLUE));
    public static final Block BROWN_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "brown_roof_light"), new BlockRoofLights(DyeColor.BROWN));
    public static final Block GREEN_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "green_roof_light"), new BlockRoofLights(DyeColor.GREEN));
    public static final Block RED_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "red_roof_light"), new BlockRoofLights(DyeColor.RED));
    public static final Block BLACK_ROOF_LIGHT = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "black_roof_light"), new BlockRoofLights(DyeColor.BLACK));

    public static final Block WHITE_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "white_router"), new BlockRouter(DyeColor.WHITE));
    public static final Block ORANGE_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "orange_router"), new BlockRouter(DyeColor.ORANGE));
    public static final Block MAGENTA_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "magenta_router"), new BlockRouter(DyeColor.MAGENTA));
    public static final Block LIGHT_BLUE_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_blue_router"), new BlockRouter(DyeColor.LIGHT_BLUE));
    public static final Block YELLOW_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "yellow_router"), new BlockRouter(DyeColor.YELLOW));
    public static final Block LIME_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "lime_router"), new BlockRouter(DyeColor.LIME));
    public static final Block PINK_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "pink_router"), new BlockRouter(DyeColor.PINK));
    public static final Block GRAY_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "gray_router"), new BlockRouter(DyeColor.GRAY));
    public static final Block LIGHT_GRAY_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_gray_router"), new BlockRouter(DyeColor.LIGHT_GRAY));
    public static final Block CYAN_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "cyan_router"), new BlockRouter(DyeColor.CYAN));
    public static final Block PURPLE_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "purple_router"), new BlockRouter(DyeColor.PURPLE));
    public static final Block BLUE_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "blue_router"), new BlockRouter(DyeColor.BLUE));
    public static final Block BROWN_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "brown_router"), new BlockRouter(DyeColor.BROWN));
    public static final Block GREEN_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "green_router"), new BlockRouter(DyeColor.GREEN));
    public static final Block RED_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "red_router"), new BlockRouter(DyeColor.RED));
    public static final Block BLACK_ROUTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "black_router"), new BlockRouter(DyeColor.BLACK));

    public static final Block WHITE_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "white_printer"), new BlockPrinter(DyeColor.WHITE));
    public static final Block ORANGE_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "orange_printer"), new BlockPrinter(DyeColor.ORANGE));
    public static final Block MAGENTA_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "magenta_printer"), new BlockPrinter(DyeColor.MAGENTA));
    public static final Block LIGHT_BLUE_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_blue_printer"), new BlockPrinter(DyeColor.LIGHT_BLUE));
    public static final Block YELLOW_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "yellow_printer"), new BlockPrinter(DyeColor.YELLOW));
    public static final Block LIME_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "lime_printer"), new BlockPrinter(DyeColor.LIME));
    public static final Block PINK_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "pink_printer"), new BlockPrinter(DyeColor.PINK));
    public static final Block GRAY_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "gray_printer"), new BlockPrinter(DyeColor.GRAY));
    public static final Block LIGHT_GRAY_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_gray_printer"), new BlockPrinter(DyeColor.LIGHT_GRAY));
    public static final Block CYAN_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "cyan_printer"), new BlockPrinter(DyeColor.CYAN));
    public static final Block PURPLE_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "purple_printer"), new BlockPrinter(DyeColor.PURPLE));
    public static final Block BLUE_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "blue_printer"), new BlockPrinter(DyeColor.BLUE));
    public static final Block BROWN_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "brown_printer"), new BlockPrinter(DyeColor.BROWN));
    public static final Block GREEN_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "green_printer"), new BlockPrinter(DyeColor.GREEN));
    public static final Block RED_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "red_printer"), new BlockPrinter(DyeColor.RED));
    public static final Block BLACK_PRINTER = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "black_printer"), new BlockPrinter(DyeColor.BLACK));

    public static final Block WHITE_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "white_laptop"), new BlockLaptop(DyeColor.WHITE));
    public static final Block ORANGE_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "orange_laptop"), new BlockLaptop(DyeColor.ORANGE));
    public static final Block MAGENTA_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "magenta_laptop"), new BlockLaptop(DyeColor.MAGENTA));
    public static final Block LIGHT_BLUE_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_blue_laptop"), new BlockLaptop(DyeColor.LIGHT_BLUE));
    public static final Block YELLOW_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "yellow_laptop"), new BlockLaptop(DyeColor.YELLOW));
    public static final Block LIME_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "lime_laptop"), new BlockLaptop(DyeColor.LIME));
    public static final Block PINK_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "pink_laptop"), new BlockLaptop(DyeColor.PINK));
    public static final Block GRAY_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "gray_laptop"), new BlockLaptop(DyeColor.GRAY));
    public static final Block LIGHT_GRAY_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "light_gray_laptop"), new BlockLaptop(DyeColor.LIGHT_GRAY));
    public static final Block CYAN_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "cyan_laptop"), new BlockLaptop(DyeColor.CYAN));
    public static final Block PURPLE_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "purple_laptop"), new BlockLaptop(DyeColor.PURPLE));
    public static final Block BLUE_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "blue_laptop"), new BlockLaptop(DyeColor.BLUE));
    public static final Block BROWN_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "brown_laptop"), new BlockLaptop(DyeColor.BROWN));
    public static final Block GREEN_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "green_laptop"), new BlockLaptop(DyeColor.GREEN));
    public static final Block RED_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "red_laptop"), new BlockLaptop(DyeColor.RED));
    public static final Block BLACK_LAPTOP = registerDevice(new ResourceLocation(HuskysGadgetMod.MOD_ID, "black_laptop"), new BlockLaptop(DyeColor.BLACK));

    public static final Block PAPER = register(new ResourceLocation(HuskysGadgetMod.MOD_ID, "paper"), new BlockPaper());

    private static Block register(ResourceLocation name, Block block)
    {
        return register(name, block, new Item.Properties().group(HuskysGadgetMod.deviceDecoration));
    }

    private static Block registerDevice(ResourceLocation name, Block block)
    {
        return register(name, block, new Item.Properties().group(HuskysGadgetMod.deviceBlocks));
    }

    private static Block register(ResourceLocation name, Block block, Item.Properties properties)
    {
        return register(name, block, new BlockItem(block, properties));
    }

    private static Block register(ResourceLocation name, Block block, BlockItem item)
    {
        block.setRegistryName(name);
        BLOCKS.add(block);
        if(block.getRegistryName() != null)
        {
            item.setRegistryName(name);
            ITEMS.add(item);
        }
        return block;
    }

    @SubscribeEvent
    public void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(block -> event.getRegistry().register(block));
    }

    @SubscribeEvent
    public void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        ITEMS.forEach(item -> event.getRegistry().register(item));
    }

}
