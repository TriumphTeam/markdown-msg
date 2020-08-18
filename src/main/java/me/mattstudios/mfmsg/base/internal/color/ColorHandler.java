package me.mattstudios.mfmsg.base.internal.color;

import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ColorHandler {

    private static final Pattern COLOR_PATTERN = Pattern.compile("(?<!\\\\)[<&](?<hex>#[A-Fa-f0-9]{3,6})[>]?|(?<!\\\\)(?<stop>&r)|<g:(?<gradient>.+?)>");
    private static final Pattern THREE_HEX = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");

    private MessageColor color;

    public List<MessagePart> colorize(@NotNull final String message, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, final List<Action> actions) {

        final List<MessagePart> components = new ArrayList<>();
        final Matcher matcher = COLOR_PATTERN.matcher(message);

        String rest = message;
        int start = 0;

        while (matcher.find()) {

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) {
                components.add(new MessagePart(before, color, bold, italic, strike, underline, obfuscated, actions));
            }

            if (matcher.group("stop") != null) color = null;

            final String hex = matcher.group("hex");
            if (hex != null) color = new FlatColor(ofHex(hex));

            final String gradient = matcher.group("gradient");
            if (gradient != null) color = new Gradient(Arrays.stream(gradient.split(":")).map(this::ofHex).collect(Collectors.toList()));

            start = matcher.end();
            rest = message.substring(start);

        }

        if (!rest.isEmpty()) {
            components.add(new MessagePart(rest, color, bold, italic, strike, underline, obfuscated, actions));
        }

        return components;
    }

    private ChatColor ofHex(final String color) {
        final StringBuilder builder = new StringBuilder();
        final String hex = color.substring(1);

        builder.append("#");
        if (hex.length() == 6) {
            builder.append(hex);
        } else {
            builder.append(increaseHex(hex.substring(0, 3)));
        }

        return ChatColor.of(builder.toString());
    }

    private String increaseHex(final String hex) {
        return THREE_HEX.matcher(hex).replaceAll("$1$1$2$2$3$3");
    }

}
