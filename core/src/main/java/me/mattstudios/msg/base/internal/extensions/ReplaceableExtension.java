package me.mattstudios.msg.base.internal.extensions;

import jdk.internal.joptsimple.internal.Strings;
import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.base.internal.extensions.delimiter.KeywordDelimiterProcessor;
import me.mattstudios.msg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ReplaceableExtension implements ReplaceableHandler {

    private final char openingCharacter;
    private final char closingCharacter;
    private final int amount;

    public ReplaceableExtension(final char openingCharacter, final char closingCharacter, final int amount) {
        this.openingCharacter = openingCharacter;
        this.closingCharacter = closingCharacter;
        this.amount = amount;
    }

    @Override
    public void extend(final Parser.Builder builder) {
        builder.customDelimiterProcessor(new KeywordDelimiterProcessor(openingCharacter, closingCharacter, amount));
    }

    @Nullable
    @Override
    public abstract MessageNode getNode(@NotNull String literal);

    @NotNull
    @Override
    public String getOpener() {
        return Strings.repeat(openingCharacter, amount);
    }

    @NotNull
    @Override
    public String getCloser() {
        return Strings.repeat(closingCharacter, amount);
    }

}
