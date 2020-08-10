package me.mattstudios.mfmsg.base.internal.token;

public final class ActionToken implements Token {

    private final String actionText;
    private final String actions;

    public ActionToken(final String actionText, final String actions) {
        this.actionText = actionText;
        this.actions = actions;
    }

    public String getActionText() {
        return actionText;
    }

    public String getActions() {
        return actions;
    }
}
