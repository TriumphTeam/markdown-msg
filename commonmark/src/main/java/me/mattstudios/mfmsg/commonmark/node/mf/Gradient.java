package me.mattstudios.mfmsg.commonmark.node.mf;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Gradient extends Node {

    @NotNull
    private final List<String> hexes;

    public Gradient(@NotNull final List<String> hexes) {
        this.hexes = hexes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @NotNull
    public List<String> getHexes() {
        return hexes;
    }

}
