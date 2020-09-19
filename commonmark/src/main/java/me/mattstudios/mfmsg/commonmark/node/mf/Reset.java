package me.mattstudios.mfmsg.commonmark.node.mf;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Visitor;

public class Reset extends Node {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
