package io.github.vampirestudios.hgm.item;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class BaseItem extends Item {
    public BaseItem(String name) {
        super(new Properties().group(HuskysGadgetMod.deviceItems));
        this.setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, name));
    }
}
