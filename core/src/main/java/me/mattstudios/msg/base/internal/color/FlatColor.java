package me.mattstudios.msg.base.internal.color;

import org.jetbrains.annotations.NotNull;

/**
 * Message color for simple flat color
 */
public final class FlatColor implements MessageColor {

    @NotNull
    private final String color;

    /**
     * Main constructor
     *
     * @param color The color to store
     */
    public FlatColor(@NotNull final String color) {
        this.color = color;
    }

    /**
     * Gets the stored color
     *
     * @return The color
     */
    @NotNull
    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color;
    }

}
