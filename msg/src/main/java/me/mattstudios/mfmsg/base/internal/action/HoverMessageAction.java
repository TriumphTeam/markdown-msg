package me.mattstudios.mfmsg.base.internal.action;

import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Hover action part
 */
public final class HoverMessageAction implements MessageAction {

    @NotNull
    private final List<MessageLine> lines;

    /**
     * Main constructor that takes a {@link List} of lines
     *
     * @param lines The {@link List} of {@link MessageLine}
     */
    public HoverMessageAction(@NotNull final List<MessageLine> lines) {
        this.lines = lines;
    }

    /**
     * Gets the {@link List} of {@link MessageLine}
     *
     * @return The {@link List} of {@link MessageLine}
     */
    @NotNull
    public List<MessageLine> getLines() {
        return lines;
    }

}
