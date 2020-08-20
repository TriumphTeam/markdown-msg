package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.handler.ColorHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class MessageAppender implements Appender {

    private final ColorHandler colorHandler;

    private final List<MessagePart> parts = new ArrayList<>();

    private final List<Action> actions = new ArrayList<>();

    public MessageAppender(final Set<Format> formats) {
        colorHandler = new ColorHandler(formats);
    }

    @Override
    public void append(@NotNull final String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated) {
        parts.addAll(colorHandler.colorize(message, bold, italic, strike, underline, obfuscated, new ArrayList<>(actions)));
    }

    @Override
    public void addActions(@Nullable final List<Action> actions) {
        if (actions == null) return;
        this.actions.addAll(actions);
    }

    @Override
    public List<MessagePart> build() {
        final List<MessagePart> test = new ArrayList<>(parts);
        reset();
        return test;
    }

    private void reset() {
        parts.clear();
        actions.clear();
    }

}
