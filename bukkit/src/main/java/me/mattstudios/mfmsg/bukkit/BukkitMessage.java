package me.mattstudios.mfmsg.bukkit;

import me.mattstudios.mfmsg.base.MarkdownMessage;
import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Main handler of the message lib
 */
public final class BukkitMessage extends MarkdownMessage<MessageComponent> {

    /**
     * Main constructor with the format options
     *
     * @param messageOptions The message options with the data needed for the parser
     */
    private BukkitMessage(@NotNull final MessageOptions messageOptions) {
        super(messageOptions);
    }

    /**
     * Creates a new {@link BukkitMessage}
     *
     * @param messageOptions The format options to use
     * @return A new {@link BukkitMessage}
     */
    @NotNull
    public static BukkitMessage create(@NotNull final MessageOptions messageOptions) {
        return new BukkitMessage(messageOptions);
    }

    /**
     * Alternative creator, without message options needed
     *
     * @return A new {@link BukkitMessage}
     */
    @NotNull
    public static BukkitMessage create() {
        return create(MessageOptions.builder().build());
    }

    /**
     * Parses the message into a useful {@link MessageComponent}
     *
     * @param message The message to parse
     * @return The {@link MessageComponent} generated
     */
    @NotNull
    @Override
    public MessageComponent parse(@NotNull final String message) {
        return new BukkitComponent(getParser().parse(message));
    }

    /**
     * Parses a message into raw {@link MessageNode} list
     *
     * @param message The message to parse
     * @return A {@link List} with the {@link MessageNode}s
     */
    @NotNull
    @Override
    public List<MessageNode> parseToNodes(@NotNull final String message) {
        return getParser().parse(message);
    }

}
