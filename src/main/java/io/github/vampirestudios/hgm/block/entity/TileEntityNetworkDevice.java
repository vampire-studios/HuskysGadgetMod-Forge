package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.DeviceConfig;
import io.github.vampirestudios.hgm.core.network.Connection;
import io.github.vampirestudios.hgm.core.network.Router;
import io.github.vampirestudios.hgm.utils.IColored;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class TileEntityNetworkDevice extends TileEntityDevice implements ITickableTileEntity {
    private int counter;
    private Connection connection;

    public TileEntityNetworkDevice(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (world.isRemote)
            return;

        if (connection != null) {
            if (++counter >= DeviceConfig.getBeaconInterval() * 2) {
                connection.setRouterPos(null);
                counter = 0;
            }
        }
    }

    public void connect(Router router) {
        if (router == null) {
            if (connection != null) {
                Router connectedRouter = connection.getRouter(world);
                if (connectedRouter != null) {
                    connectedRouter.removeDevice(this);
                }
            }
            connection = null;
            return;
        }
        connection = new Connection(router);
        counter = 0;
        this.markDirty();
    }

    public Connection getConnection() {
        return connection;
    }

    @Nullable
    public Router getRouter() {
        return connection != null ? connection.getRouter(world) : null;
    }

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public boolean receiveBeacon(Router router) {
        if (counter >= DeviceConfig.getBeaconInterval() * 2) {
            connect(router);
            return true;
        }
        if (connection != null && connection.getRouterId().equals(router.getId())) {
            connection.setRouterPos(router.getPos());
            counter = 0;
            return true;
        }
        return false;
    }

    public int getSignalStrength() {
        BlockPos routerPos = connection.getRouterPos();
        if (routerPos != null) {
            double distance = Math.sqrt(pos.distanceSq(new Vec3i(routerPos.getX() + 0.5, routerPos.getY() + 0.5, routerPos.getZ() + 0.5)));
            double level = DeviceConfig.getSignalRange() / 3.0;
            return distance > level * 2 ? 2 : distance > level ? 1 : 0;
        }
        return -1;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (connection != null) {
            compound.put("connection", connection.toTag());
        }
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("connection", Constants.NBT.TAG_COMPOUND)) {
            connection = Connection.fromTag(this, compound.getCompound("connection"));
        }
    }

    public static abstract class Colored extends TileEntityNetworkDevice implements IColored {
        private DyeColor color = DyeColor.WHITE;

        public Colored(TileEntityType<?> tileEntityTypeIn) {
            super(tileEntityTypeIn);
        }

        @Override
        public void read(CompoundNBT compound) {
            super.read(compound);
            if (compound.contains("color", Constants.NBT.TAG_BYTE)) {
                this.color = DyeColor.byId(compound.getByte("color"));
            }
        }

        @Override
        public CompoundNBT write(CompoundNBT compound) {
            super.write(compound);
            compound.putByte("color", (byte) color.getId());
            return compound;
        }

        @Override
        public CompoundNBT writeSyncTag() {
            CompoundNBT tag = super.writeSyncTag();
            tag.putByte("color", (byte) color.getId());
            return tag;
        }

        @Override
        public final DyeColor getColor() {
            return color;
        }

        @Override
        public final void setColor(DyeColor color) {
            this.color = color;
        }
    }
}