package io.github.vampirestudios.hgm.utils;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.util.List;

public class ItemUtils {

    public static NonNullList<ResourceLocation> getModels(ResourceLocation registryName) {
        NonNullList<ResourceLocation> models = NonNullList.withSize(16, new ResourceLocation(registryName.toString()));
        return models;
    }

    public static void addInformation(ItemStack stack, List<String> tooltip) {
        DyeColor color = DyeColor.byId(stack.getTag().getInt("color"));
        TextFormatting tf = TextFormatting.WHITE;
        try {
            Field f = DyeColor.class.getDeclaredField("chatColor");
            f.setAccessible(true);
            tf = (TextFormatting) f.get(color == DyeColor.MAGENTA ? DyeColor.PINK : color);
        } catch (Exception ignored) {
        }
        String colorName = color.getName().replace("_", " ");
        colorName = WordUtils.capitalize(colorName);
        tooltip.add("Color: " + TextFormatting.BOLD + tf.toString() + colorName);
    }

}
