package io.github.vampirestudios.hgm.block.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityServerRack extends TileMod implements ITickableTileEntity {

    @OnlyIn(Dist.CLIENT)
    public float rotation;
    private boolean hasServers = false, hasConnectedPower = false;

    public TileEntityServerRack(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
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
        if (compound.contains("hasServers")) {
            this.hasServers = compound.getBoolean("hasServers");
        }
        if (compound.contains("hasConnectedPower")) {
            this.hasConnectedPower = compound.getBoolean("hasConnectedPower");
        }
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        compound.putBoolean("hasServers", hasServers);
        compound.putBoolean("hasConnectedPower", hasConnectedPower);
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

    public boolean hasConnectedPower() {
        return hasConnectedPower;
    }

    public void connectUnconnectPower() {
        hasConnectedPower = !hasConnectedPower;
    }

    public boolean hasServers() {
        return hasServers;
    }
}