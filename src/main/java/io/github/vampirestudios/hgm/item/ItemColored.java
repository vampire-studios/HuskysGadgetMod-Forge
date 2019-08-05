package io.github.vampirestudios.hgm.item;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.utils.IItemColorProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public class ItemColored extends Item implements IItemColorProvider {

    public DyeColor color;

    public ItemColored(String name, DyeColor color) {
        super(new Properties().group(HuskysGadgetMod.deviceItems));
        setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, String.format("%s_%s", color.getName(), name)));
        this.color = color;
    }

    private static TextFormatting getFromColor(DyeColor color) {
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

    @Override
    public void addInformation(ItemStack stack, World player, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(new StringTextComponent("Hold " + TextFormatting.BOLD + getFromColor(color) + "SHIFT " + TextFormatting.GRAY + "for more information"));
        } else {
            String colorName = color.getName().replace("_", " ");
            colorName = WordUtils.capitalize(colorName);
            tooltip.add(new StringTextComponent("Color: " + TextFormatting.BOLD.toString() + getFromColor(color).toString() + colorName));
        }
    }

    @Override
    public IItemColor getItemColor() {
        return (stack, tintIndex) -> DyeColor.values()[tintIndex].getId();
    }

}