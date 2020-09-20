package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This interface isn't really necessary but it's good to have
 */
public interface Appender {

    /**
     * Appends the message with all it's info
     *
     * @param message    The message to append
     * @param italic     Whether or not the message is italic
     * @param bold       Whether or not the message is bold
     * @param strike     Whether or not the message is strikethrough
     * @param underline  Whether or not the message is underlined
     * @param obfuscated Whether or not the message is obfuscated
     */
    void append(@NotNull final String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated);

    /**
     * Adds the actions to the appender
     *
     * @param messageActions The actions to add
     */
    void addActions(@NotNull final List<MessageAction> messageActions);

    /**
     * Builds into a list of message parts
     *
     * @return The list of message parts
     */
    @NotNull
    List<MessageNode> build();

}
