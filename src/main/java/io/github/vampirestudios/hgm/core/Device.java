package io.github.vampirestudios.hgm.core;

import io.github.vampirestudios.hgm.block.entity.TileEntityDevice;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class Device {
    protected UUID id;
    protected String name;
    protected BlockPos pos;

    protected Device() {
    }

    public Device(TileEntityDevice device) {
        this.id = device.getId();
        update(device);
    }

    public Device(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Device fromTag(CompoundNBT tag) {
        Device device = new Device();
        device.id = UUID.fromString(tag.getString("id"));
        device.name = tag.getString("name");
        if (tag.contains("pos", Constants.NBT.TAG_LONG)) {
            device.pos = BlockPos.fromLong(tag.getLong("pos"));
        }
        return device;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void update(TileEntityDevice device) {
        name = device.getCustomName();
        pos = device.getPos();
    }

    @Nullable
    public TileEntityDevice getDevice(World world) {
        if (pos == null)
            return null;

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityDevice) {
            TileEntityDevice tileEntityDevice = (TileEntityDevice) tileEntity;
            if (tileEntityDevice.getId().equals(getId())) {
                return tileEntityDevice;
            }
        }
        return null;
    }

    public CompoundNBT toTag(boolean includePos) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("id", id.toString());
        tag.putString("name", name);
        if (includePos && pos != null) {
            tag.putLong("pos", pos.toLong());
        }
        return tag;
    }
}
