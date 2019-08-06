package io.github.vampirestudios.hgm.item;

public class ItemPixel extends BaseItem {
    public ItemPixel(String color, String type) {
        super(color + "_pixel_" + type, new Properties().maxStackSize(1));
    }
}
