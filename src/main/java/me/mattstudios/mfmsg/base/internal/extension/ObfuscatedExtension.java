package me.mattstudios.mfmsg.base.internal.extension;

import me.mattstudios.mfmsg.base.internal.extension.delimiter.ObfuscatedDelimiterProcessor;
import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

public final class ObfuscatedExtension implements Parser.ParserExtension {

    private ObfuscatedExtension() {}

    @NotNull
    public static Extension create() {
        return new ObfuscatedExtension();
    }

    @Override
    public void extend(final Parser.Builder builder) {
        builder.customDelimiterProcessor(new ObfuscatedDelimiterProcessor());
    }

}
