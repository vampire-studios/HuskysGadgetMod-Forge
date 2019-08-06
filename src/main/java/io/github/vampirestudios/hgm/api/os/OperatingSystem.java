package io.github.vampirestudios.hgm.api.os;

import io.github.vampirestudios.hgm.core.TaskBar;

public interface OperatingSystem {

    String name();

    String version();

    TaskBar taskBar();

    int ram();

    int storage();

}
