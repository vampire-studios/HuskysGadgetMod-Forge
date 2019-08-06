package io.github.vampirestudios.hgm.core.print.task;

import io.github.vampirestudios.hgm.api.print.IPrint;
import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.block.entity.TileEntityNetworkDevice;
import io.github.vampirestudios.hgm.block.entity.TileEntityPrinter;
import io.github.vampirestudios.hgm.core.network.NetworkDevice;
import io.github.vampirestudios.hgm.core.network.Router;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TaskPrint extends Task {
    private BlockPos devicePos;
    private UUID printerId;
    private IPrint print;

    public TaskPrint() {
        super("print");
    }

    public TaskPrint(BlockPos devicePos, NetworkDevice printer, IPrint print) {
        this();
        this.devicePos = devicePos;
        this.printerId = printer.getId();
        this.print = print;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt) {
        nbt.putLong("devicePos", devicePos.toLong());
        nbt.putUniqueId("printerId", printerId);
        nbt.put("print", IPrint.writeToTag(print));
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("devicePos")));
        if (tileEntity instanceof TileEntityNetworkDevice) {
            TileEntityNetworkDevice device = (TileEntityNetworkDevice) tileEntity;
            Router router = device.getRouter();
            if (router != null) {
                TileEntityNetworkDevice printer = router.getDevice(world, nbt.getUniqueId("printerId"));
                if (printer != null && printer instanceof TileEntityPrinter) {
                    IPrint print = IPrint.loadFromTag(nbt.getCompound("print"));
                    ((TileEntityPrinter) printer).addToQueue(print);
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