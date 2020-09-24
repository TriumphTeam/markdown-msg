package me.mattstudios.mfmsg.base.internal.action;

import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Hover action part
 */
public final class HoverMessageAction implements MessageAction {

    @NotNull
    private final List<MessageNode> nodes;

    /**
     * Main constructor that takes a {@link List} of lines
     *
     * @param nodes The {@link List} of {@link MessageNode}
     */
    public HoverMessageAction(@NotNull final List<MessageNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * Gets the {@link List} of {@link MessageNode}
     *
     * @return The {@link List} of {@link MessageNode}
     */
    @NotNull
    public List<MessageNode> getNodes() {
        return nodes;
    }

}
