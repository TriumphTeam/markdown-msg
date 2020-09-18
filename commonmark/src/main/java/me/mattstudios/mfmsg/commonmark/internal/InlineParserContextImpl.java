package me.mattstudios.mfmsg.commonmark.internal;

import me.mattstudios.mfmsg.commonmark.node.LinkReferenceDefinition;
import me.mattstudios.mfmsg.commonmark.parser.InlineParserContext;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterProcessor;

import java.util.List;
import java.util.Map;

public class InlineParserContextImpl implements InlineParserContext {

    private final List<DelimiterProcessor> delimiterProcessors;
    private final Map<String, LinkReferenceDefinition> linkReferenceDefinitions;

    public InlineParserContextImpl(List<DelimiterProcessor> delimiterProcessors,
                                   Map<String, LinkReferenceDefinition> linkReferenceDefinitions) {
        this.delimiterProcessors = delimiterProcessors;
        this.linkReferenceDefinitions = linkReferenceDefinitions;
    }

    @Override
    public List<DelimiterProcessor> getCustomDelimiterProcessors() {
        return delimiterProcessors;
    }

    @Override
    public LinkReferenceDefinition getLinkReferenceDefinition(String label) {
        return linkReferenceDefinitions.get(label);
    }
}
