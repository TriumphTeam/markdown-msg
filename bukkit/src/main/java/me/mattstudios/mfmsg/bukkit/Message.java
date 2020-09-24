package me.mattstudios.mfmsg.bukkit;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

/**
 * Main handler of the message lib
 */
public final class Message {

    @NotNull
    private final MessageOptions messageOptions;

    /**
     * Main constructor with the format options
     *
     * @param messageOptions The format options with the data needed for the parser
     */
    private Message(@NotNull final MessageOptions messageOptions) {
        this.messageOptions = messageOptions;
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
        return new Message(new MessageOptions.Builder(EnumSet.allOf(Format.class)).build());
    }

    /**
     * Parses the message into a useful {@link MessageComponent}
     *
     * @param message The message to parse
     * @return The {@link MessageComponent} generated
     */
    public MessageComponent parse(@NotNull final String message) {
        final MessageParser parser = new MessageParser(messageOptions);
        parser.parse(message);
        return new BukkitComponent(parser.build());
    }

}
