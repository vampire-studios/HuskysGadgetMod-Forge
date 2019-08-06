package io.github.vampirestudios.hgm.network.task;

import io.github.vampirestudios.hgm.init.GadgetBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUnlockAdvancement {

    public static MessageUnlockAdvancement decode(PacketBuffer buf) {
        return new MessageUnlockAdvancement();
    }

    public void encode(PacketBuffer buf) {
    }

    public void onMessage(Supplier<NetworkEvent.Context> contextSupplier) {
        PlayerEntity pl = contextSupplier.get().getSender();
        World w = pl.world;
        int rad = 10;
        int x = (int) pl.posX + w.rand.nextInt(rad * 2) - rad;
        int z = (int) pl.posZ + w.rand.nextInt(rad * 2) - rad;
        int y = w.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        w.setBlockState(pos, GadgetBlocks.EASTER_EGG.getDefaultState());
    }

}
