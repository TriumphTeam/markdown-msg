package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.bukkit.BukkitComponent;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Main handler of the message lib
 */
public final class Message {

    @NotNull
    private final Set<Format> formats;
    @NotNull
    private final MessageColor defaultColor;

    /**
     * Main constructor with the format options
     *
     * @param formatOptions The format options with the data needed for the parser
     */
    private Message(@NotNull final FormatOptions formatOptions) {
        formats = formatOptions.getFormats();
        defaultColor = formatOptions.getDefaultColor();
    }

    /**
     * Creates a new {@link Message}
     *
     * @param formatOptions The format options to use
     * @return A new {@link Message}
     */
    @NotNull
    public static Message create(@NotNull final FormatOptions formatOptions) {
        return new Message(formatOptions);
    }

    /**
     * Alternative creator, without format options needed
     *
     * @return A new {@link Message}
     */
    @NotNull
    public static Message create() {
        return new Message(new FormatOptions());
    }

    /**
     * Parses the message into a useful {@link MessageComponent}
     *
     * @param message The message to parse
     * @return The {@link MessageComponent} generated
     */
    public MessageComponent parse(@NotNull final String message) {
        final List<MessageLine> lines = new ArrayList<>();

        for (final String line : RegexUtils.splitNewLine(message)) {
            lines.add(new MessageLine(new MessageParser(line, formats, defaultColor).build()));
        }

        return new BukkitComponent(lines);
    }

}
