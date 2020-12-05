package me.mattstudios.msg.commonmark.parser;

import me.mattstudios.msg.commonmark.Extension;
import org.jetbrains.annotations.NotNull;

public interface ParserExtension extends Extension {

    void extend(@NotNull final Parser.Builder builder);

}
