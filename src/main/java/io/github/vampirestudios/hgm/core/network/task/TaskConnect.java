package io.github.vampirestudios.hgm.core.network.task;

import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.block.entity.TileEntityNetworkDevice;
import io.github.vampirestudios.hgm.block.entity.TileEntityRouter;
import io.github.vampirestudios.hgm.core.network.Router;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaskConnect extends Task {
    private BlockPos devicePos;
    private BlockPos routerPos;

    public TaskConnect() {
        super("connect");
    }

    public TaskConnect(BlockPos devicePos, BlockPos routerPos) {
        this();
        this.devicePos = devicePos;
        this.routerPos = routerPos;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt) {
        nbt.putLong("devicePos", devicePos.toLong());
        nbt.putLong("routerPos", routerPos.toLong());
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("routerPos")));
        if (tileEntity instanceof TileEntityRouter) {
            TileEntityRouter tileEntityRouter = (TileEntityRouter) tileEntity;
            Router router = tileEntityRouter.getRouter();

            TileEntity tileEntity1 = world.getTileEntity(BlockPos.fromLong(nbt.getLong("devicePos")));
            if (tileEntity1 instanceof TileEntityNetworkDevice) {
                TileEntityNetworkDevice TileEntityNetworkDevice = (TileEntityNetworkDevice) tileEntity1;
                if (router.addDevice(TileEntityNetworkDevice)) {
                    TileEntityNetworkDevice.connect(router);
                    this.setSuccessful();
                }
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