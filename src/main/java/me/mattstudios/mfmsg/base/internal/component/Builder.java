package me.mattstudios.mfmsg.base.internal.component;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Builder<T> {

    void append(@NotNull final String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated);

    default void setHoverEvent(@Nullable final HoverEvent hoverEvent) {}

    default void setClickEvent(@Nullable final ClickEvent clickEvent) {}

    T build();
}
