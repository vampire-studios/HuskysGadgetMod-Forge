package io.github.vampirestudios.hgm.network.task;

import com.google.common.collect.ImmutableList;
import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.AppInfo;
import io.github.vampirestudios.hgm.api.ApplicationManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class MessageSyncApplications {
    private List<AppInfo> allowedApps;

    public MessageSyncApplications(List<AppInfo> allowedApps) {
        this.allowedApps = allowedApps;
    }

    public static MessageSyncApplications decode(PacketBuffer buf) {
        int size = buf.readInt();
        ImmutableList.Builder<AppInfo> builder = new ImmutableList.Builder<>();
        for (int i = 0; i < size; i++) {
            String appId = buf.readString();
            AppInfo info = ApplicationManager.getApplication(appId);
            if (info != null) {
                builder.add(info);
            } else {
                HuskysGadgetMod.LOGGER.error("Missing application '" + appId + "'");
            }
        }
        return new MessageSyncApplications(builder.build());
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(allowedApps.size());
        for (AppInfo appInfo : allowedApps) {
            buf.writeString(appInfo.getId().toString());
        }
    }

    public void received(Supplier<NetworkEvent.Context> contextSupplier) {
    }

}
