package me.mattstudios.msg.base.internal.action;

import me.mattstudios.msg.base.internal.Format;
import org.jetbrains.annotations.NotNull;

/**
 * Click action part
 */
public final class ClickMessageAction implements MessageAction {

    @NotNull
    private final Format actionType;
    @NotNull
    private final String action;

    /**
     * Main constructor
     *
     * @param actionType The action {@link Format} for identifying the click type
     * @param action     The action to be resolved
     */
    public ClickMessageAction(@NotNull final Format actionType, @NotNull final String action) {
        this.actionType = actionType;
        this.action = action;
    }

    /**
     * Gets the action {@link Format}
     *
     * @return The action type
     */
    @NotNull
    public Format getActionType() {
        return actionType;
    }

    /**
     * Gets the action text
     *
     * @return The action text
     */
    @NotNull
    public String getAction() {
        return action;
    }

}
