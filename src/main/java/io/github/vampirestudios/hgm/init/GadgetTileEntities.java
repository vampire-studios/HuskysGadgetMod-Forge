package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.entity.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "hgm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetTileEntities {

    private static final List<TileEntityType> TILE_ENTITY_TYPES = new ArrayList<>();

    public static final TileEntityType<?> EASTER_EGG = buildType(new ResourceLocation(HuskysGadgetMod.MOD_ID, "easter_egg_be"),
            TileEntityType.Builder.create(TileEntityEasterEgg::new, GadgetBlocks.EASTER_EGG));
    public static final TileEntityType<?> PAPER = buildType(new ResourceLocation(HuskysGadgetMod.MOD_ID, "paper_be"),
            TileEntityType.Builder.create(TileEntityPaper::new, GadgetBlocks.PAPER));
    public static final TileEntityType<?> ROOF_LIGHTS = buildType(new ResourceLocation(HuskysGadgetMod.MOD_ID, "roof_light_be"), TileEntityType.Builder.create(TileEntityRoofLights::new,
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
    ));
    public static final TileEntityType<?> ROUTERS = buildType(new ResourceLocation(HuskysGadgetMod.MOD_ID, "router_be"), TileEntityType.Builder.create(TileEntityRouter::new,
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
    ));
    public static final TileEntityType<?> PRINTERS = buildType(new ResourceLocation(HuskysGadgetMod.MOD_ID, "printer_be"), TileEntityType.Builder.create(TileEntityPrinter::new,
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
    ));
    public static final TileEntityType<?> LAPTOPS = buildType(new ResourceLocation(HuskysGadgetMod.MOD_ID, "laptop_be"), TileEntityType.Builder.create(TileEntityLaptop::new,
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
    ));

    private static <T extends TileEntity> TileEntityType<T> buildType(ResourceLocation id, TileEntityType.Builder<T> builder)
    {
        TileEntityType<T> type = builder.build(null); //TODO may not allow null
        type.setRegistryName(id);
        TILE_ENTITY_TYPES.add(type);
        return type;
    }

    @SubscribeEvent
    public void onTileEntitiesRegistered(final RegistryEvent.Register<TileEntityType<?>> event) {
        TILE_ENTITY_TYPES.forEach(tileEntityType -> event.getRegistry().register(tileEntityType));
        TILE_ENTITY_TYPES.clear();
    }

}
