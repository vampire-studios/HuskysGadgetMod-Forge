package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.block.BlockEasterEgg;
import io.github.vampirestudios.hgm.block.BlockElectricSecurityFence;
import io.github.vampirestudios.hgm.block.BlockServerTerminal;
import io.github.vampirestudios.hgm.item.CustomBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hgm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetBlocks {

    public static final Block SERVER_TERMINAL = new BlockServerTerminal();
    public static final Block ELECTRIC_SECURITY_FENCE = new BlockElectricSecurityFence();
    public static final Block EASTER_EGG = new BlockEasterEgg();

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(SERVER_TERMINAL, EASTER_EGG);
        event.getRegistry().registerAll(ELECTRIC_SECURITY_FENCE);
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new CustomBlockItem(SERVER_TERMINAL, ItemGroup.BUILDING_BLOCKS));
        event.getRegistry().registerAll(new CustomBlockItem(ELECTRIC_SECURITY_FENCE, ItemGroup.BUILDING_BLOCKS));
    }

}
