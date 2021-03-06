package me.mattstudios.msg.base.serializer;

import me.mattstudios.msg.base.internal.action.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Appender<T> {

    void append(@NotNull String value);

    void appendNode(@NotNull final String text, @Nullable final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @Nullable final List<MessageAction> actions);

    @NotNull
    T build();

}
