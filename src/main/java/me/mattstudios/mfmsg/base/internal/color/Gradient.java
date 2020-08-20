package me.mattstudios.mfmsg.base.internal.color;

import java.awt.Color;
import java.util.List;

public final class Gradient implements MessageColor {

    private final List<Color> colors;

    public Gradient(final List<Color> colors) {
        this.colors = colors;
    }

    public List<Color> getColors() {
        return colors;
    }

}
