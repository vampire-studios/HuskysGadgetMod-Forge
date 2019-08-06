package io.github.vampirestudios.hgm.network;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.network.task.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HuskysGadgetMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(1, MessageRequest.class, MessageRequest::encode, MessageRequest::decode, MessageRequest::received);
        INSTANCE.registerMessage(2, MessageResponse.class, MessageResponse::encode, MessageResponse::decode, MessageResponse::received);
        INSTANCE.registerMessage(3, MessageSyncApplications.class, MessageSyncApplications::encode, MessageSyncApplications::decode, MessageSyncApplications::received);
        INSTANCE.registerMessage(4, MessageSyncConfig.class, MessageSyncConfig::encode, MessageSyncConfig::decode, MessageSyncConfig::received);
        INSTANCE.registerMessage(5, MessageSyncBlock.class, MessageSyncBlock::encode, MessageSyncBlock::decode, MessageSyncBlock::received);
        INSTANCE.registerMessage(6, MessageNotification.class, MessageNotification::encode, MessageNotification::decode, MessageNotification::received);
    }

}
