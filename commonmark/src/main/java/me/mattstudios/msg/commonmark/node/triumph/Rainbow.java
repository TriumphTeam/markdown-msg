package me.mattstudios.msg.commonmark.node.triumph;

import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

public class Rainbow extends Node {

    private float saturation;
    private float brightness;

    public Rainbow(final float saturation, final float brightness) {
        this.saturation = saturation;
        this.brightness = brightness;
    }

    @Override
    public void accept(@NotNull final Visitor visitor) {
        visitor.visit(this);
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(final float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(final float brightness) {
        this.brightness = brightness;
    }
}
