package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.color.ColorHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class MessageAppender implements Appender {

    private final ColorHandler colorHandler;

    final List<MessagePart> parts = new ArrayList<>();

    private HoverEvent hoverEvent = null;
    private ClickEvent clickEvent = null;

    public MessageAppender(final ColorHandler colorHandler) {
        this.colorHandler = colorHandler;
    }

    @Override
    public void append(@NotNull final String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated) {
        parts.addAll(colorHandler.colorize(message, bold, italic, strike, underline, obfuscated));
    }

    @Override
    public void setHoverEvent(final @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    @Override
    public void setClickEvent(final @Nullable ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Override
    public List<MessagePart> build() {
        final List<MessagePart> test = new ArrayList<>(parts);
        reset();
        return test;
    }

    private void reset() {
        hoverEvent = null;
        clickEvent = null;
        parts.clear();
    }

}
