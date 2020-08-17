package me.mattstudios.mfmsg.base.internal.component;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorComponent {

    private static final Pattern HEX = Pattern.compile("<(?<hex>#[A-Fa-f0-9]{6})>|&(?<hex1>#[A-Fa-f0-9]{6})|(?<hex2>#[A-Fa-f0-9]{6})|(?<stop>&r)");

    private String color;

    public List<Component> colorize(@NotNull final String message, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated) {

        final List<Component> components = new ArrayList<>();
        final Matcher matcher = HEX.matcher(message);

        String rest = message;
        int start = 0;

        while (matcher.find()) {

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty() && !" ".equals(before)) {
                components.add(new Component(before, color, bold, italic, strike, underline, obfuscated));
            }

            if (matcher.group("stop") != null) color = null;

            final String hex = matcher.group("hex");
            if (hex != null) color = hex;

            start = matcher.end();
            rest = message.substring(start);

        }

        if (!rest.isEmpty()) {
            components.add(new Component(rest, color, bold, italic, strike, underline, obfuscated));
        }

        return components;
    }

}
