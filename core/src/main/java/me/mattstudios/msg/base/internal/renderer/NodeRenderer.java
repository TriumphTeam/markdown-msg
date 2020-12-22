package me.mattstudios.msg.base.internal.renderer;

import me.mattstudios.msg.base.internal.nodes.TextNode;
import me.mattstudios.msg.commonmark.node.CustomNode;
import org.jetbrains.annotations.NotNull;

public interface NodeRenderer {

    @NotNull
    Class<? extends CustomNode> getParserNode();

    @NotNull
    TextNode render(@NotNull final CustomNode node);

    @NotNull
    default FormatRetention retention() {
        return FormatRetention.IGNORE;
    }

}
