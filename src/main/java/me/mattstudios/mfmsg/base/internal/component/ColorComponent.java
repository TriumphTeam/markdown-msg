package me.mattstudios.mfmsg.base.internal.component;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorComponent {

    private static final Pattern HEX = Pattern.compile("<(?<hex>#[A-Fa-f0-9]{6})>|(?<stop>&r)");

    private String color;

    public BaseComponent[] colorize(@NotNull final String message) {
        final ComponentBuilder builder = new ComponentBuilder();
        final Matcher matcher = HEX.matcher(message);

        String rest = message;
        int start = 0;

        while (matcher.find()) {

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) {
                builder.append(before, ComponentBuilder.FormatRetention.NONE);
                if (color != null) builder.color(ChatColor.of(color));
            }

            if (matcher.group("stop") != null) color = null;

            final String hex = matcher.group("hex");
            if (hex != null) color = hex;

            start = matcher.end();
            rest = message.substring(start);

        }

        if (!rest.isEmpty()) {
            builder.append(rest, ComponentBuilder.FormatRetention.NONE);
            if (color != null) builder.color(ChatColor.of(color));
        }

        return builder.create();
    }

}
