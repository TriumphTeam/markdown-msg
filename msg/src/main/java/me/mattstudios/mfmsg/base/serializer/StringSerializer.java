package me.mattstudios.mfmsg.base.serializer;

import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.Rainbow;
import me.mattstudios.mfmsg.base.internal.color.handler.ColorMapping;
import me.mattstudios.mfmsg.base.internal.color.handler.GradientHandler;
import me.mattstudios.mfmsg.base.internal.color.handler.RainbowHandler;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Will comment this class later, it's currently pretty messy
 */
public final class StringSerializer {

    private StringSerializer() {}

    @NotNull
    public static String toString(@NotNull final List<MessageLine> lines) {
        final StringBuilder stringBuilder = new StringBuilder();

        final Iterator<MessageLine> iterator = lines.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(convertLine(iterator.next().getParts()));
            if (iterator.hasNext()) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    @NotNull
    private static String convertLine(@NotNull final List<MessagePart> parts) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            final MessagePart part = parts.get(i);

            final MessageColor color = part.getColor();

            if (color instanceof Gradient) {
                final List<MessagePart> gradientParts = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessagePart newPart = parts.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    gradientParts.add(newPart);
                    i++;
                }

                stringBuilder.append(toGradient(gradientParts, gradient));
                continue;
            }

            if (color instanceof Rainbow) {
                final List<MessagePart> rainbowParts = new ArrayList<>();
                final Rainbow rainbow = (Rainbow) color;
                rainbowParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessagePart newPart = parts.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    rainbowParts.add(newPart);
                    i++;
                }

                stringBuilder.append(toRainbow(rainbowParts, rainbow));
                continue;
            }

            final String colorString = ((FlatColor) color).getColor();
            stringBuilder.append(serializePart(part.getText(), colorString, part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated()));
        }

        return stringBuilder.toString();
    }

    @NotNull
    public static String serializePart(@NotNull final String text, @NotNull final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (color.startsWith("#")) {
            stringBuilder.append("§x").append(RegexUtils.CHARACTER.matcher(color.substring(1)).replaceAll("§$0"));
        } else {
            stringBuilder.append("§").append(ColorMapping.fromName(color));
        }

        if (bold) stringBuilder.append("§l");
        if (italic) stringBuilder.append("§o");
        if (strike) stringBuilder.append("§m");
        if (underline) stringBuilder.append("§n");
        if (obfuscated) stringBuilder.append("§k");

        stringBuilder.append(text);
        stringBuilder.append("§r");

        return stringBuilder.toString();
    }

    @NotNull
    public static String toGradient(@NotNull final List<MessagePart> parts, @NotNull final Gradient gradient) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final GradientHandler gradientHandler = new GradientHandler(colors, length);

        for (final MessagePart part : parts) {
            for (char character : part.getText().toCharArray()) {
                stringBuilder.append(serializePart(String.valueOf(character), gradientHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated()));
            }
        }

        return stringBuilder.toString();
    }

    @NotNull
    private static String toRainbow(@NotNull final List<MessagePart> parts, @NotNull final Rainbow rainbow) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();

        final RainbowHandler rainbowHandler = new RainbowHandler(length, rainbow.getSaturation(), rainbow.getBrightness());

        for (final MessagePart part : parts) {
            for (char character : part.getText().toCharArray()) {
                stringBuilder.append(serializePart(String.valueOf(character), rainbowHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated()));
            }
        }

        return stringBuilder.toString();
    }

}
