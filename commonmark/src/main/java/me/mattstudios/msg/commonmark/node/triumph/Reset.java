package me.mattstudios.msg.commonmark.node.triumph;

import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Visitor;

public class Reset extends Node {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
