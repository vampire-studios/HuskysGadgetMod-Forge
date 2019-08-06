package io.github.vampirestudios.hgm.network.task;

import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.api.task.TaskManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageResponse {
    private static int id;
    private static Task request;
    private static CompoundNBT nbt;

    public MessageResponse() {
    }

    public MessageResponse(int idIn, Task requestIn) {
        id = idIn;
        request = requestIn;
    }

    public MessageResponse(int idIn, Task requestIn, CompoundNBT compoundNBT) {
        id = idIn;
        request = requestIn;
        nbt = compoundNBT;
    }

    public static MessageResponse decode(PacketBuffer buf) {
        boolean successful = buf.readBoolean();
        if (successful) request.setSuccessful();
        return new MessageResponse(buf.readInt(), TaskManager.getTaskAndRemove(id), buf.readCompoundTag());
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(id);
        buf.writeBoolean(request.isSucessful());
        CompoundNBT nbt = new CompoundNBT();
        request.prepareResponse(nbt);
        buf.writeCompoundTag(nbt);
        request.complete();
    }

    public void received(Supplier<NetworkEvent.Context> contextSupplier) {
        request.processResponse(nbt);
        request.callback(nbt);
    }

}
