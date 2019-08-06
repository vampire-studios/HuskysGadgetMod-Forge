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

    public static final Block[] ROOF_LIGHTS = new Block[16];
    public static final Block[] ROUTERS = new Block[16];
    public static final Block[] PRINTERS = new Block[16];
    public static final Block[] LAPTOPS = new Block[16];

    public static final Block PAPER = new BlockPaper();

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        for (DyeColor color : DyeColor.values()) {
            ROOF_LIGHTS[color.getId()] = new BlockRoofLights(color);
            ROUTERS[color.getId()] = new BlockRouter(color);
            PRINTERS[color.getId()] = new BlockPrinter(color);
            LAPTOPS[color.getId()] = new BlockLaptop(color);
        }
        event.getRegistry().registerAll(ROOF_LIGHTS);
        event.getRegistry().registerAll(ROUTERS);
        event.getRegistry().registerAll(PRINTERS);
        event.getRegistry().registerAll(LAPTOPS);
        event.getRegistry().registerAll(
                SERVER_TERMINAL,
                EASTER_EGG,
                ELECTRIC_SECURITY_FENCE,
                LASER_GATE,
                PAPER
        );
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        for (Block block : ROOF_LIGHTS) {
            new CustomBlockItem(block, HuskysGadgetMod.deviceDecoration);
        }
        for (Block block : ROUTERS) {
            new CustomBlockItem(block, HuskysGadgetMod.deviceDecoration);
        }
        for (Block block : PRINTERS) {
            new CustomBlockItem(block, HuskysGadgetMod.deviceDecoration);
        }
        for (Block block : LAPTOPS) {
            new CustomBlockItem(block, HuskysGadgetMod.deviceDecoration);
        }
        event.getRegistry().registerAll(
                new CustomBlockItem(SERVER_TERMINAL, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(ELECTRIC_SECURITY_FENCE, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(LASER_GATE, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(PAPER, HuskysGadgetMod.deviceBlocks)
        );
    }

}
