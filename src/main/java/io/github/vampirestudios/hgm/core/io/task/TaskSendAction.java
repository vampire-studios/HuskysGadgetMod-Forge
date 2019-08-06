package io.github.vampirestudios.hgm.core.io.task;

import io.github.vampirestudios.hgm.api.io.Drive;
import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.block.entity.TileEntityLaptop;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.core.io.FileSystem;
import io.github.vampirestudios.hgm.core.io.action.FileAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaskSendAction extends Task {
    private String uuid;
    private FileAction action;
    private BlockPos pos;

    private FileSystem.Response response;

    private TaskSendAction() {
        super("send_action");
    }

    public TaskSendAction(Drive drive, FileAction action) {
        this();
        this.uuid = drive.getUUID().toString();
        this.action = action;
        this.pos = BaseDevice.getPos();
    }

    @Override
    public void prepareRequest(CompoundNBT nbt) {
        nbt.putString("uuid", uuid);
        nbt.put("action", action.toTag());
        nbt.putLong("pos", pos.toLong());
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, PlayerEntity player) {
        FileAction action = FileAction.fromTag(nbt.getCompound("action"));
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("pos")));
        if (tileEntity instanceof TileEntityLaptop) {
            TileEntityLaptop laptop = (TileEntityLaptop) tileEntity;
            response = laptop.getFileSystem().readAction(nbt.getString("uuid"), action, world);
            this.setSuccessful();
        }
    }

    @Override
    public void prepareResponse(CompoundNBT nbt) {
        nbt.put("response", response.toTag());
    }

    @Override
    public void processResponse(CompoundNBT nbt) {

    }
}
