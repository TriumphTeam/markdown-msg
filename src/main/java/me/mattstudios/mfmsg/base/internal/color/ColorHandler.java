package me.mattstudios.mfmsg.base.internal.color;

import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorHandler {

    private static final Pattern HEX = Pattern.compile("(?<!\\\\)[<&](?<hex>#[A-Fa-f0-9]{3,6})[>]?|(?<!\\\\)(?<stop>&r)|<g:(?<gradient>.+?)>");

    private MessageColor color;

    public List<MessagePart> colorize(@NotNull final String message, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated) {

        final List<MessagePart> components = new ArrayList<>();
        final Matcher matcher = HEX.matcher(message);

        String rest = message;
        int start = 0;

        while (matcher.find()) {

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) {
                components.add(new MessagePart(before, color, bold, italic, strike, underline, obfuscated));
            }

            if (matcher.group("stop") != null) color = null;

            final String hex = matcher.group("hex");
            if (hex != null) color = new FlatColor(hex);

            final String gradient = matcher.group("gradient");
            if (gradient != null) color = new Gradient(Arrays.asList(gradient.split(":")));

            start = matcher.end();
            rest = message.substring(start);

        }

        if (!rest.isEmpty()) {
            components.add(new MessagePart(rest, color, bold, italic, strike, underline, obfuscated));
        }

        return components;
    }

}
