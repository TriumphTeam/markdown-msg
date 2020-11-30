package me.mattstudios.msg.base.internal.action.content;

import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.base.internal.components.TextNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class ShowText implements HoverContent {

    @NotNull
    private final List<MessageNode> nodes;

    public ShowText(@NotNull final List<MessageNode> nodes) {
        this.nodes = nodes;
    }

    public ShowText(@NotNull final String text) {
        this.nodes = Collections.singletonList(new TextNode(text));
    }

    public List<MessageNode> getNodes() {
        return nodes;
    }

}

