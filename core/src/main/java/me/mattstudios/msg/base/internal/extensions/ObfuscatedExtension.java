package me.mattstudios.msg.base.internal.extensions;

import me.mattstudios.msg.base.internal.extensions.delimiter.ObfuscatedDelimiterProcessor;
import me.mattstudios.msg.commonmark.Extension;
import me.mattstudios.msg.commonmark.parser.Parser;
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
