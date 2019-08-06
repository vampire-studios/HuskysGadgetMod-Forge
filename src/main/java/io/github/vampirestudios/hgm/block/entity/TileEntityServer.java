package io.github.vampirestudios.hgm.block.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityServer extends TileEntityBaseDevice {

    @OnlyIn(Dist.CLIENT)
    public float rotation;
    private boolean
            inServerRack = false,
            connected = false;

    public TileEntityServer() {
        super("Server", false, null);
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            if (rotation > 0) {
                rotation -= 10F;
            } else if (rotation < 110) {
                rotation += 10F;
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (compound.contains("connected")) {
            this.connected = compound.getBoolean("connected");
        }
        if (compound.contains("inServerRack")) {
            this.inServerRack = compound.getBoolean("inServerRack");
        }
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        compound.putBoolean("connected", connected);
        compound.putBoolean("inServerRack", inServerRack);
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("connected", connected);
        tag.putBoolean("inServerRack", inServerRack);
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

    public boolean isInServerRack() {
        return inServerRack;
    }

    public void connectedNotConnected() {
        connected = !connected;
        pipeline.putBoolean("connected", connected);
        sync();
    }

    public boolean isConnected() {
        return connected;
    }

}