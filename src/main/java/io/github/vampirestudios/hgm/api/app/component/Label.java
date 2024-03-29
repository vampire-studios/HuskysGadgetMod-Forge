package io.github.vampirestudios.hgm.api.app.component;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.api.app.Component;
import io.github.vampirestudios.hgm.core.BaseDevice;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class Label extends Component {

    protected String text;
    protected int width;
    protected boolean shadow = true;
    protected double scale = 1;
    protected int alignment = ALIGN_LEFT;

    protected int textColor = Color.WHITE.getRGB();

    /**
     * Default label constructor
     *
     * @param text the text to display
     * @param left how many pixels from the left
     * @param top  how many pixels from the top
     */
    public Label(String text, int left, int top) {
        super(left, top);
        this.text = text;
    }

    @Override
    public void render(BaseDevice laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
        if (this.visible) {
            GlStateManager.pushMatrix();
            {
                GlStateManager.translatef(xPosition, yPosition, 0);
                GlStateManager.scaled(scale, scale, scale);
                if (alignment == ALIGN_RIGHT)
                    GlStateManager.translatef((int) -(mc.fontRenderer.getStringWidth(text) * scale), 0, 0);
                if (alignment == ALIGN_CENTER)
                    GlStateManager.translatef((int) -(mc.fontRenderer.getStringWidth(text) * scale) / (int) (2 * scale), 0, 0);
                if (shadow)
                    BaseDevice.fontRenderer.drawStringWithShadow(text, 0, 0, textColor);
                else
                    BaseDevice.fontRenderer.drawString(text, 0, 0, textColor);
            }
            GlStateManager.popMatrix();
        }
    }

    /**
     * Sets the text in the label
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets the text color for this component
     *
     * @param color the text color
     */
    public void setTextColor(Color color) {
        this.textColor = color.getRGB();
    }

    /**
     * Sets the whether shadow should show under the text
     *
     * @param shadow if should render shadow
     */
    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    /**
     * Scales the text, essentially setting the font size. Minecraft
     * does not support proper font resizing. The default scale is 1
     *
     * @param scale the text scale
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Sets the alignment of the text. Use {@link Component#ALIGN_LEFT} or
     * {@link Component#ALIGN_RIGHT} to set alignment.
     *
     * @param alignment the alignment type
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }
}