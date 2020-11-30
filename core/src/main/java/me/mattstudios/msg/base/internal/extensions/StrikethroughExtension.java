package me.mattstudios.msg.base.internal.extensions;

import me.mattstudios.msg.base.internal.extensions.delimiter.StrikethroughDelimiterProcessor;
import me.mattstudios.msg.commonmark.Extension;
import me.mattstudios.msg.commonmark.parser.Parser;

public final class StrikethroughExtension implements Parser.ParserExtension {

    private StrikethroughExtension() {}

    public static Extension create() {
        return new StrikethroughExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new StrikethroughDelimiterProcessor());
    }

}
