package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.commonmark.node.AbstractVisitor;
import me.mattstudios.mfmsg.commonmark.node.mf.Color;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.mf.Reset;

public final class MarkdownVisitor extends AbstractVisitor {

    /**
     * Visits the node's components
     *
     * @param node     The node to visit
     */
    public void visitComponents(final Node node) {
        node.accept(this);
    }

    @Override
    public void visit(final Color color) {
        System.out.println(color.getLiteral());
        visitChildren(color);
    }

    @Override
    public void visit(final Reset reset) {
        System.out.println("reset");
        visitChildren(reset);
    }

}