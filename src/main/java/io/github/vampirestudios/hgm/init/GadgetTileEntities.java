package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.block.entity.TileEntityEasterEgg;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hgm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetTileEntities {

    public static final TileEntityType<TileEntityEasterEgg> EASTER_EGG = TileEntityType.Builder.<TileEntityEasterEgg>create(TileEntityEasterEgg::new, GadgetBlocks.EASTER_EGG)
            .build(null);

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(EASTER_EGG);
    }

}
