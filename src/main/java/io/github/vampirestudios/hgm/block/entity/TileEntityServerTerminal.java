package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.utils.IColored;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

public class TileEntityServerTerminal extends TileEntitySync implements IColored {

    private DyeColor color = DyeColor.WHITE;

    private byte rotation;

    public TileEntityServerTerminal(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void nextRotation() {
        rotation++;
        if (rotation > 7) {
            rotation = 0;
        }
        pipeline.putByte("rotation", rotation);
        sync();
    }

    public float getRotation() {
        return rotation * 45F;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("rotation", Constants.NBT.TAG_BYTE)) {
            rotation = compound.getByte("rotation");
        }
        if (compound.contains("color", Constants.NBT.TAG_BYTE)) {
            this.color = DyeColor.byId(compound.getByte("color"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putByte("rotation", rotation);
        compound.putByte("color", (byte) color.getId());
        return compound;
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putByte("color", (byte) color.getId());
        return tag;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColor color) {
        this.color = color;
    }

}