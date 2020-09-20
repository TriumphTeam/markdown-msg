package me.mattstudios.mfmsg.base.serializer;

import me.mattstudios.mfmsg.base.internal.component.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NodeScanner {

    @NotNull
    private final List<MessageNode> nodes;
    // The index within the line. If index == length(), we pretend that there's a `\n` and only advance after we yield
    // that.
    private int index;

    // Current line or "" if at the end of the lines (using "" instead of null saves a null check)
    private MessageNode node = null;

    NodeScanner(@NotNull final List<MessageNode> nodes) {
        this.nodes = nodes;
        this.index = -1;
    }

    public MessageNode peek() {
        return node;
    }

    public boolean hasNext() {
        return index < nodes.size() - 1;
    }

    public void next() {
        index++;
        setNode(nodes.get(index));
    }

    public void previous() {
        if (index > 0) index--;
        setNode(nodes.get(index));
    }

    private void setNode(@NotNull MessageNode node) {
        this.node = node;
    }

}
