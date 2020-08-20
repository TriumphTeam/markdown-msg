package me.mattstudios.mfmsg.base.internal.action;

import me.mattstudios.mfmsg.base.internal.component.MessagePart;

import java.util.List;
import java.util.stream.Collectors;

public final class HoverAction implements Action {

    private final List<List<MessagePart>> parts;

    public HoverAction(final List<List<MessagePart>> parts) {
        this.parts = parts;
    }

    public List<List<MessagePart>> getParts() {
        return parts;
    }

}
