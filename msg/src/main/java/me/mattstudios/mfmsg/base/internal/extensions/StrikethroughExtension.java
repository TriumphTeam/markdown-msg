package me.mattstudios.mfmsg.base.internal.extensions;

import me.mattstudios.mfmsg.base.internal.extensions.delimiter.StrikethroughDelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.Extension;
import me.mattstudios.mfmsg.commonmark.parser.Parser;

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
