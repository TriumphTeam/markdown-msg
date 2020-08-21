package me.mattstudios.mfmsg.base.internal.color.handler;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import me.mattstudios.mfmsg.base.nms.ServerVersion;
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

    public ColorHandler(final Set<Format> formats) {
        this.formats = formats;
    }

    private MessageColor color;

    public List<MessagePart> colorize(@NotNull final String message, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, final List<Action> actions) {

        final List<MessagePart> components = new ArrayList<>();
        final Matcher matcher = RegexUtils.COLOR_PATTERN.matcher(message);

        String rest = message;
        int start = 0;

        while (matcher.find()) {

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) {
                components.add(new MessagePart(before, color, bold, italic, strike, underline, obfuscated, actions));
            }

            final String colorChar = matcher.group("char");
            if (colorChar != null) {
                if (formats.contains(Format.COLOR)) {
                    color = new FlatColor(ColorMapping.fromChar(colorChar.charAt(0)));
                } else {
                    if ("r".equalsIgnoreCase(colorChar)) color = new FlatColor(ColorMapping.WHITE.color);
                }
            }

            final String hex = matcher.group("hex");
            if (hex != null && formats.contains(Format.HEX)) color = new FlatColor(ofHex(hex));

            final String gradient = matcher.group("gradient");
            if (gradient != null && !ServerVersion.CURRENT_VERSION.isColorLegacy() && formats.contains(Format.HEX)) {
                final List<String> colors = Arrays.asList(gradient.split(":"));
                if (colors.size() == 1) color = new FlatColor(ofHex(colors.get(0)));
                else color = new Gradient(colors.stream().map(this::hexToColor).collect(Collectors.toList()));
            }

            start = matcher.end();
            rest = message.substring(start);

        }

        if (!rest.isEmpty()) {
            components.add(new MessagePart(rest, color, bold, italic, strike, underline, obfuscated, actions));
        }

        return components;
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

    /**
     * Maps hex codes to ChatColors
     */
    private enum ColorMapping {

        BLACK(0x000000, "black", '0'),
        DARK_BLUE(0x0000AA, "dark_blue", '1'),
        DARK_GREEN(0x00AA00, "dark_green", '2'),
        DARK_AQUA(0x00AAAA, "dark_aqua", '3'),
        DARK_RED(0xAA0000, "dark_red", '4'),
        DARK_PURPLE(0xAA00AA, "dark_purple", '5'),
        GOLD(0xFFAA00, "gold", '6'),
        GRAY(0xAAAAAA, "gray", '7'),
        DARK_GRAY(0x555555, "dark_gray", '8'),
        BLUE(0x5555FF, "blue", '9'),
        GREEN(0x55FF55, "green", 'a'),
        AQUA(0x55FFFF, "aqua", 'b'),
        RED(0xFF5555, "red", 'c'),
        LIGHT_PURPLE(0xFF55FF, "light_purple", 'd'),
        YELLOW(0xFFFF55, "yellow", 'e'),
        WHITE(0xFFFFFF, "white", 'f');

        private final int r, g, b;
        private final char character;
        private final String color;

        ColorMapping(final int hex, final String color, final char character) {
            this.r = (hex >> 16) & 0xFF;
            this.g = (hex >> 8) & 0xFF;
            this.b = hex & 0xFF;
            this.color = color;
            this.character = character;
        }

        private static String fromChar(final char character) {
            for (ColorMapping mapping : values()) {
                if (mapping.character == character) return mapping.color;
            }

            return WHITE.color;
        }

        private static String toLegacy(final Color color) {
            int minDist = Integer.MAX_VALUE;
            String legacy = WHITE.color;
            for (ColorMapping mapping : values()) {
                int r = mapping.r - color.getRed();
                int g = mapping.g - color.getGreen();
                int b = mapping.b - color.getBlue();
                int dist = r * r + g * g + b * b;
                if (dist < minDist) {
                    minDist = dist;
                    legacy = mapping.color;
                }
            }

            return legacy;
        }

    }

}
