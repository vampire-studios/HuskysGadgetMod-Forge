package io.github.vampirestudios.hgm.network.task;

import io.github.vampirestudios.gadget.tileentity.TileEntityRouter;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncBlock {
    private static BlockPos routerPos;

    public MessageSyncBlock(BlockPos routerPosIn) {
        routerPos = routerPosIn;
    }

    public static MessageSyncBlock decode(PacketBuffer buf) {
        return new MessageSyncBlock(BlockPos.fromLong(buf.readLong()));
    }

    public void encode(PacketBuffer buf) {
        buf.writeLong(routerPos.toLong());
    }

    public void received(Supplier<NetworkEvent.Context> contextSupplier) {
        World world = contextSupplier.get().getSender().world;
        TileEntity tileEntity = world.getTileEntity(routerPos);
        if (tileEntity instanceof TileEntityRouter) {
            TileEntityRouter tileEntityRouter = (TileEntityRouter) tileEntity;
            tileEntityRouter.syncDevicesToClient();
        }
    }
}