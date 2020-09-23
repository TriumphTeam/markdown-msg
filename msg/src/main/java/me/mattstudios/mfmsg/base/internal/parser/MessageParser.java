package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.MarkdownRenderer;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.extensions.ObfuscatedExtension;
import me.mattstudios.mfmsg.base.internal.extensions.ReplaceableHandler;
import me.mattstudios.mfmsg.base.internal.extensions.StrikethroughExtension;
import me.mattstudios.mfmsg.base.internal.extensions.UnderlineExtension;
import me.mattstudios.mfmsg.base.internal.util.Version;
import me.mattstudios.mfmsg.commonmark.Extension;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final MarkdownRenderer visitor;

    public MessageParser(@NotNull final MessageOptions messageOptions, @NotNull final Version version) {
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
        visitor = new MarkdownRenderer(nodes, messageOptions, version);
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
