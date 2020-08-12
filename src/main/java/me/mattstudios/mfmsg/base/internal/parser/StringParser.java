package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.component.Builder;
import me.mattstudios.mfmsg.base.internal.component.StringComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class StringParser extends AbstractParser {

    private final StringBuilder finalBuilder = new StringBuilder();

    private final Builder<String> builder = new StringComponentBuilder();
    private final MarkdownVisitor visitor;

    public StringParser(@NotNull final String message, @NotNull Set<Format> formats) {
        visitor = new MarkdownVisitor(builder, formats);
        parseMessage(message);
    }

    private void parseMessage(@NotNull final String message) {
        visitor.parse(PARSER.parse(message));
        finalBuilder.append(builder.build()).append("§r");
    }

    public String build() {
        return finalBuilder.toString();
    }

}
