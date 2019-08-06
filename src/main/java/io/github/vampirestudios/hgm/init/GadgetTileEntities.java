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

    public static final TileEntityType<TileEntityEasterEgg> EASTER_EGG = (TileEntityType<TileEntityEasterEgg>) TileEntityType.Builder.create(TileEntityEasterEgg::new, GadgetBlocks.EASTER_EGG)
            .build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "easter_egg_be"));
    public static final TileEntityType<TileEntityPaper> PAPER = (TileEntityType<TileEntityPaper>) TileEntityType.Builder.create(TileEntityPaper::new, GadgetBlocks.PAPER)
            .build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "paper_be"));
    public static final TileEntityType<TileEntityRoofLights> ROOF_LIGHTS = (TileEntityType<TileEntityRoofLights>) TileEntityType.Builder.create(TileEntityRoofLights::new,
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
    ).build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "roof_light_be"));
    public static final TileEntityType<TileEntityRouter> ROUTERS = (TileEntityType<TileEntityRouter>) TileEntityType.Builder.create(TileEntityRouter::new,
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
    ).build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "router_be"));
    public static final TileEntityType<TileEntityPrinter> PRINTERS = (TileEntityType<TileEntityPrinter>) TileEntityType.Builder.create(TileEntityPrinter::new,
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
    ).build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "printer_be"));

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                EASTER_EGG,
                ROOF_LIGHTS,
                ROUTERS
        );
    }

}
