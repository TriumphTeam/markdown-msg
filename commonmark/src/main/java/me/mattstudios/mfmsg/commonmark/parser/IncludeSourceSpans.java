package me.mattstudios.mfmsg.commonmark.parser;

/**
 * Whether to include {@link me.mattstudios.mfmsg.commonmark.node.SourceSpan} or not while parsing,
 * see {@link Parser.Builder#includeSourceSpans(IncludeSourceSpans)}.
 */
public enum IncludeSourceSpans {
    /**
     * Do not include source spans.
     */
    NONE,
    /**
     * Include source spans on {@link me.mattstudios.mfmsg.commonmark.node.Block} nodes.
     */
    BLOCKS,
}
