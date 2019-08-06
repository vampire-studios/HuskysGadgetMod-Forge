package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.utils.VanillaPacketDispatcher;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileMod extends TileEntity {

    public TileMod(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

	/*@Override
	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}*/

    @Override
    public CompoundNBT write(CompoundNBT par1nbtTagCompound) {
        CompoundNBT nbt = super.write(par1nbtTagCompound);

        writeSharedNBT(par1nbtTagCompound);
        return nbt;
    }

    @Override
    public void read(CompoundNBT par1nbtTagCompound) {
        super.read(par1nbtTagCompound);

        readSharedNBT(par1nbtTagCompound);
    }

    public void writeSharedNBT(CompoundNBT cmp) {
        // NO-OP
    }

    public void readSharedNBT(CompoundNBT cmp) {
        // NO-OP
    }

    public void sync() {
        VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT cmp = new CompoundNBT();
        writeSharedNBT(cmp);
        return new SUpdateTileEntityPacket(getPos(), 0, cmp);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        readSharedNBT(packet.getNbtCompound());
    }

}
