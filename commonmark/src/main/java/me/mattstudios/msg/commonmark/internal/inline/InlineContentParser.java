package me.mattstudios.msg.commonmark.internal.inline;

import org.jetbrains.annotations.Nullable;

public interface InlineContentParser {

    @Nullable
    ParsedInline tryParse(InlineParserState inlineParserState);

}
