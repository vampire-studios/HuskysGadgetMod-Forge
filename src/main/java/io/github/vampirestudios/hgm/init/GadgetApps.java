package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.ApplicationManager;
import io.github.vampirestudios.hgm.programs.system.ApplicationAppStore;
import io.github.vampirestudios.hgm.programs.system.ApplicationSettings;
import net.minecraft.util.ResourceLocation;

public class GadgetApps {

    private static final String MOD_ID = HuskysGadgetMod.MOD_ID;

    public static void init() {
        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "settings"), ApplicationSettings.class);
//        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "bank"), ApplicationBank.class);
//        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "file_browser"), ApplicationFileBrowser.class);
//        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "note_stash"), ApplicationNoteStash.class);
//        ApplicationManagerOld.registerApplication(new ResourceLocation(Reference.MOD_ID, "pixel_mail"), ApplicationEmail.class);
        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "app_store"), ApplicationAppStore.class);
//        ApplicationManagerOld.registerApplication(new ResourceLocation(Reference.MOD_ID, "package_manager"), ApplicationPackageManager.class);
//        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "snake"), ApplicationSnake.class);
//        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "gitweb"), ApplicationGitWeb.class);

//        ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "boat_racer"), ApplicationBoatRacers.class);

        /*if(HuskyGadgetMod.isServerAdmin) {
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "server_admin"), ApplicationServerAdmin.class);
        }

        if(HuskyGadgetMod.isArtist) {
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "icons"), ApplicationIcons.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "pixel_shop"), ApplicationPixelShop.class);
        }

        if(HuskyGadgetMod.isDeveloper) {
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "text_field"), ApplicationTextArea.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "test"), ApplicationTest.class);
            if (Loader.isModLoaded("futopia") || Loader.isModLoaded("futopia2") || Loader.isModLoaded("ae2") || Loader.isModLoaded("refined_storage")) {
                ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "machine_reader"), ApplicationMachineReader.class);
            }
        }

        if(HuskyGadgetMod.likesSocialMedias) {
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "pixel_bay"), ApplicationPixelBay.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "flame_chat"), ApplicationFlameChat.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "pixel_book"), ApplicationPixelBook.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "pixel_plus"), ApplicationPixelPlus.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "cackler"), ApplicationCackler.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "pixel_tube"), ApplicationPixelTube.class);
            ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "pixel_browser"), ApplicationPixelBrowser.class);
        }*/

    }

}
