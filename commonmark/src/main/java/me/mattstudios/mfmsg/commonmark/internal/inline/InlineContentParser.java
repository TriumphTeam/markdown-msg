package me.mattstudios.mfmsg.commonmark.internal.inline;

public interface InlineContentParser {

    ParsedInline tryParse(InlineParserState inlineParserState);
}
