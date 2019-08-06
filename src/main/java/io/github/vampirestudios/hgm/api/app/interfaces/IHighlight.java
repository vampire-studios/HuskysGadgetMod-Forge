package io.github.vampirestudios.hgm.api.app.interfaces;

import net.minecraft.util.text.TextFormatting;

public interface IHighlight {
    TextFormatting[] getKeywordFormatting(String text);
}
