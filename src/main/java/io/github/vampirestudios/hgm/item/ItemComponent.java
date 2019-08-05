package io.github.vampirestudios.hgm.item;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ItemComponent extends Item {

    public ItemComponent(String componentName) {
        super(new Properties().group(HuskysGadgetMod.deviceItems));
        setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, componentName));
    }

    public static Item getComponentFromName(String name) {
        return Registry.ITEM.getOrDefault(new ResourceLocation(HuskysGadgetMod.MOD_ID, name));
    }

}
