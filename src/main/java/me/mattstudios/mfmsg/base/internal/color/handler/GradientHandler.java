package me.mattstudios.mfmsg.base.internal.color.handler;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;

/**
 * Handler for the gradient colors
 */
public final class GradientHandler {

    @NotNull
    private final List<Color> colors;

    private final int stepSize;
    private int step = 0;
    private int stepIndex = 0;

    /**
     * Main constructor for the gradient handler
     *
     * @param colors      The list of colors for the given gradient
     * @param totalColors The total amount of characters to paint
     */
    public GradientHandler(@NotNull final List<Color> colors, final int totalColors) {
        this.colors = colors;
        stepSize = totalColors / (colors.size() - 1);
    }

    /**
     * Gets the next color for the gradient
     *
     * @return The next color
     */
    @NotNull
    public String next() {
        final Color color;
        if (stepIndex + 1 < colors.size()) {
            final Color start = colors.get(stepIndex);
            final Color end = colors.get(stepIndex + 1);
            final float interval = (float) step / stepSize;

            color = getGradientInterval(start, end, interval);
        } else {
            color = colors.get(colors.size() - 1);
        }

        step += 1;
        if (step >= stepSize) {
            step = 0;
            stepIndex++;
        }

        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    /**
     * Gets the color in the interval
     *
     * @param start    The start color
     * @param end      The end color
     * @param interval The interval value
     * @return The correct color for the interval point
     */
    @NotNull
    private static Color getGradientInterval(@NotNull final Color start, @NotNull final Color end, final float interval) {
        int r = (int) (end.getRed() * interval + start.getRed() * (1 - interval));
        int g = (int) (end.getGreen() * interval + start.getGreen() * (1 - interval));
        int b = (int) (end.getBlue() * interval + start.getBlue() * (1 - interval));

        return new Color(r, g, b);
    }

}
