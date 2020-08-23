package me.mattstudios.mfmsg.base.internal.color.handler;

import java.awt.Color;
import java.util.List;

public final class GradientHandler {

    private final List<Color> colors;

    private final int stepSize;
    private int step = 0;
    private int stepIndex = 0;

    public GradientHandler(final List<Color> colors, final int totalColors) {
        this.colors = colors;
        stepSize = totalColors / (colors.size() - 1);
    }

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

    private static Color getGradientInterval(final Color start, final Color end, final float interval) {
        int r = (int) (end.getRed() * interval + start.getRed() * (1 - interval));
        int g = (int) (end.getGreen() * interval + start.getGreen() * (1 - interval));
        int b = (int) (end.getBlue() * interval + start.getBlue() * (1 - interval));

        return new Color(r, g, b);
    }

}
