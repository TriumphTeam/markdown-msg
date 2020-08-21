package me.mattstudios.mfmsg.base.internal.color;

public final class FlatColor implements MessageColor {

    private final String color;

    public FlatColor(final String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
