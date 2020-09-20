package me.mattstudios.mfmsg.commonmark.node.mf;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Visitor;

public class Action extends Node {

    private String literal;

    public Action(String literal) {
        this.literal = literal;
    }

    @Override
    public void accept(Visitor visitor) {
        //visitor.visit(this);
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }
}
