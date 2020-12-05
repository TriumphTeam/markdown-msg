package me.mattstudios.msg.base;

import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.base.internal.parser.MarkdownParser;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class MarkdownMessage<T> {

    @NotNull
    private final MarkdownParser parser;

    public MarkdownMessage(@NotNull final MessageOptions messageOptions) {
        this.parser = new MarkdownParser(messageOptions);
    }

    @NotNull
    public abstract T parse(@NotNull final String message);

    @NotNull
    public abstract List<MessageNode> parseToNodes(@NotNull final String message);

    @NotNull
    protected MarkdownParser getParser() {
        return parser;
    }

}
