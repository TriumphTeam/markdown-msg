package me.mattstudios.mfmsg.adventure;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

/**
 * Adventure message to parse the markdown into adventure
 */
public class AdventureMessage {

    @NotNull
    private final MessageOptions messageOptions;

    /**
     * Private constructor for the message
     *
     * @param messageOptions The {@link MessageOptions} with the settings needed to run
     */
    private AdventureMessage(@NotNull final MessageOptions messageOptions) {
        this.messageOptions = messageOptions;
    }

    /**
     * Creates a new {@link AdventureMessage}
     *
     * @param messageOptions The {@link MessageOptions} with the settings needed to run
     * @return A new {@link AdventureMessage}
     */
    @NotNull
    public static AdventureMessage create(@NotNull final MessageOptions messageOptions) {
        return new AdventureMessage(messageOptions);
    }

    /**
     * Alternative constructor that uses default {@link MessageOptions}
     *
     * @return A new {@link AdventureMessage}
     */
    @NotNull
    public static AdventureMessage create() {
        return create(new MessageOptions.Builder(EnumSet.allOf(Format.class)).build());
    }

    /**
     * Parses the message into a {@link Component}
     *
     * @param message The markdown message
     * @return A parsed {@link Component}
     */
    public Component parse(@NotNull final String message) {
        final MessageParser parser = new MessageParser(messageOptions);
        parser.parse(message);
        return AdventureSerializer.toComponent(parser.build());
    }

    public List<MessageNode> parseToNodes(@NotNull final String message) {
        final MessageParser parser = new MessageParser(messageOptions);
        parser.parse(message);
        return parser.build();
    }

}
