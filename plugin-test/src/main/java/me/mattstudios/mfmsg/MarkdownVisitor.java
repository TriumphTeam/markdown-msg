package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.commonmark.node.AbstractVisitor;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Text;
import me.mattstudios.mfmsg.commonmark.node.mf.Action;
import me.mattstudios.mfmsg.commonmark.node.mf.Color;
import me.mattstudios.mfmsg.commonmark.node.mf.Gradient;
import me.mattstudios.mfmsg.commonmark.node.mf.LineBreak;
import me.mattstudios.mfmsg.commonmark.node.mf.Rainbow;
import me.mattstudios.mfmsg.commonmark.node.mf.Reset;

public final class MarkdownVisitor extends AbstractVisitor {

    /**
     * Visits the node's components
     *
     * @param node The node to visit
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

    @Override
    public void visit(final Rainbow rainbow) {
        System.out.println("sat: " + rainbow.getSaturation() + " bri: " + rainbow.getBrightness());
        visitChildren(rainbow);
    }

    @Override
    public void visit(final Gradient gradient) {
        System.out.println(gradient.getHexes());
        visitChildren(gradient);
    }

    @Override
    public void visit(final Action action) {
        System.out.println(action.getActions());
        visitChildren(action);
    }

    @Override
    public void visit(final Text text) {
        System.out.println(text.getLiteral());
        visitChildren(text);
    }

    @Override
    public void visit(final LineBreak lineBreak) {
        System.out.println("break?");
        visitChildren(lineBreak);
    }

}