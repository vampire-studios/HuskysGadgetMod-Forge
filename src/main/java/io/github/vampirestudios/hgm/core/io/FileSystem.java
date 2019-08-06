package io.github.vampirestudios.hgm.core.io;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.ApplicationManager;
import io.github.vampirestudios.hgm.api.app.Application;
import io.github.vampirestudios.hgm.api.io.Drive;
import io.github.vampirestudios.hgm.api.io.Folder;
import io.github.vampirestudios.hgm.api.task.Callback;
import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.api.task.TaskManager;
import io.github.vampirestudios.hgm.block.entity.TileEntityBaseDevice;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.core.io.action.FileAction;
import io.github.vampirestudios.hgm.core.io.drive.AbstractDrive;
import io.github.vampirestudios.hgm.core.io.drive.ExternalDrive;
import io.github.vampirestudios.hgm.core.io.drive.InternalDrive;
import io.github.vampirestudios.hgm.core.io.task.TaskGetFiles;
import io.github.vampirestudios.hgm.core.io.task.TaskGetMainDrive;
import io.github.vampirestudios.hgm.core.io.task.TaskSendAction;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

public class FileSystem {
    public static final Pattern PATTERN_FILE_NAME = Pattern.compile("^[\\w'. _]{1,32}$");
    public static final Pattern PATTERN_DIRECTORY = Pattern.compile("^(/)|(/[\\w'. _]{1,32})*$");

    public static final String DIR_ROOT = "/";
    public static final String DIR_APPLICATION_DATA = DIR_ROOT + "Programfiles";
    public static final String DIR_APPLICATION_DATA_32 = DIR_ROOT + "Programfiles (x86)";
    public static final String DIR_HOME = DIR_ROOT + "NeonOS";
    public static final String DIR_USERS = DIR_ROOT + "Users";
    public static final String LAPTOP_DRIVE_NAME = "NeonOS (C:)";

    private AbstractDrive mainDrive = null;
    private Map<UUID, AbstractDrive> additionalDrives = new HashMap<>();
    private AbstractDrive attachedDrive = null;

    private TileEntityBaseDevice tileEntity;

    private DyeColor attachedDriveColor = DyeColor.WHITE;

    public FileSystem(TileEntityBaseDevice tileEntity, CompoundNBT fileSystemTag) {
        this.tileEntity = tileEntity;

        load(fileSystemTag);
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendAction(Drive drive, FileAction action, @Nullable Callback<Response> callback) {
        if (BaseDevice.getPos() != null) {
            Task task = new TaskSendAction(drive, action);
            task.setCallback((nbt, success) ->
            {
                if (callback != null) {
                    callback.execute(Response.fromTag(nbt.getCompound("response")), success);
                }
            });
            TaskManager.sendTask(task);
        }
    }

    public static void getApplicationFolder(Application app, Callback<Folder> callback) {
        if (!ApplicationManager.getSystemApplications().contains(app.getInfo())) {
            callback.execute(null, false);
            return;
        }
        if (BaseDevice.getMainDrive() == null) {
            Task task = new TaskGetMainDrive(BaseDevice.getPos());
            task.setCallback((nbt, success) ->
            {
                if (success) {
                    setupApplicationFolder(app, callback);
                } else {
                    callback.execute(null, false);
                }
            });
            TaskManager.sendTask(task);
        } else {
            setupApplicationFolder(app, callback);
        }
    }

    private static void setupApplicationFolder(Application app, Callback<Folder> callback) {
        Folder folder = BaseDevice.getMainDrive().getFolder(FileSystem.DIR_APPLICATION_DATA);
        if (folder != null) {
            if (folder.hasFolder(app.getInfo().getFormattedId())) {
                Folder appFolder = folder.getFolder(app.getInfo().getFormattedId());
                if (appFolder.isSynced()) {
                    callback.execute(appFolder, true);
                } else {
                    Task task = new TaskGetFiles(appFolder, BaseDevice.getPos());
                    task.setCallback((nbt, success) ->
                    {
                        if (success && nbt.contains("files", Constants.NBT.TAG_LIST)) {
                            ListNBT files = nbt.getList("files", Constants.NBT.TAG_COMPOUND);
                            appFolder.syncFiles(files);
                            callback.execute(appFolder, true);
                        } else {
                            callback.execute(null, false);
                        }
                    });
                    TaskManager.sendTask(task);
                }
            } else {
                Folder appFolder = new Folder(app.getInfo().getFormattedId());
                folder.add(appFolder, (response, success) ->
                {
                    if (response != null && response.getStatus() == Status.SUCCESSFUL) {
                        callback.execute(appFolder, true);
                    } else {
                        callback.execute(null, false);
                    }
                });
            }
        } else {
            callback.execute(null, false);
        }
    }

    public static Response createSuccessResponse() {
        return new Response(Status.SUCCESSFUL);
    }

    public static Response createResponse(int status, String message) {
        return new Response(status, message);
    }

    private void load(CompoundNBT fileSystemTag) {
        if (fileSystemTag.contains("main_drive", Constants.NBT.TAG_COMPOUND)) {
            mainDrive = InternalDrive.fromTag(fileSystemTag.getCompound("main_drive"));
        }

        if (fileSystemTag.contains("drives", Constants.NBT.TAG_LIST)) {
            ListNBT tagList = fileSystemTag.getList("drives", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundNBT driveTag = tagList.getCompound(i);
                AbstractDrive drive = InternalDrive.fromTag(driveTag.getCompound("drive"));
                additionalDrives.put(drive.getUUID(), drive);
            }
        }

        if (fileSystemTag.contains("external_drive", Constants.NBT.TAG_COMPOUND)) {
            attachedDrive = ExternalDrive.fromTag(fileSystemTag.getCompound("external_drive"));
        }

        if (fileSystemTag.contains("external_drive_color", Constants.NBT.TAG_BYTE)) {
            attachedDriveColor = DyeColor.byId(fileSystemTag.getByte("external_drive_color"));
        }

        setupDefault();
    }

    /**
     * Sets up the default folders for the file system if they don't exist.
     */
    private void setupDefault() {
        if (mainDrive == null) {
            AbstractDrive drive = new InternalDrive(LAPTOP_DRIVE_NAME);
            ServerFolder root = drive.getRoot(tileEntity.getWorld());
            root.add(createProtectedFolder(DIR_USERS), false);
            root.add(createProtectedFolder("Programfiles"), false);
            root.add(createProtectedFolder("Programfiles (x86)"), false);
            root.add(createProtectedFolder("NeonOS"), false);
            mainDrive = drive;
            tileEntity.markDirty();
        }
    }

    private ServerFolder createProtectedFolder(String name) {
        try {
            Constructor<ServerFolder> constructor = ServerFolder.class.getDeclaredConstructor(String.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(name, true);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response readAction(String driveUuid, FileAction action, World world) {
        UUID uuid = UUID.fromString(driveUuid);
        AbstractDrive drive = getAvailableDrives(world, true).get(uuid);
        if (drive != null) {
            Response response = drive.handleFileAction(this, action, world);
            if (response.getStatus() == Status.SUCCESSFUL) {
                tileEntity.markDirty();
            }
            return response;
        }
        return createResponse(Status.DRIVE_UNAVAILABLE, "Drive unavailable or missing");
    }

    public AbstractDrive getMainDrive() {
        return mainDrive;
    }

    public Map<UUID, AbstractDrive> getAvailableDrives(World world, boolean includeMain) {
        Map<UUID, AbstractDrive> drives = new LinkedHashMap<>();

        if (includeMain)
            drives.put(mainDrive.getUUID(), mainDrive);

        additionalDrives.forEach(drives::put);

        if (attachedDrive != null)
            drives.put(attachedDrive.getUUID(), attachedDrive);

        //TODO add network drives
        return drives;
    }

    public boolean setAttachedDrive(ItemStack flashDrive) {
        if (attachedDrive == null) {
            CompoundNBT flashDriveTag = getExternalDriveTag(flashDrive);
            AbstractDrive drive = ExternalDrive.fromTag(flashDriveTag.getCompound("drive"));
            if (drive != null) {
                drive.setName(flashDrive.getDisplayName().getFormattedText());
                attachedDrive = drive;
                attachedDriveColor = DyeColor.byId(flashDrive.getTag().getInt("dyeColor"));

                tileEntity.getPipeline().putByte("external_drive_color", (byte) attachedDriveColor.getId());
                tileEntity.sync();

                return true;
            }
        }
        return false;
    }

    public AbstractDrive getAttachedDrive() {
        return attachedDrive;
    }

    public DyeColor getAttachedDriveColor() {
        return attachedDriveColor;
    }

    @Nullable
    public ItemStack removeAttachedDrive() {
        if (attachedDrive != null) {
            ItemStack stack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(HuskysGadgetMod.MOD_ID, "flash_drive_" +
                    getAttachedDriveColor().getName()))), 1);
            stack.setDisplayName(new StringTextComponent(attachedDrive.getName()));
            stack.getTag().put("drive", attachedDrive.toTag());
            attachedDrive = null;
            return stack;
        }
        return null;
    }

    private CompoundNBT getExternalDriveTag(ItemStack stack) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound == null) {
            tagCompound = new CompoundNBT();
            tagCompound.put("drive", new ExternalDrive(stack.getDisplayName().getFormattedText()).toTag());
            stack.setTag(tagCompound);
        } else if (!tagCompound.contains("drive", Constants.NBT.TAG_COMPOUND)) {
            tagCompound.put("drive", new ExternalDrive(stack.getDisplayName().getFormattedText()).toTag());
        }
        return tagCompound;
    }

    public CompoundNBT toTag() {
        CompoundNBT fileSystemTag = new CompoundNBT();

        if (mainDrive != null)
            fileSystemTag.put("main_drive", mainDrive.toTag());

        ListNBT tagList = new ListNBT();
        additionalDrives.forEach((k, v) -> tagList.add(v.toTag()));
        fileSystemTag.put("drives", tagList);

        if (attachedDrive != null) {
            fileSystemTag.put("external_drive", attachedDrive.toTag());
            fileSystemTag.putByte("external_drive_color", (byte) attachedDriveColor.getId());
        }

        return fileSystemTag;
    }

    public static class Response {
        private final int status;
        private String message = "";

        private Response(int status) {
            this.status = status;
        }

        private Response(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public static Response fromTag(CompoundNBT responseTag) {
            return new Response(responseTag.getInt("status"), responseTag.getString("message"));
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public CompoundNBT toTag() {
            CompoundNBT responseTag = new CompoundNBT();
            responseTag.putInt("status", status);
            responseTag.putString("message", message);
            return responseTag;
        }
    }

    public static class Status {
        public static final int FAILED = 0;
        public static final int SUCCESSFUL = 1;
        public static final int FILE_INVALID = 2;
        public static final int FILE_IS_PROTECTED = 3;
        public static final int FILE_EXISTS = 4;
        public static final int FILE_INVALID_NAME = 5;
        public static final int FILE_INVALID_DATA = 6;
        public static final int DRIVE_UNAVAILABLE = 7;
    }
}
