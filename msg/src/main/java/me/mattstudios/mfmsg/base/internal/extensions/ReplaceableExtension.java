package me.mattstudios.mfmsg.base.internal.extensions;

import me.mattstudios.mfmsg.base.internal.extensions.delimiter.KeywordDelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.Extension;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

public final class ReplaceableExtension implements Parser.ParserExtension {

    private final char openingCharacter;
    private final char closingCharacter;
    private final int amount;

    private ReplaceableExtension(final char openingCharacter, final char closingCharacter, final int amount) {
        this.openingCharacter = openingCharacter;
        this.closingCharacter = closingCharacter;
        this.amount = amount;
    }

    @NotNull
    public static Extension create(final char openingCharacter, final char closingCharacter, final int amount) {
        return new ReplaceableExtension(openingCharacter, closingCharacter, amount);
    }

    @Override
    public void extend(final Parser.Builder builder) {
        builder.customDelimiterProcessor(new KeywordDelimiterProcessor(openingCharacter, closingCharacter, amount));
    }

}
