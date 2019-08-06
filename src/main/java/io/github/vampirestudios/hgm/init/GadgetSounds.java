package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.gadget.Reference;
import io.github.vampirestudios.hgm.HuskysGadgetMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;

public class GadgetSounds {
    public static final SoundEvent PRINTER_PRINTING;
    public static final SoundEvent PRINTER_LOADING_PAPER;
    public static final SoundEvent FANS_BLOWING;
    public static final SoundEvent ZAP;
    public static final SoundEvent LASER;


    static {
        PRINTER_PRINTING = registerSound(Reference.RESOURCE_PREFIX + "printing_ink");
        PRINTER_LOADING_PAPER = registerSound(Reference.RESOURCE_PREFIX + "printing_paper");
        FANS_BLOWING = registerSound(Reference.RESOURCE_PREFIX + "fans_blowing");
        ZAP = registerSound(Reference.RESOURCE_PREFIX + "zap");
        LASER = registerSound(Reference.RESOURCE_PREFIX + "lasers");
    }

    private static SoundEvent registerSound(String soundNameIn) {
        ResourceLocation resource = new ResourceLocation(soundNameIn);
        SoundEvent sound = new SoundEvent(resource).setRegistryName(soundNameIn);
        RegistrationHandler.SOUNDS.add(sound);
        return sound;
    }

    @Mod.EventBusSubscriber(modid = HuskysGadgetMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {
        public static final List<SoundEvent> SOUNDS = new LinkedList<>();

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<SoundEvent> event) {
            SOUNDS.forEach(sound -> event.getRegistry().register(sound));
        }
    }
}
