package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.*;
import io.github.vampirestudios.hgm.item.CustomBlockItem;
import net.minecraft.block.Block;
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
    public static final Block ROOF_LIGHTS = new BlockRoofLights();

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                SERVER_TERMINAL,
                EASTER_EGG,
                ELECTRIC_SECURITY_FENCE,
                ROOF_LIGHTS,
                LASER_GATE
        );
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new CustomBlockItem(SERVER_TERMINAL, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(ELECTRIC_SECURITY_FENCE, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(LASER_GATE, HuskysGadgetMod.deviceDecoration),
                new CustomBlockItem(ROOF_LIGHTS, HuskysGadgetMod.deviceDecoration)
        );
    }

}
