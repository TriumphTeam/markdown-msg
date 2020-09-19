package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.handler.ColorHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Message appender that'll append the message parts
 */
public final class MessageAppender implements Appender {

    private final ColorHandler colorHandler;

    @NotNull
    private final List<MessageNode> parts = new ArrayList<>();
    @NotNull
    private final List<Action> actions = new ArrayList<>();

    /**
     * Main constructor
     *
     * @param formats      The formats to be used in the color etc
     * @param defaultColor The default color if one is added
     */
    public MessageAppender(@NotNull final Set<Format> formats, @NotNull final MessageColor defaultColor) {
        colorHandler = new ColorHandler(formats, defaultColor);
    }

    /**
     * Appends the message and it's attributes
     *
     * @param message    The message to append
     * @param italic     Whether or not the message is italic
     * @param bold       Whether or not the message is bold
     * @param strike     Whether or not the message is strikethrough
     * @param underline  Whether or not the message is underlined
     * @param obfuscated Whether or not the message is obfuscated
     */
    @Override
    public void append(@NotNull final String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated) {
        parts.addAll(colorHandler.colorize(message, bold, italic, strike, underline, obfuscated, new ArrayList<>(actions)));
    }

    /**
     * Adds the actions to the message
     *
     * @param actions The actions to add
     */
    @Override
    public void addActions(@Nullable final List<Action> actions) {
        if (actions == null) return;
        this.actions.addAll(actions);
    }

    /**
     * Builds the message into a list of message parts
     *
     * @return The list of all the message parts
     */
    @NotNull
    @Override
    public List<MessageNode> build() {
        // Requires to be copied to not be mutated
        final List<MessageNode> test = new ArrayList<>(parts);
        reset();
        return test;
    }

    /**
     * Resets everything for the next parts
     */
    private void reset() {
        parts.clear();
        actions.clear();
    }

}
