package me.mattstudios.msg.base.internal.nodes;

import me.mattstudios.msg.base.internal.action.MessageAction;
import me.mattstudios.msg.base.internal.color.MessageColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Stores all the information about each message node
 */
public interface MessageNode {

    @NotNull String getText();

    @NotNull MessageColor getColor();

    boolean isBold();

    boolean isItalic();

    boolean isStrike();

    boolean isUnderlined();

    boolean isObfuscated();

    @Nullable List<MessageAction> getActions();

}
