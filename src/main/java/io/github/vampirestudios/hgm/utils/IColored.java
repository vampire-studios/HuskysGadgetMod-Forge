package io.github.vampirestudios.hgm.utils;

import net.minecraft.item.DyeColor;

public interface IColored {
    DyeColor getColor();

    void setColor(DyeColor color);
}