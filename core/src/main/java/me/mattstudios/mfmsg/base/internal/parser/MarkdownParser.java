package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.extensions.ObfuscatedExtension;
import me.mattstudios.mfmsg.base.internal.extensions.ReplaceableHandler;
import me.mattstudios.mfmsg.base.internal.extensions.StrikethroughExtension;
import me.mattstudios.mfmsg.base.internal.extensions.UnderlineExtension;
import me.mattstudios.mfmsg.base.internal.renderer.MarkdownRenderer;
import me.mattstudios.mfmsg.commonmark.Extension;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main parser used
 */
public final class MarkdownParser {

    // The parser that'll be used with the strikethrough, underline, obfuscated extensions
    private final Parser parser;

    @NotNull
    private final List<MessageNode> nodes = new ArrayList<>();

    @NotNull
    private final MarkdownRenderer visitor;

    public MarkdownParser(@NotNull final MessageOptions messageOptions) {
        final List<Extension> extensions = new ArrayList<>(
                Arrays.asList(
                        StrikethroughExtension.create(),
                        UnderlineExtension.create(),
                        ObfuscatedExtension.create()
                )
        );

        final ReplaceableHandler replaceableHandler = messageOptions.getReplaceableHandler();

        if (replaceableHandler != null) {
            extensions.add(replaceableHandler);
        }

        parser = Parser.builder().extensions(extensions).build();
        visitor = new MarkdownRenderer(nodes, messageOptions);
    }

    public List<MessageNode> parse(@NotNull final String message) {
        visitor.visitComponents(parser.parse(message));
        final List<MessageNode> parsedNodes = new ArrayList<>(nodes);
        nodes.clear();
        return parsedNodes;
    }

}
