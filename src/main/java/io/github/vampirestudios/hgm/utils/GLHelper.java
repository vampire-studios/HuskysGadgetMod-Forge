package io.github.vampirestudios.hgm.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.Stack;

public class GLHelper {
    public static Stack<Scissor> scissorStack = new Stack<>();

    public static void pushScissor(int x, int y, int width, int height) {
        if (scissorStack.size() > 0) {
            Scissor scissor = scissorStack.peek();
            x = Math.max(scissor.x, x);
            y = Math.max(scissor.y, y);
            width = x + width > scissor.x + scissor.width ? scissor.x + scissor.width - x : width;
            height = y + height > scissor.y + scissor.height ? scissor.y + scissor.height - y : height;
        }

        Minecraft mc = Minecraft.getInstance();
        MainWindow resolution = mc.mainWindow;
        int scale = (int) resolution.getGuiScaleFactor();
        GL11.glScissor(x * scale, mc.mainWindow.getScaledWidth() - y * scale - height * scale, Math.max(0, width * scale), Math.max(0, height * scale));
        scissorStack.push(new Scissor(x, y, width, height));
    }

    public static void popScissor() {
        if (!scissorStack.isEmpty()) {
            scissorStack.pop();
        }
        restoreScissor();
    }

    private static void restoreScissor() {
        if (!scissorStack.isEmpty()) {
            Scissor scissor = scissorStack.peek();
            Minecraft mc = Minecraft.getInstance();
            MainWindow resolution = mc.mainWindow;
            int scale = (int) resolution.getGuiScaleFactor();
            GL11.glScissor(scissor.x * scale, mc.mainWindow.getScaledWidth() - scissor.y * scale - scissor.height * scale, Math.max(0, scissor.width * scale), Math.max(0, scissor.height * scale));
        }
    }

    public static boolean isScissorStackEmpty() {
        return scissorStack.isEmpty();
    }

    /**
     * Do not call! Used for core only.
     */
    public static void clearScissorStack() {
        scissorStack.clear();
    }

    public static Color getPixel(int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        MainWindow resolution = mc.mainWindow;
        int scale = (int) resolution.getGuiScaleFactor();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
        GL11.glReadPixels(x * scale, mc.mainWindow.getScaledWidth() - y * scale - scale, 1, 1, GL11.GL_RGB, GL11.GL_FLOAT, buffer);
        return new Color(buffer.get(0), buffer.get(1), buffer.get(2));
    }

    /**
     * Sets the currently bound color to the specified color
     *
     * @param color The new color to use
     */
    public static void color(Color color) {
        color(color.getRGB());
    }

    /**
     * Sets the currently bound color to the specified color
     *
     * @param color The new color to use
     */
    public static void color(int color) {
        GlStateManager.color4f((float) ((color >> 16) & 0xFF) / 255, (float) ((color >> 8) & 0xFF) / 255, (float) ((color >> 0) & 0xFF) / 255, (float) ((color >> 24) & 0xFF) / 255);
    }

    public static class Scissor {
        public int x;
        public int y;
        public int width;
        public int height;

        Scissor(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}