package me.mattstudios.mfmsg.base.internal.extensions;

import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReplaceableHandler extends Parser.ParserExtension{

    @Nullable
    MessageNode getNode(@NotNull String literal);

    @NotNull
    String getOpener();

    @NotNull
    String getCloser();

}
