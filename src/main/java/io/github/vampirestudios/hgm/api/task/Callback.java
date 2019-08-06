package io.github.vampirestudios.hgm.api.task;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

/**
 * A simple interface to handle processing responses by {@link io.github.vampirestudios.hgm.api.task.Task}.
 * Callbacks are necessary for the updating of any UI component.
 */
public interface Callback<T> {

    /**
     * Executes the callback. You should perform any changes to
     * your UI in this method. The NBT tag contains the same data
     * as {@link io.github.vampirestudios.hgm.api.task.Task#processResponse(CompoundNBT)}'s
     * tag does.
     *
     * @param t       the response object
     * @param success if the {@link io.github.vampirestudios.hgm.api.task.Task} performed it's intended action correctly.
     */
    void execute(@Nullable T t, boolean success);
}
