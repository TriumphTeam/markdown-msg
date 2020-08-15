package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.component.StringComponentAppender;
import me.mattstudios.mfmsg.base.internal.util.HexUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class StringParser extends AbstractParser {

    private final StringBuilder finalBuilder = new StringBuilder();

    private final Appender<String> appender = new StringComponentAppender();
    private final MarkdownVisitor visitor;

    public StringParser(@NotNull final String message, @NotNull Set<Format> formats) {
        visitor = new MarkdownVisitor(appender, formats);
        parseMessage(message);
    }

    private void parseMessage(@NotNull final String message) {
        visitor.visitComponents(PARSER.parse(message));
        finalBuilder.append(appender.build()).append("§r");
    }

    public String build() {
        return HexUtils.colorify(finalBuilder.toString());
    }

}
