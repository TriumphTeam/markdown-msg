package me.mattstudios.mfmsg.base.serializer;

import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Appender {

    void append(@NotNull String value);

    void appendNode(@NotNull final String text, @Nullable final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @Nullable final List<MessageAction> actions);

    String build();

}
