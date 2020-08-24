package me.mattstudios.mfmsg.base.internal.component;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Stores the message lines
 */
public final class MessageLine {

    @NotNull
    private final List<MessagePart> parts;

    /**
     * Main constructor to add all the parts
     *
     * @param parts The {@link MessagePart}s to add to the line
     */
    public MessageLine(@NotNull final List<MessagePart> parts) {
        this.parts = parts;
    }

    /**
     * Gets all the parts in the current line
     *
     * @return The {@link MessagePart}s of the line
     */
    @NotNull
    public List<MessagePart> getParts() {
        return parts;
    }

}
