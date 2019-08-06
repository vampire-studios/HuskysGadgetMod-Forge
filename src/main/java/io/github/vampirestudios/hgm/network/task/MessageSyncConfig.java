package io.github.vampirestudios.hgm.network.task;

import io.github.vampirestudios.hgm.Config;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageSyncConfig {

    public static MessageSyncConfig decode(PacketBuffer buf) {
        CompoundNBT syncTag = buf.readCompoundTag();
        Config.readSyncTag(Objects.requireNonNull(syncTag));
        return new MessageSyncConfig();
    }

    public void encode(PacketBuffer buf) {
        buf.writeCompoundTag(Config.writeSyncTag());
    }

    public void received(Supplier<NetworkEvent.Context> contextSupplier) {
    }

}
