package me.mattstudios.mfmsg.base.internal.extension;

import me.mattstudios.mfmsg.base.internal.extension.delimiter.UnderlineDelimiterProcessor;
import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

public final class UnderlineExtension implements Parser.ParserExtension {

    private UnderlineExtension() {}

    @NotNull
    public static Extension create() {
        return new UnderlineExtension();
    }

    @Override
    public void extend(final Parser.Builder builder) {
        builder.customDelimiterProcessor(new UnderlineDelimiterProcessor());
    }

}
