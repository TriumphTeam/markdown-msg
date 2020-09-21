package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.extensions.ReplaceableExtension;
import me.mattstudios.mfmsg.base.internal.extensions.ObfuscatedExtension;
import me.mattstudios.mfmsg.base.internal.extensions.StrikethroughExtension;
import me.mattstudios.mfmsg.base.internal.extensions.UnderlineExtension;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Main parser used
 */
public final class MessageParser {

    // The parser that'll be used with the strikethrough, underline, obfuscated extensions
    private final Parser PARSER = Parser.builder()
            .extensions(
                    Arrays.asList(
                            StrikethroughExtension.create(),
                            UnderlineExtension.create(),
                            ObfuscatedExtension.create(),
                            ReplaceableExtension.create('{', '}', 1)
                    )
            ).build();

    @NotNull
    private final Set<Format> formats;
    @NotNull
    private final MessageColor defaultColor;

    @NotNull
    private final List<MessageNode> nodes = new ArrayList<>();
    
    @NotNull
    private final MarkdownVisitor visitor;

    /**
     * Main constructor that takes message formats and default color
     *
     * @param formats      The formats to check for
     * @param defaultColor The default color
     */
    public MessageParser(@NotNull Set<Format> formats, @NotNull final MessageColor defaultColor) {
        this.formats = formats;
        this.defaultColor = defaultColor;

        visitor = new MarkdownVisitor(formats);
    }

    public void parse(@NotNull final String message) {
        visit(PARSER.parse(message));
    }

    /**
     * Visits the given node
     *
     * @param node The node to visit
     */
    private void visit(final Node node) {
        visitor.visitComponents(node, nodes);
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
