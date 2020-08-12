package me.mattstudios.mfmsg.base.internal;

import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.extension.node.Obfuscated;
import me.mattstudios.mfmsg.base.internal.extension.node.Underline;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Emphasis;
import org.commonmark.node.Node;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;

import java.util.Set;

public final class MarkdownVisitor extends AbstractVisitor {

    // Text properties
    private boolean italic;
    private boolean bold;
    private boolean strike;
    private boolean underline;
    private boolean obfuscated;

    private final Appender<?> appender;
    private final Set<Format> formats;

    public MarkdownVisitor(final Appender<?> abstractParser, final Set<Format> formats) {
        this.appender = abstractParser;
        this.formats = formats;
    }

    /**
     * Parses the {@link Node}
     *
     * @param node The {@link Node} given by the {@link Parser}
     */
    public void parse(final Node node) {
        node.accept(this);
    }

    /**
     * Handles italic
     *
     * @param emphasis The italic is matched by either `* *` or `_ _`
     */
    @Override
    public void visit(final Emphasis emphasis) {
        if (!formats.contains(Format.ITALIC)) {
            visitChildren(emphasis);
            return;
        }

        italic = true;
        visitChildren(emphasis);
        italic = false;
    }

    /**
     * Handles bold
     *
     * @param strongEmphasis Bold is matched by `** **`
     */
    @Override
    public void visit(final StrongEmphasis strongEmphasis) {
        if (!formats.contains(Format.BOLD)) {
            visitChildren(strongEmphasis);
            return;
        }

        bold = true;
        visitChildren(strongEmphasis);
        bold = false;
    }

    /**
     * Handles extended nodes
     *
     * @param customNode The {@link CustomNode} can be either {@link Underline}, {@link Strikethrough}, or {@link Obfuscated}
     */
    @Override
    public void visit(final CustomNode customNode) {
        if (customNode instanceof Underline) {
            underline = true;
            visitChildren(customNode);
            underline = false;
            return;
        }

        if (customNode instanceof Obfuscated) {
            obfuscated = true;
            visitChildren(customNode);
            obfuscated = false;
            return;
        }

        if (!(customNode instanceof Strikethrough)) return;
        strike = true;
        visitChildren(customNode);
        strike = false;
    }

    /**
     * Handles the appending of the component
     *
     * @param text A parsed {@link Text} node
     */
    @Override
    public void visit(final Text text) {
        appender.append(text.getLiteral(), italic, bold, strike, underline, obfuscated);
        visitChildren(text);
    }

}