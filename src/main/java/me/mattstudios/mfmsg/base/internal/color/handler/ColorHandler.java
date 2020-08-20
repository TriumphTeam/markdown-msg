package me.mattstudios.mfmsg.base.internal.color.handler;

import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import me.mattstudios.mfmsg.base.internal.util.ServerVersion;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public final class ColorHandler {

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
                color = new FlatColor(ChatColor.getByChar(colorChar.charAt(0)));
            }

            final String hex = matcher.group("hex");
            if (hex != null) color = new FlatColor(ofHex(hex));

            final String gradient = matcher.group("gradient");
            if (gradient != null && !ServerVersion.CURRENT_VERSION.isColorLegacy()) {
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

    private ChatColor ofHex(final String color) {
        if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
            return ChatColorHexMapping.toLegacy(hexToColor(color));
        }
        return ChatColor.of(hexToColor(color));
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
    private enum ChatColorHexMapping {

        BLACK(0x000000, ChatColor.BLACK),
        DARK_BLUE(0x0000AA, ChatColor.DARK_BLUE),
        DARK_GREEN(0x00AA00, ChatColor.DARK_GREEN),
        DARK_AQUA(0x00AAAA, ChatColor.DARK_AQUA),
        DARK_RED(0xAA0000, ChatColor.DARK_RED),
        DARK_PURPLE(0xAA00AA, ChatColor.DARK_PURPLE),
        GOLD(0xFFAA00, ChatColor.GOLD),
        GRAY(0xAAAAAA, ChatColor.GRAY),
        DARK_GRAY(0x555555, ChatColor.DARK_GRAY),
        BLUE(0x5555FF, ChatColor.BLUE),
        GREEN(0x55FF55, ChatColor.GREEN),
        AQUA(0x55FFFF, ChatColor.AQUA),
        RED(0xFF5555, ChatColor.RED),
        LIGHT_PURPLE(0xFF55FF, ChatColor.LIGHT_PURPLE),
        YELLOW(0xFFFF55, ChatColor.YELLOW),
        WHITE(0xFFFFFF, ChatColor.WHITE);

        private final int r, g, b;
        private final ChatColor chatColor;

        ChatColorHexMapping(int hex, ChatColor chatColor) {
            this.r = (hex >> 16) & 0xFF;
            this.g = (hex >> 8) & 0xFF;
            this.b = hex & 0xFF;
            this.chatColor = chatColor;
        }

        private static ChatColor toLegacy(final Color color) {
            int minDist = Integer.MAX_VALUE;
            ChatColor legacy = ChatColor.WHITE;
            for (ChatColorHexMapping mapping : values()) {
                int r = mapping.r - color.getRed();
                int g = mapping.g - color.getGreen();
                int b = mapping.b - color.getBlue();
                int dist = r * r + g * g + b * b;
                if (dist < minDist) {
                    minDist = dist;
                    legacy = mapping.chatColor;
                }
            }

            return legacy;
        }

    }

}
