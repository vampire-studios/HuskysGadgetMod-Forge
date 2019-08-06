package io.github.vampirestudios.hgm;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_LAPTOP = "laptop-settings";
    public static final String CATEGORY_ROUTER = "router-settings";
    public static final String CATEGORY_PRINTING = "printer-settings";
    public static final String CATEGORY_APPLICATIONS = "applications-settings";
    public static final String SUBCATEGORY_PIXEL_PAINTER = "pixel-shop";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    private static ForgeConfigSpec.IntValue PING_RATE;
    private static ForgeConfigSpec.IntValue SIGNAL_RANGE;
    private static ForgeConfigSpec.IntValue BEACON_INTERVAL;
    private static ForgeConfigSpec.IntValue MAX_DEVICES;
    private static ForgeConfigSpec.BooleanValue OVERRIDE_PRINT_SPEED;
    private static ForgeConfigSpec.IntValue CUSTOM_PRINT_SPEED;
    private static ForgeConfigSpec.IntValue MAX_PAPER_COUNT;
    private static ForgeConfigSpec.BooleanValue RENDER_PRINTED_3D;


    static {
        COMMON_BUILDER.comment("Laptop Settings").push(CATEGORY_LAPTOP);
        setupLaptopConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Router Settings").push(CATEGORY_ROUTER);
        setupRouterConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Printer Settings").push(CATEGORY_PRINTING);
        setupPrinterConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Application Settings").push(CATEGORY_APPLICATIONS);
        setupApplicationConfig();
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupLaptopConfig() {
        COMMON_BUILDER.push(CATEGORY_LAPTOP);

        PING_RATE = COMMON_BUILDER.comment("The amount of ticks the laptop waits until sending another ping to it's connected router.")
                .defineInRange("pingRate", 20, 1, 200);

        COMMON_BUILDER.pop();
    }


    private static void setupRouterConfig() {
        COMMON_BUILDER.push(CATEGORY_ROUTER);

        SIGNAL_RANGE = COMMON_BUILDER.comment("The range that routers can produce a signal to devices. This is the radius in blocks. Be careful when increasing this value, the performance is O(n^3) and larger numbers will have a bigger impact on the server")
                .defineInRange("signalRange", 20, 10, 100);
        BEACON_INTERVAL = COMMON_BUILDER.comment("The amount of ticks the router waits before sending out a beacon signal. Higher number will increase performance but devices won't know as quick if they lost connection.")
                .defineInRange("beaconInterval", 20, 1, 200);
        MAX_DEVICES = COMMON_BUILDER.comment("The maximum amount of devices that can be connected to the router.")
                .defineInRange("maxDevices", 16, 1, 1000);

        COMMON_BUILDER.pop();
    }

    private static void setupPrinterConfig() {
        COMMON_BUILDER.push(CATEGORY_PRINTING);

        OVERRIDE_PRINT_SPEED = COMMON_BUILDER.comment("If enabled, overrides all printing times with customPrintSpeed property")
                .define("overridePrintSpeed", false);
        CUSTOM_PRINT_SPEED = COMMON_BUILDER.comment("The amount of seconds it should take for the printer to addToQueue a document")
                .defineInRange("customPrintSpeed", 20, 1, 600);
        MAX_PAPER_COUNT = COMMON_BUILDER.comment("The amount of paper that can be loaded into the printer")
                .defineInRange("maxPaperCount", 64, 0, 99);

        COMMON_BUILDER.pop();
    }

    private static void setupApplicationConfig() {
        COMMON_BUILDER.comment("Pixelshop Settings").push(SUBCATEGORY_PIXEL_PAINTER);

        RENDER_PRINTED_3D = COMMON_BUILDER.comment("Should the pixels on printed pictures render in 3D. Warning, this will decrease the performance of the game. You should not enable if you have a slow computer!")
                .define("render-printed-in-3d", false);

        COMMON_BUILDER.pop();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {

    }

    public static void readSyncTag(CompoundNBT tag) {
        if (tag.contains("pingRate", Constants.NBT.TAG_INT)) {
            PING_RATE.set(tag.getInt("pingRate"));
        }
        if (tag.contains("signalRange", Constants.NBT.TAG_INT)) {
            SIGNAL_RANGE.set(tag.getInt("signalRange"));
        }
    }

    public static CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("pingRate", PING_RATE.get());
        tag.putInt("signalRange", SIGNAL_RANGE.get());
        return tag;
    }

    public static int getPingRate() {
        return PING_RATE.get();
    }

    public static int getSignalRange() {
        return SIGNAL_RANGE.get();
    }

    public static int getBeaconInterval() {
        return BEACON_INTERVAL.get();
    }

    public static int getMaxDevices() {
        return MAX_DEVICES.get();
    }

    public static boolean isOverridePrintSpeed() {
        return OVERRIDE_PRINT_SPEED.get();
    }

    public static int getCustomPrintSpeed() {
        return CUSTOM_PRINT_SPEED.get();
    }

    public static int getMaxPaperCount() {
        return MAX_PAPER_COUNT.get();
    }

    public static boolean isRenderPrinted3D() {
        return RENDER_PRINTED_3D.get();
    }


}
