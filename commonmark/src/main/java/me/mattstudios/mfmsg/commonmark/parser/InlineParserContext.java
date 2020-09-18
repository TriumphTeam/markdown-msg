package me.mattstudios.mfmsg.commonmark.parser;

import me.mattstudios.mfmsg.commonmark.node.LinkReferenceDefinition;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterProcessor;

import java.util.List;

/**
 * Context for inline parsing.
 */
public interface InlineParserContext {

    /**
     * @return custom delimiter processors that have been configured with {@link Parser.Builder#customDelimiterProcessor(DelimiterProcessor)}
     */
    List<DelimiterProcessor> getCustomDelimiterProcessors();

    /**
     * Look up a {@link LinkReferenceDefinition} for a given label.
     *
     * @param label the link label to look up
     * @return the definition if one exists, {@code null} otherwise
     */
    LinkReferenceDefinition getLinkReferenceDefinition(String label);
}
