package me.mattstudios.mfmsg.base.internal.color.handler;

import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.awt.Color;
import java.util.List;

public final class GradientHandler {

    public static void appendGradient(final List<MessagePart> parts, final Gradient gradient, final ComponentBuilder builder) {
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final ColorGradient colorGradient = new ColorGradient(colors, length);

        for (final MessagePart part : parts) {
            for (char character : part.getText().toCharArray()) {
                builder.append(String.valueOf(character));

                builder.bold(part.isBold());
                builder.italic(part.isItalic());
                builder.strikethrough(part.isStrike());
                builder.underlined(part.isUnderline());
                builder.obfuscated(part.isObfuscated());

                builder.color(ChatColor.of(colorGradient.next()));
            }
        }
    }

    /**
     * Allows generation of a multi-part gradient with a fixed number of steps
     */
    private static class ColorGradient {

        private final List<Color> colors;

        private final int stepSize;
        private int step = 0;
        private int stepIndex = 0;

        private ColorGradient(final List<Color> colors, final int totalColors) {
            this.colors = colors;
            stepSize = totalColors / (colors.size() - 1);
        }

        private Color next() {
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

            return color;
        }

        private static Color getGradientInterval(final Color start, final Color end, final float interval) {
            int r = (int) (end.getRed() * interval + start.getRed() * (1 - interval));
            int g = (int) (end.getGreen() * interval + start.getGreen() * (1 - interval));
            int b = (int) (end.getBlue() * interval + start.getBlue() * (1 - interval));

            return new Color(r, g, b);
        }

    }
}
