package me.mattstudios.msg.base.internal.extensions;

import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.commonmark.parser.Parser;
import me.mattstudios.msg.commonmark.parser.ParserExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReplaceableHandler extends ParserExtension {

    @Nullable
    MessageNode getNode(@NotNull String literal);

    @NotNull
    String getOpener();

    @NotNull
    String getCloser();

}
