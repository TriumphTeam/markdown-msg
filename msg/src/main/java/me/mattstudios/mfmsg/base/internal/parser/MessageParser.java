package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Main parser used
 */
public final class MessageParser {

    // The parser that'll be used with the strikethrough, underline, obfuscated extensions
    private final Parser parser;

    @NotNull
    private final List<MessageNode> nodes = new ArrayList<>();

    @NotNull
    private final MarkdownVisitor visitor;

    public MessageParser(@NotNull final MessageOptions messageOptions) {
        parser = Parser.builder().extensions(messageOptions.getExtensions()).build();
        visitor = new MarkdownVisitor(nodes, messageOptions);
    }

    public void parse(@NotNull final String message) {
        visitor.visitComponents(parser.parse(message));
    }

    /**
     * Builds the parsed information into the list of parts
     *
     * @return The list of all the parsed parts
     */
    public List<MessageNode> build() {
        return nodes;
    }

}
