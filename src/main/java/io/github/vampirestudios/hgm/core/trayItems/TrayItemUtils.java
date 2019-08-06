package io.github.vampirestudios.hgm.core.trayItems;

import io.github.vampirestudios.hgm.api.app.Layout;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;

public class TrayItemUtils {

    public static Layout createMenu(int menuWidth, int menuHeight) {
        Layout layout = new Layout.Context(menuWidth, menuHeight);
        layout.yPosition = 70;
        layout.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
                Screen.fill(x, y, x + width, y + height, new Color(0.65F, 0.65F, 0.65F, 0.9F).getRGB()));
        return layout;
    }

}
