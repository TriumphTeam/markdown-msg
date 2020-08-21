package me.mattstudios.mfmsg.base.internal.action;

import me.mattstudios.mfmsg.base.internal.component.MessageLine;

import java.util.List;

public final class HoverAction implements Action {

    private final List<MessageLine> lines;

    public HoverAction(final List<MessageLine> lines) {
        this.lines = lines;
    }

    public List<MessageLine> getLines() {
        return lines;
    }

}
