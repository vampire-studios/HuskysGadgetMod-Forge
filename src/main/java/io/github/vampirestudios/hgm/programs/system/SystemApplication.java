package io.github.vampirestudios.hgm.programs.system;

import io.github.vampirestudios.hgm.api.app.Application;
import io.github.vampirestudios.hgm.core.BaseDevice;

import javax.annotation.Nullable;

/**
 * Created by Casey on 03-Aug-17.
 */
public abstract class SystemApplication extends Application {

    private BaseDevice laptop;

    protected SystemApplication() {

    }

    @Nullable
    public BaseDevice getLaptop() {
        return laptop;
    }

    public void setLaptop(@Nullable BaseDevice laptop) {
        this.laptop = laptop;
    }

}
