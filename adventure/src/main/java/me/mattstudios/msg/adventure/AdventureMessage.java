package me.mattstudios.msg.adventure;

import me.mattstudios.msg.base.MarkdownMessage;
import me.mattstudios.msg.base.MessageOptions;
import me.mattstudios.msg.base.internal.Format;
import me.mattstudios.msg.base.internal.components.MessageNode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

/**
 * Adventure message to parse the markdown into adventure
 */
public class AdventureMessage extends MarkdownMessage<Component> {

    /**
     * Private constructor for the message
     *
     * @param messageOptions The {@link MessageOptions} with the settings needed to run
     */
    private AdventureMessage(@NotNull final MessageOptions messageOptions) {
        super(messageOptions);
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
    @NotNull
    @Override
    public Component parse(@NotNull final String message) {
        return AdventureSerializer.toComponent(getParser().parse(message));
    }

    @NotNull
    @Override
    public List<MessageNode> parseToNodes(@NotNull final String message) {
        return getParser().parse(message);
    }

}
