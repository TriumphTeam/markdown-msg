package me.mattstudios.msg.commonmark.internal.inline.triumph;

import me.mattstudios.msg.commonmark.internal.inline.InlineContentParser;
import me.mattstudios.msg.commonmark.internal.inline.InlineParserState;
import me.mattstudios.msg.commonmark.internal.inline.ParsedInline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CustomInlineParser implements InlineContentParser {

    @NotNull
    private final TriggerProcessor triggerProcessor;

    public CustomInlineParser(@NotNull final TriggerProcessor triggerProcessor) {
        this.triggerProcessor = triggerProcessor;
    }

    @Nullable
    @Override
    public ParsedInline tryParse(final InlineParserState inlineParserState) {
        return triggerProcessor.parse(inlineParserState.scanner());
    }

}
