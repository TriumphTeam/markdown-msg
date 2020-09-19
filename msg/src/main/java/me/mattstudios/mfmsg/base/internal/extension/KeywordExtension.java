package me.mattstudios.mfmsg.base.internal.extension;

import me.mattstudios.mfmsg.base.internal.extension.delimiter.KeywordDelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.Extension;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

public final class KeywordExtension implements Parser.ParserExtension {

    private KeywordExtension() {}

    @NotNull
    public static Extension create() {
        return new KeywordExtension();
    }

    @Override
    public void extend(final Parser.Builder builder) {
        builder.customDelimiterProcessor(new KeywordDelimiterProcessor());
    }

}
