package me.mattstudios.mfmsg.base.internal.token;

import org.jetbrains.annotations.NotNull;

/**
 * Action token
 */
public final class ActionToken implements Token {

    @NotNull
    private final String actionText;
    @NotNull
    private final String actions;

    /**
     * Main constructor with the action text and the action
     *
     * @param actionText The action text
     * @param actions    The action to execute
     */
    public ActionToken(@NotNull final String actionText, @NotNull final String actions) {
        this.actionText = actionText;
        this.actions = actions;
    }

    /**
     * Gets the action text
     *
     * @return The action text
     */
    @NotNull
    public String getActionText() {
        return actionText;
    }

    /**
     * Gets the action
     *
     * @return The action
     */
    @NotNull
    public String getActions() {
        return actions;
    }
}
