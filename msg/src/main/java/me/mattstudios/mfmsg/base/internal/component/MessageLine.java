package me.mattstudios.mfmsg.base.internal.component;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Stores the message lines
 */
public final class MessageLine {

    @NotNull
    private final List<MessageNode> parts;

    /**
     * Main constructor to add all the parts
     *
     * @param parts The {@link MessageNode}s to add to the line
     */
    public MessageLine(@NotNull final List<MessageNode> parts) {
        this.parts = parts;
    }

    /**
     * Gets all the parts in the current line
     *
     * @return The {@link MessageNode}s of the line
     */
    @NotNull
    public List<MessageNode> getParts() {
        return parts;
    }

}
