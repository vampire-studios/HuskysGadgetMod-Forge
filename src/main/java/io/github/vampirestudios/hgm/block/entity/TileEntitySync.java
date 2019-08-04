package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.utils.TileEntityUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileEntitySync extends TileMod {

    protected CompoundNBT pipeline = new CompoundNBT();

    public TileEntitySync(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void sync() {
        TileEntityUtil.markBlockForUpdate(world, pos);
        markDirty();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(pkt.getNbtCompound());
    }

    @Override
    public final CompoundNBT getUpdateTag() {
        if (!pipeline.isEmpty()) {
            CompoundNBT updateTag = super.write(pipeline);
            pipeline = new CompoundNBT();
            return updateTag;
        }
        return super.write(writeSyncTag());
    }

    public abstract CompoundNBT writeSyncTag();

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    public CompoundNBT getPipeline() {
        return pipeline;
    }

}