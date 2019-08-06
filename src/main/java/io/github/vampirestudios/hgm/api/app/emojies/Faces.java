package io.github.vampirestudios.hgm.api.app.emojies;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.app.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Timor Morrien
 */
public enum Faces implements IIcon {

    STEVE,
    ALEX,
    SICK,
    DEVIL1,
    DEVIL2,
    PUMPKIN,
    CLOWN,
    CREYFUSH,
    ALIEN,
    QBERT;

    private static final ResourceLocation ICON_ASSET = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/icon_packs/other_faces.png");

    private static final int ICON_SIZE = 10;
    private static final int GRID_SIZE = 20;

    @Override
    public ResourceLocation getIconAsset() {
        return ICON_ASSET;
    }

    @Override
    public int getIconSize() {
        return ICON_SIZE;
    }

    @Override
    public int getGridWidth() {
        return GRID_SIZE;
    }

    @Override
    public int getGridHeight() {
        return GRID_SIZE;
    }

    @Override
    public int getU() {
        return (ordinal() % GRID_SIZE) * ICON_SIZE;
    }

    @Override
    public int getV() {
        return (ordinal() / GRID_SIZE) * ICON_SIZE;
    }
}
