package me.mattstudios.mfmsg.base.internal.color;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public final class Gradient implements MessageColor {

    private final List<Color> colors;

    public Gradient(final List<Color> colors) {
        this.colors = colors;
    }

    public List<Color> getColors() {
        return colors;
    }

    @Override
    public String test() {
        return "{Gradient: " + colors.stream().map(Color::toString).collect(Collectors.joining(", ")) + "}";
    }
}
