package me.mattstudios.mfmsg.base.serializer;

import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.handler.GradientHandler;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class StringSerializer {

    private StringSerializer() {}

    public static String toString(final List<MessageLine> lines) {
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

    private static String convertLine(final List<MessagePart> parts) {
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

                stringBuilder.append(GradientHandler.toGradientString(gradientParts, gradient));
                continue;
            }

            String colorString = null;
            if (color != null) colorString = ((FlatColor) color).getColor();

            stringBuilder.append(toObject(part.getText(), colorString, part.isBold(), part.isItalic(), part.isStrike(), part.isUnderline(), part.isObfuscated(), part.getActions()));
        }

        return stringBuilder.toString();
    }

    public static String toObject(final String text, final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, final List<Action> actions) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (color != null) stringBuilder.append("§x").append(color.substring(1).replaceAll(".", "§$0"));

        if (bold) stringBuilder.append("§l");
        if (italic) stringBuilder.append("§o");
        if (strike) stringBuilder.append("§m");
        if (underline) stringBuilder.append("§n");
        if (obfuscated) stringBuilder.append("§k");

        stringBuilder.append(text);
        stringBuilder.append("§r");

        return stringBuilder.toString();
    }

}
