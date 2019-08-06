package io.github.vampirestudios.hgm.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.core.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

public class GuiButtonWindow extends Button {

    protected Minecraft minecraft = Minecraft.getInstance();

    public GuiButtonWindow(int x, int y) {
        super(x, y, 11, 11, "", p_onPress_1_ -> {

        });
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.minecraft.getTextureManager().bindTexture(Window.WINDOW_GUI);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            int state = this.getHoverState(this.isHovered);
            this.blit(this.x, this.y, state * this.width + 26, 2 * this.height, this.width, this.height);
        }
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.active)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }
}
