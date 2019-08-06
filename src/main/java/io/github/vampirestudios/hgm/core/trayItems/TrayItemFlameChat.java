package io.github.vampirestudios.hgm.core.trayItems;

import io.github.vampirestudios.hgm.api.app.Layout;
import io.github.vampirestudios.hgm.api.app.emojies.Icons;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.object.TrayItem;

public class TrayItemFlameChat extends TrayItem {

    private static Layout layout = TrayItemUtils.createMenu(100, 100);

    public TrayItemFlameChat() {
        super(Icons.ONLINE);
    }

    @Override
    public void init() {
        this.setClickListener((mouseX, mouseY, mouseButton) -> {
            if (BaseDevice.getSystem().hasContext()) {
                BaseDevice.getSystem().closeContext();
            } else {
                BaseDevice.getSystem().openContext(layout, layout.width + 100, layout.height - 80);
            }
        });
    }

}