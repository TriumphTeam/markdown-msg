package me.mattstudios.msg.commonmark.node.triumph;

import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Gradient extends Node {

    @NotNull
    private final List<String> hexes;

    public Gradient(@NotNull final List<String> hexes) {
        this.hexes = hexes;
    }

    @Override
    public void accept(@NotNull final Visitor visitor) {
        visitor.visit(this);
    }

    @NotNull
    public List<String> getHexes() {
        return hexes;
    }

}
