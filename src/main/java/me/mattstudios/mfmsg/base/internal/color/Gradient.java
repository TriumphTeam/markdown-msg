package me.mattstudios.mfmsg.base.internal.color;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;

/**
 * Gradient message color
 */
public final class Gradient implements MessageColor {

    @NotNull
    private final List<Color> colors;

    /**
     * Main constructor that takes the colors lis
     *
     * @param colors The list of colors to store
     */
    public Gradient(@NotNull final List<Color> colors) {
        this.colors = colors;
    }

    /**
     * Gets the colors to use in the gradient
     *
     * @return The gradient colors
     */
    @NotNull
    public List<Color> getColors() {
        return colors;
    }

}
