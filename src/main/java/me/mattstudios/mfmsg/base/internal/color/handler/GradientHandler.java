package me.mattstudios.mfmsg.base.internal.color.handler;

import com.google.gson.JsonArray;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.serializer.JsonSerializer;
import me.mattstudios.mfmsg.base.serializer.StringSerializer;

import java.awt.Color;
import java.util.List;

public final class GradientHandler {

    public static JsonArray toGradientJson(final List<MessagePart> parts, final Gradient gradient) {
        final JsonArray jsonArray = new JsonArray();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final ColorGradient colorGradient = new ColorGradient(colors, length);

        for (final MessagePart part : parts) {
            for (char character : part.getText().toCharArray()) {
                jsonArray.add(JsonSerializer.toObject(String.valueOf(character), colorGradient.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderline(), part.isObfuscated(), part.getActions()));
            }
        }

        return jsonArray;
    }

    public static String toGradientString(final List<MessagePart> parts, final Gradient gradient) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final ColorGradient colorGradient = new ColorGradient(colors, length);

        for (final MessagePart part : parts) {
            for (char character : part.getText().toCharArray()) {
                stringBuilder.append(StringSerializer.toObject(String.valueOf(character), colorGradient.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderline(), part.isObfuscated(), part.getActions()));
            }
        }

        return stringBuilder.toString();
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

        private String next() {
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
}
