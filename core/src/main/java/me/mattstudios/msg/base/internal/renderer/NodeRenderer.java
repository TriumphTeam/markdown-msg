package me.mattstudios.msg.base.internal.renderer;

import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.commonmark.node.CustomNode;
import org.jetbrains.annotations.NotNull;

public interface NodeRenderer {

    @NotNull
    Class<? extends CustomNode> getParserNode();

    @NotNull
    MessageNode render(@NotNull final CustomNode node);

}
