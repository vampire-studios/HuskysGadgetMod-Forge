package io.github.vampirestudios.hgm.api.app.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

public abstract class ItemRenderer<E> {
    public abstract void render(E e, Screen gui, Minecraft mc, int x, int y, int width, int height);
}
