package io.github.vampirestudios.hgm.api.app;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.api.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public interface IIcon {

    ResourceLocation getIconAsset();

    int getIconSize();

    int getGridWidth();

    int getGridHeight();

    int getU();

    int getV();

    default void draw(Minecraft mc, int x, int y) {
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(getIconAsset());
        int size = getIconSize();
        int assetWidth = getGridWidth() * size;
        int assetHeight = getGridHeight() * size;
        RenderUtil.drawRectWithTexture(x, y, getU(), getV(), size, size, size, size, assetWidth, assetHeight);
    }
}
