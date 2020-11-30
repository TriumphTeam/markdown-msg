package me.mattstudios.msg.base.internal.components;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ReplaceableNode implements MessageNode {

    private final List<TextNode> nodes = new ArrayList<>();

    public void addNode(@NotNull final TextNode node) {
        nodes.add(node);
    }

    public List<TextNode> getNodes() {
        return nodes;
    }

}
