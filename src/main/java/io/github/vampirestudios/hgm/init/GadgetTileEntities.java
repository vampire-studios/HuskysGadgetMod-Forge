package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.entity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hgm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetTileEntities {

    public static final TileEntityType<?> EASTER_EGG = TileEntityType.Builder.create(TileEntityEasterEgg::new, GadgetBlocks.EASTER_EGG)
            .build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "easter_egg_be"));
    public static final TileEntityType<?> PAPER = TileEntityType.Builder.create(TileEntityPaper::new, GadgetBlocks.PAPER)
            .build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "paper_be"));
    public static final TileEntityType<?> ROOF_LIGHTS = TileEntityType.Builder.create(TileEntityRoofLights::new,
            GadgetBlocks.ROOF_LIGHTS
    ).build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "roof_light_be"));
    public static final TileEntityType<?> ROUTERS = TileEntityType.Builder.create(TileEntityRouter::new,
            GadgetBlocks.ROUTERS
    ).build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "router_be"));
    public static final TileEntityType<?> PRINTERS = TileEntityType.Builder.create(TileEntityPrinter::new,
            GadgetBlocks.PRINTERS
    ).build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "printer_be"));
    public static final TileEntityType<?> LAPTOPS = TileEntityType.Builder.<TileEntityLaptop>create(TileEntityLaptop::new,
            GadgetBlocks.LAPTOPS
    ).build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "laptop_be"));

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                EASTER_EGG,
                ROOF_LIGHTS,
                ROUTERS,
                PRINTERS,
                LAPTOPS
        );
    }

}
