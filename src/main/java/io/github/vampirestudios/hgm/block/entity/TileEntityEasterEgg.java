package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.init.GadgetTileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntityEasterEgg extends TileEntity {

    static Random rng = new Random();
    private int color0, color1;

    public TileEntityEasterEgg() {
        super(GadgetTileEntities.EASTER_EGG);
        this.color0 = rng.nextInt(0xFFFFFF);
        this.color1 = rng.nextInt(0xFFFFFF);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);
        this.writeColorsToNBT(compound);
        return compound;
    }

    public CompoundNBT writeColorsToNBT(CompoundNBT compound) {
        for (int i = 0; i < 2; i++) {
            compound.putInt("color" + i, this.getColor(i));
        }
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.readColorsFromNBT(compound);
    }

    private void readColorsFromNBT(CompoundNBT compound) {
        for (int i = 0; i < 2; i++) {
            if (compound.contains("color" + i)) {
                this.setColor(i, compound.getInt("color" + i));
            }
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.writeColorsToNBT(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readColorsFromNBT(pkt.getNbtCompound());
    }

    public int getColor(int index) {
        return index == 0 ? color0 : (index == 1 ? color1 : 0xFFFFFF);
    }

    private void setColor(int index, int color) {
        if (index == 0) {
            this.color0 = color;
        } else if (index == 1) {
            this.color1 = color;
        }
    }

}
