package me.mattstudios.mfmsg.base.internal.extensions;

import me.mattstudios.mfmsg.base.internal.extensions.delimiter.UnderlineDelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.Extension;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
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
