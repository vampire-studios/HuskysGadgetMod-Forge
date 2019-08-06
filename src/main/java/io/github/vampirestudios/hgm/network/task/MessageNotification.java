package io.github.vampirestudios.hgm.network.task;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.app.Notification;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageNotification {
    private CompoundNBT notificationTag;

    public MessageNotification() {
    }

    public MessageNotification(Notification notification) {
        this.notificationTag = notification.toTag();
    }

    public MessageNotification(CompoundNBT notification) {
        this.notificationTag = notification;
    }

    public static MessageNotification decode(PacketBuffer buf) {
        return new MessageNotification(buf.readCompoundTag());
    }

    public void encode(PacketBuffer buf) {
        buf.writeCompoundTag(notificationTag);
    }

    public void received(Supplier<NetworkEvent.Context> contextSupplier) {
        HuskysGadgetMod.setup.showNotification(notificationTag);
    }
}