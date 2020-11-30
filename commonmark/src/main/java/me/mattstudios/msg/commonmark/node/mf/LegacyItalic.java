package me.mattstudios.msg.commonmark.node.mf;

import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

public class LegacyItalic extends Node {

    @Override
    public void accept(@NotNull final Visitor visitor) {
        visitor.visit(this);
    }

}
