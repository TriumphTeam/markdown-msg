package me.mattstudios.msg.base.internal.action;

import me.mattstudios.msg.base.internal.Format;
import me.mattstudios.msg.base.internal.action.content.HoverContent;
import me.mattstudios.msg.base.internal.nodes.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface in common for both click and hover actions
 */
public interface MessageAction {

    /**
     * Gets a MessageAction from the given action type and action
     *
     * @param actionType The action type
     * @param action     The action
     * @return A ClickMessageAction
     */
    static MessageAction from(@NotNull final Format actionType, @NotNull final String action) {
        return new ClickMessageAction(actionType, action);
    }

    /**
     * Gets a MessageAction from a list of Nodes
     *
     * @param nodes The list of nodes
     * @return A new HoverMessageAction
     */
    static MessageAction from(@NotNull final List<MessageNode> nodes) {
        return new HoverMessageAction(HoverContent.showText(nodes));
    }

}
