package me.mattstudios.mfmsg.base.internal.action;

import me.mattstudios.mfmsg.base.internal.action.content.HoverContent;
import org.jetbrains.annotations.NotNull;

/**
 * Hover action part
 */
public final class HoverMessageAction implements MessageAction {

    @NotNull
    private final HoverContent hoverContent;

    public HoverMessageAction(@NotNull final HoverContent hoverContent) {
        this.hoverContent = hoverContent;
    }

    public HoverContent getHoverContent() {
        return hoverContent;
    }

}
