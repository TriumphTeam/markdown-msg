package me.mattstudios.mfmsg.commonmark.internal.inline;

import me.mattstudios.mfmsg.commonmark.node.Node;

public class ParsedInlineImpl extends ParsedInline {
    private final Node node;
    private final Position position;

    ParsedInlineImpl(Node node, Position position) {
        this.node = node;
        this.position = position;
    }

    public Node getNode() {
        return node;
    }

    public Position getPosition() {
        return position;
    }
}
