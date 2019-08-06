package io.github.vampirestudios.hgm.core.tasks;

import io.github.vampirestudios.hgm.api.AppInfo;
import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.block.entity.TileEntityBaseDevice;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Author: MrCrayfish
 */
public class TaskInstallApp extends Task {
    private String appId;
    private BlockPos laptopPos;
    private boolean install;

    private TaskInstallApp() {
        super("install_app");
    }

    public TaskInstallApp(AppInfo info, BlockPos laptopPos, boolean install) {
        this();
        this.appId = info.getFormattedId();
        this.laptopPos = laptopPos;
        this.install = install;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt) {
        nbt.putString("appId", appId);
        nbt.putLong("pos", laptopPos.toLong());
        nbt.putBoolean("install", install);
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, PlayerEntity player) {
        String appId = nbt.getString("appId");
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("pos")));
        if (tileEntity instanceof TileEntityBaseDevice) {
            TileEntityBaseDevice laptop = (TileEntityBaseDevice) tileEntity;
            CompoundNBT systemData = laptop.getSystemData();
            ListNBT tagList = systemData.getList("InstalledApps", Constants.NBT.TAG_STRING);

            System.out.println("Before the task: ");
            for (int i = 0; i < tagList.size(); i++) {
                System.out.println("\t- " + tagList.getString(i));
            }

            if (nbt.getBoolean("install")) {
                for (int i = 0; i < tagList.size(); i++) {
                    if (tagList.getString(i).equals(appId)) {
                        return;
                    }
                }
                tagList.add(new StringNBT(appId));
                this.setSuccessful();
            } else {
                for (int i = 0; i < tagList.size(); i++) {
                    if (tagList.getString(i).equals(appId)) {
                        tagList.remove(i);
                        this.setSuccessful();
                    }
                }
            }
            systemData.put("InstalledApps", tagList);

            System.out.println("After the task: ");
            for (int i = 0; i < tagList.size(); i++) {
                System.out.println("\t- " + tagList.getString(i));
            }
        }
    }

    @Override
    public void prepareResponse(CompoundNBT nbt) {

    }

    @Override
    public void processResponse(CompoundNBT nbt) {

    }
}