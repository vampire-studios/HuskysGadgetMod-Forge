package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.entity.TileEntityEasterEgg;
import io.github.vampirestudios.hgm.block.entity.TileEntityRoofLights;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hgm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetTileEntities {

    public static final TileEntityType<TileEntityEasterEgg> EASTER_EGG = (TileEntityType<TileEntityEasterEgg>) TileEntityType.Builder.create(TileEntityEasterEgg::new, GadgetBlocks.EASTER_EGG)
            .build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "easter_egg_be"));
    public static final TileEntityType<TileEntityRoofLights> ROOF_LIGHTS = (TileEntityType<TileEntityRoofLights>) TileEntityType.Builder.create(TileEntityRoofLights::new, GadgetBlocks.ROOF_LIGHTS)
            .build(null).setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "roof_light_be"));

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(EASTER_EGG);
    }

}
