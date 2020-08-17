package me.mattstudios.mfmsg.base.internal.color;

import java.util.List;

public final class Gradient implements MessageColor {

    private final List<String> colors;

    public Gradient(final List<String> colors) {
        this.colors = colors;
    }

    public List<String> getColors() {
        return colors;
    }

    @Override
    public String test() {
        return "{Gradient: " + String.join(", ", colors) + "}";
    }
}
