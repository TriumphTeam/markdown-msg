package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.bukkit.BukkitComponent;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Message {

    private final Set<Format> formats;
    private final MessageColor defaultColor;

    private Message(final FormatOptions formatOptions) {
        formats = formatOptions.getFormats();
        defaultColor = formatOptions.getDefaultColor();
    }

    public static Message create(final FormatOptions formatOptions) {
        return new Message(formatOptions);
    }

    public static Message create() {
        return new Message(new FormatOptions());
    }

    public MessageComponent parse(final String message) {
        final List<MessageLine> lines = new ArrayList<>();

        for (final String line : RegexUtils.splitNewLine(message)) {
            lines.add(new MessageLine(new MessageParser(line, formats, defaultColor).parse()));
        }

        return new BukkitComponent(lines);
    }

}
