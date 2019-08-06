package io.github.vampirestudios.hgm.api.app.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

public abstract class ListItemRenderer<E> {
    private final int height;

    public ListItemRenderer(int height) {
        this.height = height;
    }

    public final int getHeight() {
        return height;
    }

    public abstract void render(E e, Screen gui, Minecraft mc, int x, int y, int width, int height, boolean selected);
}
