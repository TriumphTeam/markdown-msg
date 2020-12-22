package me.mattstudios.msg.base.internal.parser;

import me.mattstudios.msg.base.MessageOptions;
import me.mattstudios.msg.base.internal.nodes.MessageNode;
import me.mattstudios.msg.base.internal.extensions.ObfuscatedExtension;
import me.mattstudios.msg.base.internal.extensions.StrikethroughExtension;
import me.mattstudios.msg.base.internal.extensions.UnderlineExtension;
import me.mattstudios.msg.base.internal.renderer.MarkdownRenderer;
import me.mattstudios.msg.base.internal.renderer.NodeRenderer;
import me.mattstudios.msg.commonmark.Extension;
import me.mattstudios.msg.commonmark.node.CustomNode;
import me.mattstudios.msg.commonmark.parser.Parser;
import me.mattstudios.msg.commonmark.parser.ParserExtension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main parser used
 */
public final class MarkdownParser {


    // The parser that'll be used with the strikethrough, underline, obfuscated extensions
    @NotNull
    private final Parser parser;

    @NotNull
    private final MessageOptions messageOptions;

    @NotNull
    private final List<MessageNode> nodes = new ArrayList<>();

    @NotNull
    private final Map<Class<? extends CustomNode>, NodeRenderer> renderers = new HashMap<>();

    public MarkdownParser(@NotNull final MessageOptions messageOptions) {
        this.messageOptions = messageOptions;

        final List<Extension> extensions = new ArrayList<>(
                Arrays.asList(
                        StrikethroughExtension.create(),
                        UnderlineExtension.create(),
                        ObfuscatedExtension.create()
                )
        );

        final List<ParserExtension> parserExtensions = messageOptions.getExtensions();

        for (final ParserExtension extension : parserExtensions) {
            if (!(extension instanceof NodeRenderer)) continue;
            final NodeRenderer renderer = (NodeRenderer) extension;
            extensions.add(extension);
            renderers.put(renderer.getParserNode(), renderer);
        }

        parser = Parser.builder().extensions(extensions).build();
    }

    public List<MessageNode> parse(@NotNull final String message) {
        final MarkdownRenderer visitor = new MarkdownRenderer(nodes, messageOptions, renderers);
        visitor.visitComponents(parser.parse(message));
        final List<MessageNode> parsedNodes = new ArrayList<>(nodes);
        nodes.clear();
        return parsedNodes;
    }

}
