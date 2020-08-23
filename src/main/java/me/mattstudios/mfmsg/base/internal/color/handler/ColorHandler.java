package me.mattstudios.mfmsg.base.internal.color.handler;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import me.mattstudios.mfmsg.base.bukkit.nms.ServerVersion;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public final class ColorHandler {

    private final Set<Format> formats;
    private final MessageColor defaultColor;
    private MessageColor currentColor;

    public ColorHandler(final Set<Format> formats, final MessageColor defaultColor) {
        this.formats = formats;
        this.defaultColor = defaultColor;

        currentColor = defaultColor;
    }

    public List<MessagePart> colorize(@NotNull final String message, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, final List<Action> actions) {

        final List<MessagePart> parts = new ArrayList<>();
        final Matcher matcher = RegexUtils.COLOR_PATTERN.matcher(message);

        String rest = message;
        int start = 0;

        while (matcher.find()) {

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) {
                parts.add(new MessagePart(before, currentColor, bold, italic, strike, underline, obfuscated, actions));
            }

            final String colorChar = matcher.group("char");
            if (colorChar != null) {
                if (formats.contains(Format.COLOR)) {
                    currentColor = new FlatColor(ColorMapping.fromChar(colorChar.charAt(0)));
                } else {
                    if ("r".equalsIgnoreCase(colorChar)) currentColor = defaultColor;
                }
            }

            final String hex = matcher.group("hex");
            if (hex != null && formats.contains(Format.HEX)) currentColor = new FlatColor(ofHex(hex));

            final String gradient = matcher.group("gradient");
            if (gradient != null && !ServerVersion.CURRENT_VERSION.isColorLegacy() && formats.contains(Format.HEX)) {
                final List<String> colors = Arrays.asList(gradient.split(":"));
                if (colors.size() == 1) currentColor = new FlatColor(ofHex(colors.get(0)));
                else currentColor = new Gradient(colors.stream().map(this::hexToColor).collect(Collectors.toList()));
            }

            start = matcher.end();
            rest = message.substring(start);

        }

        if (!rest.isEmpty()) {
            parts.add(new MessagePart(rest, currentColor, bold, italic, strike, underline, obfuscated, actions));
        }

        return parts;
    }

    private String ofHex(final String color) {
        if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
            return ColorMapping.toLegacy(hexToColor(color));
        }
        return "#" + Integer.toHexString(hexToColor(color).getRGB()).substring(2);
    }

    private Color hexToColor(final String color) {
        final StringBuilder builder = new StringBuilder();
        final String hex = color.substring(1);

        builder.append("#");
        if (hex.length() == 6) {
            builder.append(hex);
        } else {
            builder.append(increaseHex(hex.substring(0, 3)));
        }

        return Color.decode(builder.toString());
    }

    private String increaseHex(final String hex) {
        return RegexUtils.THREE_HEX.matcher(hex).replaceAll("$1$1$2$2$3$3");
    }

}
