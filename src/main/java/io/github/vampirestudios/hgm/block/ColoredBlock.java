package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.utils.IBlockColorProvider;
import io.github.vampirestudios.hgm.utils.IItemColorProvider;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.DyeColor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public interface ColoredBlock extends IBlockColorProvider, IItemColorProvider {

    DyeColor getDyeColor();

    static TextFormatting getFromColor(DyeColor color) {
        switch (color) {
            case ORANGE:
            case BROWN:
                return TextFormatting.GOLD;
            case MAGENTA:
            case PINK:
                return TextFormatting.LIGHT_PURPLE;
            case LIGHT_BLUE:
                return TextFormatting.BLUE;
            case YELLOW:
                return TextFormatting.YELLOW;
            case LIME:
                return TextFormatting.GREEN;
            case GRAY:
                return TextFormatting.DARK_GRAY;
            case LIGHT_GRAY:
                return TextFormatting.GRAY;
            case CYAN:
                return TextFormatting.DARK_AQUA;
            case PURPLE:
                return TextFormatting.DARK_PURPLE;
            case BLUE:
                return TextFormatting.DARK_BLUE;
            case GREEN:
                return TextFormatting.DARK_GREEN;
            case RED:
                return TextFormatting.DARK_RED;
            case BLACK:
                return TextFormatting.BLACK;
            default:
                return TextFormatting.WHITE;
        }
    }

    default void addInformation(List<ITextComponent> tooltip) {
        String colorName = getDyeColor().getName().replace("_", " ");
        colorName = WordUtils.capitalize(colorName);
        tooltip.add(new StringTextComponent("Color: " + TextFormatting.BOLD.toString() + getFromColor(getDyeColor()).toString() + colorName));
    }

    @Override
    default IBlockColor getBlockColor() {
        return (state, worldIn, pos, tintIndex) -> DyeColor.values()[tintIndex].getId();
    }

    @Override
    default IItemColor getItemColor() {
        return (stack, tintIndex) -> DyeColor.values()[tintIndex].getId();
    }

}
