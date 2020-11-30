package me.mattstudios.msg.commonmark.internal.inline;

public interface InlineContentParser {

    ParsedInline tryParse(InlineParserState inlineParserState);
}
