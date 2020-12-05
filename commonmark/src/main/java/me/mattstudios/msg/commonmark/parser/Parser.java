package me.mattstudios.msg.commonmark.parser;

import me.mattstudios.msg.commonmark.Extension;
import me.mattstudios.msg.commonmark.internal.DocumentParser;
import me.mattstudios.msg.commonmark.internal.InlineParserContextImpl;
import me.mattstudios.msg.commonmark.internal.InlineParserImpl;
import me.mattstudios.msg.commonmark.internal.inline.triumph.TriggerProcessor;
import me.mattstudios.msg.commonmark.node.Block;
import me.mattstudios.msg.commonmark.node.FencedCodeBlock;
import me.mattstudios.msg.commonmark.node.HtmlBlock;
import me.mattstudios.msg.commonmark.node.IndentedCodeBlock;
import me.mattstudios.msg.commonmark.node.LinkReferenceDefinition;
import me.mattstudios.msg.commonmark.node.ListBlock;
import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.ThematicBreak;
import me.mattstudios.msg.commonmark.parser.block.BlockParserFactory;
import me.mattstudios.msg.commonmark.parser.delimiter.DelimiterProcessor;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * Parses input text to a tree of nodes.
 * <p>
 * Start with the {@link #builder} method, configure the parser and build it. Example:
 * <pre><code>
 * Parser parser = Parser.builder().build();
 * Node document = parser.parse("input text");
 * </code></pre>
 */
public class Parser {

    private final List<BlockParserFactory> blockParserFactories;
    private final List<DelimiterProcessor> delimiterProcessors;
    private final List<TriggerProcessor> triggerProcessors;
    private final InlineParserFactory inlineParserFactory;
    private final List<PostProcessor> postProcessors;
    private final IncludeSourceSpans includeSourceSpans;

    private Parser(Builder builder) {
        this.blockParserFactories = DocumentParser.calculateBlockParserFactories(builder.blockParserFactories, builder.enabledBlockTypes);
        this.inlineParserFactory = builder.getInlineParserFactory();
        this.postProcessors = builder.postProcessors;
        this.delimiterProcessors = builder.delimiterProcessors;
        this.triggerProcessors = builder.triggerProcessors;
        this.includeSourceSpans = builder.includeSourceSpans;

        // Try to construct an inline parser. Invalid configuration might result in an exception, which we want to
        // detect as soon as possible.
        this.inlineParserFactory.create(
                new InlineParserContextImpl(
                        delimiterProcessors,
                        triggerProcessors,
                        Collections.emptyMap()
                )
        );
    }

    /**
     * Create a new builder for configuring a {@link Parser}.
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Parse the specified input text into a tree of nodes.
     * <p>
     * This method is thread-safe (a new parser state is used for each invocation).
     *
     * @param input the text to parse - must not be null
     * @return the root node
     */
    public Node parse(String input) {
        if (input == null) {
            throw new NullPointerException("input must not be null");
        }
        DocumentParser documentParser = createDocumentParser();
        Node document = documentParser.parse(input);
        return postProcess(document);
    }

    private DocumentParser createDocumentParser() {
        return new DocumentParser(blockParserFactories, inlineParserFactory, delimiterProcessors, triggerProcessors, includeSourceSpans);
    }

    private Node postProcess(Node document) {
        for (PostProcessor postProcessor : postProcessors) {
            document = postProcessor.process(document);
        }
        return document;
    }

    /**
     * Builder for configuring a {@link Parser}.
     */
    public static class Builder {
        private final List<BlockParserFactory> blockParserFactories = new ArrayList<>();
        private final List<DelimiterProcessor> delimiterProcessors = new ArrayList<>();
        private final List<TriggerProcessor> triggerProcessors = new ArrayList<>();
        private final List<PostProcessor> postProcessors = new ArrayList<>();
        private final Set<Class<? extends Block>> enabledBlockTypes = DocumentParser.getDefaultBlockParserTypes();
        private InlineParserFactory inlineParserFactory;
        private IncludeSourceSpans includeSourceSpans = IncludeSourceSpans.NONE;

        /**
         * @return the configured {@link Parser}
         */
        public Parser build() {
            return new Parser(this);
        }

        /**
         * @param extensions extensions to use on this parser
         * @return {@code this}
         */
        public Builder extensions(Iterable<? extends Extension> extensions) {
            if (extensions == null) {
                throw new NullPointerException("extensions must not be null");
            }
            for (Extension extension : extensions) {
                if (extension instanceof ParserExtension) {
                    ParserExtension parserExtension = (ParserExtension) extension;
                    parserExtension.extend(this);
                }
            }
            return this;
        }

        /**
         * Whether to calculate {@link me.mattstudios.msg.commonmark.node.SourceSpan} for {@link Node}.
         * <p>
         * By default, source spans are disabled.
         *
         * @param includeSourceSpans which kind of source spans should be included
         * @return {@code this}
         */
        public Builder includeSourceSpans(IncludeSourceSpans includeSourceSpans) {
            this.includeSourceSpans = includeSourceSpans;
            return this;
        }

        /**
         * Adds a custom delimiter processor.
         * <p>
         * Note that multiple delimiter processors with the same characters can be added, as long as they have a
         * different minimum length. In that case, the processor with the shortest matching length is used. Adding more
         * than one delimiter processor with the same character and minimum length is invalid.
         *
         * @param delimiterProcessor a delimiter processor implementation
         * @return {@code this}
         */
        public Builder customDelimiterProcessor(@NotNull final DelimiterProcessor delimiterProcessor) {
            delimiterProcessors.add(delimiterProcessor);
            return this;
        }

        public Builder customTriggerProcessor(@NotNull final TriggerProcessor triggerProcessor) {
            triggerProcessors.add(triggerProcessor);
            return this;
        }

        private InlineParserFactory getInlineParserFactory() {
            if (inlineParserFactory != null) {
                return inlineParserFactory;
            }
            return InlineParserImpl::new;
        }
    }

}
