package io.github.vampirestudios.hgm.programs.system.object;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

public class ColourScheme {

    private int textColour;
    private int textSecondaryColour;
    private int headerColour;
    private int backgroundColour;
    private int itemBackgroundColour;
    private int itemHighlightColour;
    private int protectedFileColour;
    private int buttonNormalColour;
    private int buttonHoveredColour;
    private int buttonDisabledColour;
    private int taskBarColour;
    private int mainApplicationBarColour;
    private int secondApplicationBarColour;
    private int applicationBackgroundColor;

    public ColourScheme() {
        resetDefault();
    }

    public static ColourScheme fromTag(CompoundNBT tag) {
        ColourScheme scheme = new ColourScheme();
        if (tag.contains("textColour", Constants.NBT.TAG_INT)) {
            scheme.textColour = tag.getInt("textColour");
        }
        if (tag.contains("textSecondaryColour", Constants.NBT.TAG_INT)) {
            scheme.textSecondaryColour = tag.getInt("textSecondaryColour");
        }
        if (tag.contains("headerColour", Constants.NBT.TAG_INT)) {
            scheme.headerColour = tag.getInt("headerColour");
        }
        if (tag.contains("backgroundColour", Constants.NBT.TAG_INT)) {
            scheme.backgroundColour = tag.getInt("backgroundColour");
        }
        if (tag.contains("itemBackgroundColour", Constants.NBT.TAG_INT)) {
            scheme.itemBackgroundColour = tag.getInt("itemBackgroundColour");
        }
        if (tag.contains("itemHighlightColour", Constants.NBT.TAG_INT)) {
            scheme.itemHighlightColour = tag.getInt("itemHighlightColour");
        }
        if (tag.contains("protectedFileColour", Constants.NBT.TAG_INT)) {
            scheme.protectedFileColour = tag.getInt("protectedFileColour");
        }
        if (tag.contains("buttonNormalColour", Constants.NBT.TAG_INT)) {
            scheme.buttonNormalColour = tag.getInt("buttonNormalColour");
        }
        if (tag.contains("buttonHoveredColour", Constants.NBT.TAG_INT)) {
            scheme.buttonHoveredColour = tag.getInt("buttonHoveredColour");
        }
        if (tag.contains("buttonDisabledColour", Constants.NBT.TAG_INT)) {
            scheme.buttonDisabledColour = tag.getInt("buttonDisabledColour");
        }
        if (tag.contains("taskBarColour", Constants.NBT.TAG_INT)) {
            scheme.taskBarColour = tag.getInt("taskBarColour");
        }
        if (tag.contains("mainApplicationBarColour", Constants.NBT.TAG_INT)) {
            scheme.mainApplicationBarColour = tag.getInt("mainApplicationBarColour");
        }
        if (tag.contains("secondApplicationBarColour", Constants.NBT.TAG_INT)) {
            scheme.secondApplicationBarColour = tag.getInt("secondApplicationBarColour");
        }
        if (tag.contains("applicationBackgroundColour", Constants.NBT.TAG_INT)) {
            scheme.applicationBackgroundColor = tag.getInt("applicationBackgroundColour");
        }
        return scheme;
    }

    public int getTextColour() {
        return textColour;
    }

    public int getHeaderColour() {
        return headerColour;
    }

    public void setHeaderColour(int headerColour) {
        this.headerColour = headerColour;
    }

    public int getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(int backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public int getItemBackgroundColour() {
        return itemBackgroundColour;
    }

    public void setItemBackgroundColour(int itemBackgroundColour) {
        this.itemBackgroundColour = itemBackgroundColour;
    }

    public int getItemHighlightColour() {
        return itemHighlightColour;
    }

    public void setItemHighlightColour(int itemHighlightColour) {
        this.itemHighlightColour = itemHighlightColour;
    }

    public int getProtectedFileColour() {
        return protectedFileColour;
    }

    public void setProtectedFileColour(int protectedFileColour) {
        this.protectedFileColour = protectedFileColour;
    }

    public int getMainApplicationBarColour() {
        return mainApplicationBarColour;
    }

    public void setMainApplicationBarColour(int mainApplicationBarColour) {
        this.mainApplicationBarColour = mainApplicationBarColour;
    }

    public int getSecondApplicationBarColour() {
        return secondApplicationBarColour;
    }

    public void setSecondApplicationBarColour(int secondApplicationBarColour) {
        this.secondApplicationBarColour = secondApplicationBarColour;
    }

    public int getApplicationBackgroundColor() {
        return applicationBackgroundColor;
    }

    public void setApplicationBackgroundColor(int applicationBackgroundColor) {
        this.applicationBackgroundColor = applicationBackgroundColor;
    }

    public int getButtonNormalColour() {
        return buttonNormalColour;
    }

    public void setButtonNormalColour(int buttonNormalColour) {
        this.buttonNormalColour = buttonNormalColour;
    }

    public int getButtonHoveredColour() {
        return buttonHoveredColour;
    }

    public void setButtonHoveredColour(int buttonHoveredColour) {
        this.buttonHoveredColour = buttonHoveredColour;
    }

    public int getButtonDisabledColour() {
        return buttonDisabledColour;
    }

    public void setButtonDisabledColour(int buttonDisabledColour) {
        this.buttonDisabledColour = buttonDisabledColour;
    }

    public int getTaskBarColour() {
        return taskBarColour;
    }

    public void setTaskBarColour(int taskBarColour) {
        this.taskBarColour = taskBarColour;
    }

    public void resetDefault() {
        textColour = 0xFFFFFFFF;
        textSecondaryColour = 0xFF9BEDF2;
        headerColour = 0xFF535861;
        backgroundColour = 0xFF3D4147;
        itemBackgroundColour = 0xFF9E9E9E;
        itemHighlightColour = 0xFF757575;
        protectedFileColour = 0xFF9BEDF2;
        buttonNormalColour = 0xFF535861;
        buttonHoveredColour = 0xFF535861;
        buttonDisabledColour = 0xFF535861;
        taskBarColour = 0xFF9E9E9E;
        mainApplicationBarColour = 0xFF9E9E9E;
        secondApplicationBarColour = 0xFF7F7F7F;
        applicationBackgroundColor = 0xFF7F7F7F;
    }

    public CompoundNBT toTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("textColour", textColour);
        tag.putInt("textSecondaryColour", textSecondaryColour);
        tag.putInt("headerColour", headerColour);
        tag.putInt("backgroundColour", backgroundColour);
        tag.putInt("itemBackgroundColour", itemBackgroundColour);
        tag.putInt("itemHighlightColour", itemHighlightColour);
        tag.putInt("buttonNormalColour", buttonNormalColour);
        tag.putInt("buttonHoveredColour", buttonHoveredColour);
        tag.putInt("buttonDisabledColour", buttonDisabledColour);
        tag.putInt("taskBarColour", taskBarColour);
        tag.putInt("mainApplicationBarColour", mainApplicationBarColour);
        tag.putInt("secondApplicationBarColour", secondApplicationBarColour);
        tag.putInt("applicationBackgroundColor", applicationBackgroundColor);

        return tag;
    }
}