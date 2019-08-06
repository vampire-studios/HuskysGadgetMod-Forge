package io.github.vampirestudios.hgm.core;

import io.github.vampirestudios.hgm.api.os.OperatingSystem;
import io.github.vampirestudios.hgm.programs.system.object.ColourScheme;
import net.minecraft.nbt.CompoundNBT;

public class Settings {

    private static boolean showAllApps = false;

    private ColourScheme colourScheme = new ColourScheme();

    private String hasWallpaperOrColor, taskBarPlacement;

    private OperatingSystem OS;

    public static boolean isShowAllApps() {
        return Settings.showAllApps;
    }

    public static void setShowAllApps(boolean showAllApps) {
        Settings.showAllApps = showAllApps;
    }

    public static Settings fromTag(CompoundNBT tag) {
        showAllApps = tag.getBoolean("showAllApps");

        Settings settings = new Settings();
        settings.colourScheme = ColourScheme.fromTag(tag.getCompound("colourScheme"));
        settings.hasWallpaperOrColor = "Wallpaper";
        settings.taskBarPlacement = "Bottom";
        return settings;
    }

    public OperatingSystem getOS() {
        return OS;
    }

    public void setOS(OperatingSystem OS) {
        this.OS = OS;
    }

    public String hasWallpaperOrColor() {
        return hasWallpaperOrColor;
    }

    public void setHasWallpaperOrColor(String hasWallpaperOrColor) {
        this.hasWallpaperOrColor = hasWallpaperOrColor;
    }

    public String getTaskBarPlacement() {
        return taskBarPlacement;
    }

    public void setTaskBarPlacement(String taskBarPlacement) {
        this.taskBarPlacement = taskBarPlacement;
    }

    public ColourScheme getColourScheme() {
        return colourScheme;
    }

    public CompoundNBT toTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("showAllApps", showAllApps);
        tag.put("colourScheme", colourScheme.toTag());
        tag.putString("wallpaperOrColor", hasWallpaperOrColor);
        tag.putString("taskBarPlacement", taskBarPlacement);
        return tag;
    }
}