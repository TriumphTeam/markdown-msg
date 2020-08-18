package me.mattstudios.mfmsg.base.internal.action;

import me.mattstudios.mfmsg.base.internal.component.MessagePart;

import java.util.List;
import java.util.stream.Collectors;

public final class HoverAction implements Action {

    private final List<MessagePart> parts;

    public HoverAction(final List<MessagePart> parts) {
        this.parts = parts;
    }

    public List<MessagePart> getParts() {
        return parts;
    }

    @Override
    public String test() {
        return "{Hover: " + parts.stream().map(MessagePart::test).collect(Collectors.joining(", ")) + "}";
    }

}
