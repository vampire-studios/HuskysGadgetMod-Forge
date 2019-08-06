package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.api.task.TaskManager;
import io.github.vampirestudios.hgm.core.io.task.*;
import io.github.vampirestudios.hgm.core.network.task.TaskConnect;
import io.github.vampirestudios.hgm.core.network.task.TaskGetDevices;
import io.github.vampirestudios.hgm.core.network.task.TaskPing;
import io.github.vampirestudios.hgm.core.print.task.TaskPrint;
import io.github.vampirestudios.hgm.core.tasks.TaskInstallApp;
import io.github.vampirestudios.hgm.programs.system.tasks.TaskUpdateApplicationData;
import io.github.vampirestudios.hgm.programs.system.tasks.TaskUpdateSystemData;

public class GadgetTasks {

    public static void register() {
        // Core
        TaskManager.registerTask(TaskInstallApp.class);
        TaskManager.registerTask(TaskUpdateApplicationData.class);
        TaskManager.registerTask(TaskPrint.class);
        TaskManager.registerTask(TaskUpdateSystemData.class);
        TaskManager.registerTask(TaskConnect.class);
        TaskManager.registerTask(TaskPing.class);
        TaskManager.registerTask(TaskGetDevices.class);

        // Bank
        /*TaskManager.registerTask(TaskDeposit.class);
        TaskManager.registerTask(TaskWithdraw.class);
        TaskManager.registerTask(TaskGetBalance.class);
        TaskManager.registerTask(TaskPay.class);
        TaskManager.registerTask(TaskAdd.class);
        TaskManager.registerTask(TaskRemove.class);*/

        // File browser
        TaskManager.registerTask(TaskSendAction.class);
        TaskManager.registerTask(TaskSetupFileBrowser.class);
        TaskManager.registerTask(TaskGetFiles.class);
        TaskManager.registerTask(TaskGetStructure.class);
        TaskManager.registerTask(TaskGetMainDrive.class);

        // Ender Mail
        /*TaskManager.registerTask(TaskUpdateInbox.class);
        TaskManager.registerTask(TaskSendEmail.class);
        TaskManager.registerTask(TaskCheckEmailAccount.class);
        TaskManager.registerTask(TaskRegisterEmailAccount.class);
        TaskManager.registerTask(TaskDeleteEmail.class);
        TaskManager.registerTask(TaskViewEmail.class);*/

//        PrintingManager.registerPrint(new ResourceLocation(HuskysGadgetMod.MOD_ID, "picture"), ApplicationPixelShop.PicturePrint.class);
    }

}
