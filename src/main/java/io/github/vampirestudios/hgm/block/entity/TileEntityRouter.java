package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.core.network.Router;
import io.github.vampirestudios.hgm.init.GadgetTileEntities;
import io.github.vampirestudios.hgm.utils.IColored;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class TileEntityRouter extends TileEntitySync implements ITickableTileEntity, IColored {

    private DyeColor color = DyeColor.WHITE;

    private Router router;

    @OnlyIn(Dist.CLIENT)
    private int debugTimer;

    public TileEntityRouter() {
        super(GadgetTileEntities.ROUTERS);
    }

    public Router getRouter() {
        if (router == null) {
            router = new Router(pos);
            markDirty();
        }
        return router;
    }

    public void tick() {
        if (!world.isRemote) {
            getRouter().update(world);
        } else if (debugTimer > 0) {
            debugTimer--;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isDebug() {
        return debugTimer > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void setDebug() {
        if (debugTimer <= 0) {
            debugTimer = 1200;
        } else {
            debugTimer = 0;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("router", getRouter().toTag(false));
        compound.putByte("color", (byte) color.getId());
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("router", Constants.NBT.TAG_COMPOUND)) {
            router = Router.fromTag(pos, compound.getCompound("router"));
        }
        if (compound.contains("color", Constants.NBT.TAG_BYTE)) {
            this.color = DyeColor.byId(compound.getByte("color"));
        }
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putByte("color", (byte) color.getId());
        return tag;
    }

    public void syncDevicesToClient() {
        pipeline.put("router", getRouter().toTag(true));
        sync();
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

    @Override
    public DyeColor getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColor color) {
        this.color = color;
    }

}