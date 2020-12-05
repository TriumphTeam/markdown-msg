package me.mattstudios.msg.commonmark.node;

import me.mattstudios.msg.commonmark.node.triumph.Action;
import me.mattstudios.msg.commonmark.node.triumph.Color;
import me.mattstudios.msg.commonmark.node.triumph.Gradient;
import me.mattstudios.msg.commonmark.node.triumph.LegacyBold;
import me.mattstudios.msg.commonmark.node.triumph.LegacyItalic;
import me.mattstudios.msg.commonmark.node.triumph.LegacyObfuscated;
import me.mattstudios.msg.commonmark.node.triumph.LegacyStrikethrough;
import me.mattstudios.msg.commonmark.node.triumph.LegacyUnderline;
import me.mattstudios.msg.commonmark.node.triumph.LineBreak;
import me.mattstudios.msg.commonmark.node.triumph.Rainbow;
import me.mattstudios.msg.commonmark.node.triumph.Reset;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract visitor that visits all children by default.
 * <p>
 * Can be used to only process certain nodes. If you override a method and want visiting to descend into children,
 * call {@link #visitChildren}.
 */
public abstract class AbstractVisitor implements Visitor {

    /* MF NODES START ERE */

    @Override
    public void visit(@NotNull final Color color) {
        visitChildren(color);
    }

    @Override
    public void visit(@NotNull final Rainbow rainbow) {
        visitChildren(rainbow);
    }

    @Override
    public void visit(@NotNull final Gradient gradient) {
        visitChildren(gradient);
    }

    @Override
    public void visit(@NotNull final Reset reset) {
        visitChildren(reset);
    }

    @Override
    public void visit(@NotNull final Action action) {
        visitChildren(action);
    }

    @Override
    public void visit(@NotNull final LineBreak lineBreak) {
        visitChildren(lineBreak);
    }

    // Legacy format

    @Override
    public void visit(@NotNull final LegacyBold legacyBold) {
        visitChildren(legacyBold);
    }

    @Override
    public void visit(@NotNull final LegacyItalic legacyItalic) {
        visitChildren(legacyItalic);
    }

    @Override
    public void visit(@NotNull final LegacyStrikethrough legacyStrikethrough) {
        visitChildren(legacyStrikethrough);
    }

    @Override
    public void visit(@NotNull final LegacyUnderline legacyUnderline) {
        visitChildren(legacyUnderline);
    }

    @Override
    public void visit(@NotNull final LegacyObfuscated legacyObfuscated) {
        visitChildren(legacyObfuscated);
    }


    /* MF NODES END HERE */

    @Override
    public void visit(@NotNull final Document document) {
        visitChildren(document);
    }

    @Override
    public void visit(@NotNull final Emphasis emphasis) {
        visitChildren(emphasis);
    }

    @Override
    public void visit(@NotNull final Paragraph paragraph) {
        visitChildren(paragraph);
    }

    @Override
    public void visit(@NotNull final StrongEmphasis strongEmphasis) {
        visitChildren(strongEmphasis);
    }

    @Override
    public void visit(@NotNull final Text text) {
        visitChildren(text);
    }

    @Override
    public void visit(@NotNull final CustomBlock customBlock) {
        visitChildren(customBlock);
    }

    @Override
    public void visit(@NotNull final CustomNode customNode) {
        visitChildren(customNode);
    }

    /**
     * Visit the child nodes.
     *
     * @param parent the parent node whose children should be visited
     */
    protected void visitChildren(@NotNull final Node parent) {
        Node node = parent.getFirstChild();
        while (node != null) {
            // A subclass of this visitor might modify the node, resulting in getNext returning a different node or no
            // node after visiting it. So get the next node before visiting.
            final Node next = node.getNext();
            node.accept(this);
            node = next;
        }
    }
}
