package io.github.vampirestudios.hgm.network.task;

import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.api.task.TaskManager;
import io.github.vampirestudios.hgm.network.PacketHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageRequest {

    private static int id;
    private static Task task;
    private static CompoundNBT nbt;

    public MessageRequest(int idIn, Task taskIn) {
        id = idIn;
        task = taskIn;
    }

    public MessageRequest(int idIn, Task taskIn, CompoundNBT nbtIn) {
        id = idIn;
        task = taskIn;
        nbt = nbtIn;
    }

    public static MessageRequest decode(PacketBuffer buf) {
        String name = buf.readString();
        return new MessageRequest(buf.readInt(), TaskManager.getTask(name), buf.readCompoundTag());
    }

    public int getId() {
        return id;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(id);
        packetBuffer.writeString(task.getName());
        CompoundNBT nbt = new CompoundNBT();
        task.prepareRequest(nbt);
        packetBuffer.writeCompoundTag(nbt);
    }

    public void received(Supplier<NetworkEvent.Context> contextSupplier) {
        task.processRequest(nbt, Objects.requireNonNull(contextSupplier.get().getSender()).world, contextSupplier.get().getSender());
        PacketHandler.INSTANCE.sendToServer(new MessageResponse(id, task));
    }

}
