package me.mattstudios.mfmsg.base.internal.action.content;

import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface HoverContent {

    /**
     *
     * @param nodes
     * @return
     */
    static HoverContent showText(@NotNull final List<MessageNode> nodes) {
        return new ShowText(nodes);
    }

    static HoverContent showText(@NotNull final String text) {
        return new ShowText(text);
    }

    static HoverContent showItem(@NotNull final String id) {
        return new ShowItem(id);
    }

    static HoverContent showItem(@NotNull final String id, final int amount) {
        return new ShowItem(id, amount);
    }

    static HoverContent showItem(@NotNull final String id, final int amount, @Nullable final String nbt) {
        return new ShowItem(id, amount, nbt);
    }

}
