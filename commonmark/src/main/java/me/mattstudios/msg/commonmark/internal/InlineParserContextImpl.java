package me.mattstudios.msg.commonmark.internal;

import me.mattstudios.msg.commonmark.internal.inline.triumph.TriggerProcessor;
import me.mattstudios.msg.commonmark.node.LinkReferenceDefinition;
import me.mattstudios.msg.commonmark.parser.InlineParserContext;
import me.mattstudios.msg.commonmark.parser.delimiter.DelimiterProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class InlineParserContextImpl implements InlineParserContext {

    @NotNull
    private final List<DelimiterProcessor> delimiterProcessors;

    @NotNull
    private final List<TriggerProcessor> triggerProcessors;

    private final Map<String, LinkReferenceDefinition> linkReferenceDefinitions;

    public InlineParserContextImpl(
            @NotNull final List<DelimiterProcessor> delimiterProcessors,
            @NotNull final List<TriggerProcessor> triggerProcessors,
            @NotNull final Map<String, LinkReferenceDefinition> linkReferenceDefinitions
    ) {
        this.delimiterProcessors = delimiterProcessors;
        this.triggerProcessors = triggerProcessors;
        this.linkReferenceDefinitions = linkReferenceDefinitions;
    }

    @NotNull
    @Override
    public List<DelimiterProcessor> getCustomDelimiterProcessors() {
        return delimiterProcessors;
    }

    @NotNull
    @Override
    public List<TriggerProcessor> getCustomTriggerProcessors() {
        return triggerProcessors;
    }

    @Override
    public LinkReferenceDefinition getLinkReferenceDefinition(String label) {
        return linkReferenceDefinitions.get(label);
    }
}
