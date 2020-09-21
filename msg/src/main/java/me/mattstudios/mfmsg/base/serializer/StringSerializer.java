package me.mattstudios.mfmsg.base.serializer;

import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Will comment this class later, it's currently pretty messy
 */
public final class StringSerializer {

    private StringSerializer() {}

    @NotNull
    public static String toString(@NotNull final List<MessageNode> lines) {
        final StringBuilder stringBuilder = new StringBuilder();

        /*final Iterator<MessageLine> iterator = lines.iterator();
        while (iterator.hasNext()) {
            //stringBuilder.append(convertLine(iterator.next().getParts()));
            if (iterator.hasNext()) {
                stringBuilder.append("\n");
            }
        }*/

        return stringBuilder.toString();
    }

    /*@NotNull
    private static String convertLine(@NotNull final List<MessageNode> parts) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            final MessageNode part = parts.get(i);

            final MessageColor color = part.getColor();

            if (color instanceof Gradient) {
                final List<MessageNode> gradientParts = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessageNode newPart = parts.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    gradientParts.add(newPart);
                    i++;
                }

                stringBuilder.append(toGradient(gradientParts, gradient));
                continue;
            }

            if (color instanceof Rainbow) {
                final List<MessageNode> rainbowParts = new ArrayList<>();
                final Rainbow rainbow = (Rainbow) color;
                rainbowParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessageNode newPart = parts.get(i + 1);
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
    }*/

    /*@NotNull
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
    public static String toGradient(@NotNull final List<MessageNode> parts, @NotNull final Gradient gradient) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final GradientHandler gradientHandler = new GradientHandler(colors, length);

        for (final MessageNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                stringBuilder.append(serializePart(String.valueOf(character), gradientHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated()));
            }
        }

        return stringBuilder.toString();
    }

    @NotNull
    private static String toRainbow(@NotNull final List<MessageNode> parts, @NotNull final Rainbow rainbow) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();

        final RainbowHandler rainbowHandler = new RainbowHandler(length, rainbow.getSaturation(), rainbow.getBrightness());

        for (final MessageNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                stringBuilder.append(serializePart(String.valueOf(character), rainbowHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated()));
            }
        }

        return stringBuilder.toString();
    }*/

}
