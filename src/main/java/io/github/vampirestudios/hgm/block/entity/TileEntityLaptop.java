package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.init.GadgetTileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityLaptop extends TileEntityBaseDevice {

    private static final int OPENED_ANGLE = 102;

    private boolean open = false, powered = false, hasBattery = false;

    @OnlyIn(Dist.CLIENT)
    private int rotation;

    @OnlyIn(Dist.CLIENT)
    private int prevRotation;

    public TileEntityLaptop() {
        super("Laptop", true, GadgetTileEntities.LAPTOPS);
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isRemote) {
            prevRotation = rotation;
            if (!open) {
                if (rotation > 0) {
                    rotation -= 10F;
                }
            } else {
                if (rotation < OPENED_ANGLE) {
                    rotation += 10F;
                }
            }
            //System.out.println(this);
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("open")) {
            this.open = compound.getBoolean("open");
        }
        if (compound.contains("powered")) {
            this.powered = compound.getBoolean("powered");
        }
        if (compound.contains("hasBattery")) {
            this.hasBattery = compound.getBoolean("hasBattery");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("open", open);
        compound.putBoolean("powered", powered);
        compound.putBoolean("hasBattery", hasBattery);
        return compound;
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = super.writeSyncTag();
        tag.putBoolean("open", open);
        tag.putBoolean("powered", powered);
        tag.putBoolean("hasBattery", hasBattery);
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

    public void openClose() {
        open = !open;
        pipeline.putBoolean("open", open);
        sync();
    }

    public void powerUnpower() {
        powered = !powered;
        pipeline.putBoolean("powered", powered);
        sync();
    }

    public void hasBatteryHasNotBattery() {
        hasBattery = !hasBattery;
        pipeline.putBoolean("hasBattery", hasBattery);
        sync();
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isPowered() {
        return powered;
    }

    public boolean hasBattery() {
        return hasBattery;
    }

    @OnlyIn(Dist.CLIENT)
    public float getScreenAngle(float partialTicks) {
        return -OPENED_ANGLE * ((prevRotation + (rotation - prevRotation) * partialTicks) / OPENED_ANGLE);
    }

}
