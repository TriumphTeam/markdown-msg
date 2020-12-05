package me.mattstudios.msg.base.internal.extensions;

import me.mattstudios.msg.base.internal.extensions.delimiter.StrikethroughDelimiterProcessor;
import me.mattstudios.msg.commonmark.Extension;
import me.mattstudios.msg.commonmark.parser.Parser;
import me.mattstudios.msg.commonmark.parser.ParserExtension;
import org.jetbrains.annotations.NotNull;

public final class StrikethroughExtension implements ParserExtension {

    private StrikethroughExtension() {}

    public static Extension create() {
        return new StrikethroughExtension();
    }

    @Override
    public void extend(@NotNull final Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new StrikethroughDelimiterProcessor());
    }

}
