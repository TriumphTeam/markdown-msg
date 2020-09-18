package me.mattstudios.mfmsg.commonmark.node;

public abstract class CustomNode extends Node {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
