package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.core.io.FileSystem;
import io.github.vampirestudios.hgm.utils.TileEntityUtil;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class TileEntityBaseDevice extends TileEntityNetworkDevice.Colored {

    @OnlyIn(Dist.CLIENT)
    public float rotation;
    @OnlyIn(Dist.CLIENT)
    public float prevRotation;
    private String deviceName;
    private boolean powered = false, powerConnected = false, wifiConnected = false;
    private CompoundNBT applicationData;
    private CompoundNBT systemData;
    private FileSystem fileSystem;
    @OnlyIn(Dist.CLIENT)
    private boolean hasExternalDrive;

    @OnlyIn(Dist.CLIENT)
    private DyeColor externalDriveColor;

    public TileEntityBaseDevice(String deviceName, boolean isLaptop, TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.deviceName = deviceName;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            prevRotation = rotation;
            if (rotation > 0) {
                rotation -= 10F;
            } else {
                if (rotation < 110) {
                    rotation += 10F;
                }
            }
        }
        if (this.systemData != null && this.systemData.contains("boottimer") && this.systemData.contains("bootmode")) {
            BaseDevice.BootMode bootmode = BaseDevice.BootMode.getBootMode(this.systemData.getInt("bootmode"));
            if (bootmode != null && bootmode != BaseDevice.BootMode.NOTHING) {
                int boottimer = Math.max(this.systemData.getInt("boottimer") - 1, 0);
                if (boottimer == 0) {
                    bootmode = bootmode == BaseDevice.BootMode.BOOTING ? BaseDevice.BootMode.NOTHING : null;
                    this.systemData.putInt("bootmode", BaseDevice.BootMode.ordinal(bootmode));
                }
                this.systemData.putInt("boottimer", boottimer);
            }
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("device_name", Constants.NBT.TAG_STRING)) {
            this.deviceName = compound.getString("device_name");
        }
        if (compound.contains("system_data", Constants.NBT.TAG_COMPOUND)) {
            this.systemData = compound.getCompound("system_data");
        }
        if (compound.contains("application_data", Constants.NBT.TAG_COMPOUND)) {
            this.applicationData = compound.getCompound("application_data");
        }
        if (compound.contains("has_external_drive")) {
            this.hasExternalDrive = compound.getBoolean("has_external_drive");
        }
        if (compound.contains("powered")) {
            this.powered = compound.getBoolean("powered");
        }
        if (compound.contains("powerConnected")) {
            this.powerConnected = compound.getBoolean("powerConnected");
        }
        if (compound.contains("wifiConnected")) {
            this.wifiConnected = compound.getBoolean("wifiConnected");
        }
        if (compound.contains("file_system")) {
            this.fileSystem = new FileSystem(this, compound.getCompound("file_system"));
        }
        if (compound.contains("external_drive_color", Constants.NBT.TAG_BYTE)) {
            this.externalDriveColor = null;
            if (compound.getByte("external_drive_color") != -1) {
                this.externalDriveColor = DyeColor.byId(compound.getByte("external_drive_color"));
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putString("device_name", deviceName);

        if (systemData != null) {
            compound.put("system_data", systemData);
        }

        if (applicationData != null) {
            compound.put("application_data", applicationData);
        }

        if (fileSystem != null) {
            compound.put("file_system", fileSystem.toTag());
        }
        compound.putBoolean("powered", powered);
        compound.putBoolean("wifiConnected", wifiConnected);
        compound.putBoolean("powerConnected", powerConnected);
        return compound;
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("device_name", deviceName);
        tag.putBoolean("powered", powered);
        tag.putBoolean("wifiConnected", wifiConnected);
        tag.putBoolean("powerConnected", powerConnected);
        tag.put("system_data", getSystemData());
        tag.put("application_data", getApplicationData());

        if (getFileSystem().getAttachedDrive() != null) {
            tag.putByte("external_drive_color", (byte) getFileSystem().getAttachedDriveColor().getId());
        } else {
            tag.putByte("external_drive_color", (byte) -1);
        }
        return tag;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 16384;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void powerUnpower() {
        powered = !powered;
        pipeline.putBoolean("powered", powered);
        sync();
    }

    public boolean isPowered() {
        return powered;
    }

    public boolean isPowerConnected() {
        return powerConnected;
    }

    public boolean isWifiConnected() {
        return wifiConnected;
    }

    public void wifiOnlineOffline() {
        wifiConnected = !wifiConnected;
        pipeline.putBoolean("wifiConnected", wifiConnected);
        sync();
    }

    public void powerOnOff() {
        powerConnected = !powerConnected;
        pipeline.putBoolean("powerConnected", powerConnected);
        sync();
    }

    public CompoundNBT getApplicationData() {
        return applicationData != null ? applicationData : new CompoundNBT();
    }

    public CompoundNBT getSystemData() {
        return systemData != null ? systemData : new CompoundNBT();
    }

    public void setSystemData(CompoundNBT systemData) {
        this.systemData = systemData;
        markDirty();
        TileEntityUtil.markBlockForUpdate(world, pos);
    }

    public DyeColor getExternalDriveColor() {
        return externalDriveColor;
    }

    public FileSystem getFileSystem() {
        return fileSystem != null ? fileSystem : new FileSystem(this, new CompoundNBT());
    }

    public void setApplicationData(String appId, CompoundNBT applicationData) {
        this.applicationData.put(appId, applicationData);
        markDirty();
        TileEntityUtil.markBlockForUpdate(world, pos);
    }

    public boolean isExternalDriveAttached() {
        return hasExternalDrive;
    }

}
