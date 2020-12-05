package me.mattstudios.msg.commonmark.parser;

/**
 * Whether to include {@link me.mattstudios.msg.commonmark.node.SourceSpan} or not while parsing,
 * see {@link Parser.Builder#includeSourceSpans(IncludeSourceSpans)}.
 */
public enum IncludeSourceSpans {
    /**
     * Do not include source spans.
     */
    NONE,
    /**
     * Include source spans on {@link me.mattstudios.msg.commonmark.node.Block} nodes.
     */
    BLOCKS,
}
