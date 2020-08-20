package me.mattstudios.mfmsg.base.internal.color.handler;

import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
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
                if ("r".equalsIgnoreCase(colorChar)) {
                    color = null;
                } else {
                    color = new FlatColor(ChatColor.getByChar(colorChar.charAt(0)));
                }
            }

            final String hex = matcher.group("hex");
            if (hex != null) color = new FlatColor(ofHex(hex));

            final String gradient = matcher.group("gradient");
            if (gradient != null) {
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

}
