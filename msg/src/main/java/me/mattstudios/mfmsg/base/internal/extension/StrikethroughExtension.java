package me.mattstudios.mfmsg.base.internal.extension;

import me.mattstudios.mfmsg.base.internal.extension.delimiter.StrikethroughDelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.Extension;
import me.mattstudios.mfmsg.commonmark.node.CustomNode;
import me.mattstudios.mfmsg.commonmark.node.Delimited;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import me.mattstudios.mfmsg.commonmark.renderer.NodeRenderer;
import me.mattstudios.mfmsg.commonmark.renderer.html.HtmlNodeRendererContext;
import me.mattstudios.mfmsg.commonmark.renderer.html.HtmlNodeRendererFactory;
import me.mattstudios.mfmsg.commonmark.renderer.html.HtmlRenderer;
import me.mattstudios.mfmsg.commonmark.renderer.text.TextContentNodeRendererContext;
import me.mattstudios.mfmsg.commonmark.renderer.text.TextContentNodeRendererFactory;
import me.mattstudios.mfmsg.commonmark.renderer.text.TextContentRenderer;

public final class StrikethroughExtension implements Parser.ParserExtension {

    private StrikethroughExtension() {}

    public static Extension create() {
        return new StrikethroughExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new StrikethroughDelimiterProcessor());
    }

}
