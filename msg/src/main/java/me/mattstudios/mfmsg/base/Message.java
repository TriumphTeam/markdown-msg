package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.bukkit.BukkitComponent;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import org.jetbrains.annotations.NotNull;

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
     * @param messageOptions The format options with the data needed for the parser
     */
    private Message(@NotNull final MessageOptions messageOptions) {
        formats = messageOptions.getFormats();
        defaultColor = messageOptions.getDefaultColor();
    }

    /**
     * Creates a new {@link Message}
     *
     * @param messageOptions The format options to use
     * @return A new {@link Message}
     */
    @NotNull
    public static Message create(@NotNull final MessageOptions messageOptions) {
        return new Message(messageOptions);
    }

    /**
     * Alternative creator, without format options needed
     *
     * @return A new {@link Message}
     */
    @NotNull
    public static Message create() {
        return new Message(new MessageOptions());
    }

    /**
     * Parses the message into a useful {@link MessageComponent}
     *
     * @param message The message to parse
     * @return The {@link MessageComponent} generated
     */
    public MessageComponent parse(@NotNull final String message) {
        /*if (!formats.contains(Format.NEW_LINE)) {
            return new BukkitComponent(Collections.singletonList(new MessageLine(new MessageParser(message, formats, defaultColor).build())));
        }

        final List<MessageLine> lines = new ArrayList<>();

        for (final String line : message.split("\\n")) {
            lines.add(new MessageLine(new MessageParser(line, formats, defaultColor).build()));
        }*/

        final MessageParser parser = new MessageParser(formats, defaultColor);
        parser.parse(message);
        //System.out.println(parser.build());

        return new BukkitComponent(parser.build());
    }

}
