package me.mattstudios.mfmsg.base.internal.action;

import me.mattstudios.mfmsg.base.internal.Format;

public final class ClickAction implements Action {

    private final Format actionType;
    private final String action;

    public ClickAction(final Format actionType, final String action) {
        this.actionType = actionType;
        this.action = action;
    }

    public Format getActionType() {
        return actionType;
    }

    public String getAction() {
        return action;
    }

}
