package io.github.vampirestudios.hgm.core.io.drive;

import io.github.vampirestudios.hgm.core.io.ServerFolder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public final class InternalDrive extends AbstractDrive {
    public InternalDrive(String name) {
        super(name);
    }

    @Nullable
    public static AbstractDrive fromTag(CompoundNBT driveTag) {
        AbstractDrive drive = new InternalDrive(driveTag.getString("name"));
        if (driveTag.contains("root", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT folderTag = driveTag.getCompound("root");
            drive.root = ServerFolder.fromTag(folderTag.getString("file_name"), folderTag.getCompound("data"));
        }
        return drive;
    }

    @Override
    public CompoundNBT toTag() {
        CompoundNBT driveTag = new CompoundNBT();
        driveTag.putString("name", name);

        CompoundNBT folderTag = new CompoundNBT();
        folderTag.putString("file_name", root.getName());
        folderTag.put("data", root.toTag());
        driveTag.put("root", folderTag);

        return driveTag;
    }

    @Override
    public Type getType() {
        return Type.INTERNAL;
    }
}
