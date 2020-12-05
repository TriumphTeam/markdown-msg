package me.mattstudios.msg.commonmark.node.triumph;

import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

public final class LineBreak extends Node {

    @Override
    public void accept(@NotNull final Visitor visitor) {
        visitor.visit(this);
    }

}