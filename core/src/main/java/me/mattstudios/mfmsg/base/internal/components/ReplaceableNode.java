package me.mattstudios.mfmsg.base.internal.components;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ReplaceableNode implements MessageNode {

    private final String literal;
    private final List<TextNode> nodes = new ArrayList<>();

    public ReplaceableNode(final String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public void addNode(@NotNull final TextNode node) {
        nodes.add(node);
    }

    public List<TextNode> getNodes() {
        return nodes;
    }

}
