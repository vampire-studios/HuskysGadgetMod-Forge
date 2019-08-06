package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.api.print.IPrint;
import io.github.vampirestudios.hgm.init.GadgetTileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class TileEntityPaper extends TileEntitySync {

    private IPrint print;
    private byte rotation;

    public TileEntityPaper() {
        super(GadgetTileEntities.PAPER);
    }

    public void nextRotation() {
        rotation++;
        if (rotation > 7) {
            rotation = 0;
        }
        pipeline.putByte("rotation", rotation);
        sync();
        playSound(SoundEvents.ENTITY_ITEM_FRAME_ROTATE_ITEM);
    }

    public float getRotation() {
        return rotation * 45F;
    }

    @Nullable
    public IPrint getPrint() {
        return print;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("print", Constants.NBT.TAG_COMPOUND)) {
            print = IPrint.loadFromTag(compound.getCompound("print"));
        }
        if (compound.contains("rotation", Constants.NBT.TAG_BYTE)) {
            rotation = compound.getByte("rotation");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (print != null) {
            compound.put("print", IPrint.writeToTag(print));
        }
        compound.putByte("rotation", rotation);
        return compound;
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        if (print != null) {
            tag.put("print", IPrint.writeToTag(print));
        }
        tag.putByte("rotation", rotation);
        return tag;
    }

    private void playSound(SoundEvent sound) {
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}