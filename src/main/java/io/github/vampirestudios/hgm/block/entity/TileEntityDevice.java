package io.github.vampirestudios.hgm.block.entity;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public abstract class TileEntityDevice extends TileEntitySync implements ITickable {

    private UUID deviceId;
    private String name;

    public TileEntityDevice(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public final UUID getId() {
        if (deviceId == null) {
            deviceId = UUID.randomUUID();
        }
        return deviceId;
    }

    public abstract String getDeviceName();

    public String getCustomName() {
        return hasCustomName() ? name : getDeviceName();
    }

    public void setCustomName(String name) {
        this.name = name;
    }

    public boolean hasCustomName() {
        return !StringUtils.isEmpty(name);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putString("deviceId", getId().toString());
        if (hasCustomName()) {
            compound.putString("name", name);
        }
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("deviceId", Constants.NBT.TAG_STRING)) {
            deviceId = UUID.fromString(compound.getString("deviceId"));
        }
        if (compound.contains("name", Constants.NBT.TAG_STRING)) {
            name = compound.getString("name");
        }
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        if (hasCustomName()) {
            tag.putString("name", name);
        }
        return tag;
    }

}