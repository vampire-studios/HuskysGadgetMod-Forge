package io.github.vampirestudios.hgm;

import io.github.vampirestudios.hgm.proxy.ClientProxy;
import io.github.vampirestudios.hgm.proxy.IProxy;
import io.github.vampirestudios.hgm.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("hgm")
public class HuskysGadgetMod {

    public static final String MOD_ID = "hgm";
    public static final String MOD_NAME = "Husky's Gadget Mod";
    public static final Logger LOGGER = LogManager.getLogger(String.format("[%s]", MOD_NAME));
    public static final ModSetup setup = new ModSetup();
    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static ItemGroup deviceBlocks = new DeviceTab("gadgetBlocks");
    public static ItemGroup deviceItems = new DeviceTab("gadgetItems");
    public static ItemGroup deviceDecoration = new DeviceTab("gadgetDecoration");

    public HuskysGadgetMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

    }

    private void setup(final FMLCommonSetupEvent event) {
        proxy.init();
    }

}
