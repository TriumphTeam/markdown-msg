package me.mattstudios.mfmsg.commonmark.node.mf;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

// TODO THIS SHIT
public final class LineBreak extends Node {

    @Override
    public void accept(@NotNull final Visitor visitor) {
        visitor.visit(this);
    }

}