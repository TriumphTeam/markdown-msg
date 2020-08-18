package me.mattstudios.mfmsg.base.internal.color.handler;

import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.ServerVersion;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.awt.*;
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
        private int step, stepIndex;

        public ColorGradient(List<Color> colors, int totalColors) {
            this.colors = colors;
            this.stepSize = totalColors / (colors.size() - 1);
            this.step = this.stepIndex = 0;
        }

        /**
         * @return the next color in the gradient
         */
        public Color next() {
            // Gradients will use the first color of the entire spectrum won't be available to preserve prettiness
            if (ServerVersion.CURRENT_VERSION.isOlderThan(ServerVersion.V1_16_R1))
                return this.colors.get(0);

            Color color;
            if (this.stepIndex + 1 < this.colors.size()) {
                Color start = this.colors.get(this.stepIndex);
                Color end = this.colors.get(this.stepIndex + 1);
                float interval = (float) this.step / this.stepSize;

                color = getGradientInterval(start, end, interval);
            } else {
                color = this.colors.get(this.colors.size() - 1);
            }

            this.step += 1;
            if (this.step >= this.stepSize) {
                this.step = 0;
                this.stepIndex++;
            }

            return color;
        }

        /**
         * Gets a color along a linear gradient between two colors
         *
         * @param start    The start color
         * @param end      The end color
         * @param interval The interval to get, between 0 and 1 inclusively
         * @return A Color at the interval between the start and end colors
         */
        public static Color getGradientInterval(Color start, Color end, float interval) {
            if (0 > interval || interval > 1)
                throw new IllegalArgumentException("Interval must be between 0 and 1 inclusively.");

            int r = (int) (end.getRed() * interval + start.getRed() * (1 - interval));
            int g = (int) (end.getGreen() * interval + start.getGreen() * (1 - interval));
            int b = (int) (end.getBlue() * interval + start.getBlue() * (1 - interval));

            return new Color(r, g, b);
        }

    }
}
