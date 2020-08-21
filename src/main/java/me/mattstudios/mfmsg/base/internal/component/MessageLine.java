package me.mattstudios.mfmsg.base.internal.component;

import java.util.List;

public final class MessageLine {

    private final List<MessagePart> parts;

    public MessageLine(final List<MessagePart> parts) {
        this.parts = parts;
    }

    public List<MessagePart> getParts() {
        return parts;
    }
    
}
