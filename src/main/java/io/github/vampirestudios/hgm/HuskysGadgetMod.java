package io.github.vampirestudios.hgm;

import io.github.vampirestudios.hgm.init.GadgetApps;
import io.github.vampirestudios.hgm.init.GadgetTasks;
import io.github.vampirestudios.hgm.network.PacketHandler;
import io.github.vampirestudios.hgm.proxy.ClientProxy;
import io.github.vampirestudios.hgm.proxy.IProxy;
import io.github.vampirestudios.hgm.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("hgm-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        GadgetApps.init();
        GadgetTasks.register();
        proxy.init(event);
    }

}
