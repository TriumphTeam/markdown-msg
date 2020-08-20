package me.mattstudios.mfmsg.base.internal;

import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.extension.node.Obfuscated;
import me.mattstudios.mfmsg.base.internal.extension.node.Underline;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.node.*;

import java.util.Set;

public final class MarkdownVisitor extends AbstractVisitor {

    private final Set<Format> formats;

    // Text properties
    private boolean italic;
    private boolean bold;
    private boolean strike;
    private boolean underline;
    private boolean obfuscated;

    private Appender appender;

    public MarkdownVisitor(final Set<Format> formats) {
        this.formats = formats;
    }

    public void visitComponents(final Node node, final Appender appender) {
        if (this.appender != appender) this.appender = appender;
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
            if (!formats.contains(Format.UNDERLINE)) {
                visitChildren(customNode);
                return;
            }
            underline = true;
            visitChildren(customNode);
            underline = false;
            return;
        }

        if (customNode instanceof Obfuscated) {
            if (!formats.contains(Format.OBFUSCATED)) {
                visitChildren(customNode);
                return;
            }
            obfuscated = true;
            visitChildren(customNode);
            obfuscated = false;
            return;
        }

        if (!(customNode instanceof Strikethrough)) return;
        if (!formats.contains(Format.STRIKETHROUGH)) {
            visitChildren(customNode);
            return;
        }
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