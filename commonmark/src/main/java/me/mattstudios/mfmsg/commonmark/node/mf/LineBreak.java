package me.mattstudios.mfmsg.commonmark.node.mf;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Visitor;

// TODO THIS SHIT
public final class LineBreak extends Node {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}