package me.mattstudios.mfmsg.adventure.appender;

import me.mattstudios.mfmsg.base.internal.action.ClickMessageAction;
import me.mattstudios.mfmsg.base.internal.action.HoverMessageAction;
import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.action.content.HoverContent;
import me.mattstudios.mfmsg.base.internal.action.content.ShowItem;
import me.mattstudios.mfmsg.base.internal.action.content.ShowText;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.serializer.Appender;
import me.mattstudios.mfmsg.base.serializer.scanner.NodeScanner;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Appender to turn MF nodes into Kyori {@link Component}
 */
public final class AdventureAppender implements Appender<Component> {

    // Main builder
    final TextComponent.Builder builder = TextComponent.builder("");

    /**
     * Appends a text value to the builder
     *
     * @param value The text value
     */
    @Override
    public void append(@NotNull final String value) {
        if (value.isEmpty()) return;
        builder.append(value);
    }

    /**
     * Appends a node to the builder
     *
     * @param text       The node's text
     * @param color      The node's color
     * @param bold       If the node is bold
     * @param italic     If the node is italic
     * @param strike     If the node is strikethrough
     * @param underline  If the node is underlined
     * @param obfuscated If the node is obfuscated
     * @param actions    The node's actions
     */
    @Override
    public void appendNode(@NotNull final String text, @Nullable final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @Nullable final List<MessageAction> actions) {
        final TextComponent.Builder textComponent = TextComponent.builder().content(text);

        // Applies the node's properties (decoration)
        if (bold) textComponent.decoration(TextDecoration.BOLD, true);
        if (italic) textComponent.decoration(TextDecoration.ITALIC, true);
        if (strike) textComponent.decoration(TextDecoration.STRIKETHROUGH, true);
        if (underline) textComponent.decoration(TextDecoration.UNDERLINED, true);
        if (obfuscated) textComponent.decoration(TextDecoration.OBFUSCATED, true);

        // Sets the color
        if (color != null) {
            if (color.startsWith("#")) {
                textComponent.color(TextColor.fromHexString(color));
            } else {
                textComponent.color(NamedTextColor.NAMES.value(color));
            }
        }

        if (actions == null || actions.isEmpty()) {
            builder.append(textComponent);
            return;
        }

        // Handles the actions
        for (final MessageAction messageAction : actions) {
            if (messageAction instanceof HoverMessageAction) {
                final HoverContent hoverContent = ((HoverMessageAction) messageAction).getHoverContent();

                if (hoverContent instanceof ShowText) {
                    final List<MessageNode> nodes = ((ShowText) hoverContent).getNodes();

                    final AdventureAppender appender = new AdventureAppender();
                    NodeScanner.scan(nodes, appender);
                    final Component component = appender.build();

                    textComponent.hoverEvent(HoverEvent.showText(component));
                    continue;
                }

                final ShowItem showItem = (ShowItem) hoverContent;

                final Key key = Key.of(showItem.getId());

                BinaryTagHolder nbt = null;

                if (showItem.getNbt() != null) {
                    nbt = BinaryTagHolder.of(showItem.getNbt());
                }

                textComponent.hoverEvent(HoverEvent.showItem(new HoverEvent.ShowItem(key, showItem.getCount(), nbt)));
                continue;
            }

            final ClickMessageAction clickAction = (ClickMessageAction) messageAction;

            switch (clickAction.getActionType()) {
                case ACTION_COMMAND:
                    textComponent.clickEvent(ClickEvent.runCommand(clickAction.getAction()));
                    continue;

                case ACTION_SUGGEST:
                    textComponent.clickEvent(ClickEvent.suggestCommand(clickAction.getAction()));
                    continue;

                case ACTION_URL:
                    textComponent.clickEvent(ClickEvent.openUrl(clickAction.getAction()));
                    continue;

                case ACTION_CLIPBOARD:
                    textComponent.clickEvent(ClickEvent.copyToClipboard(clickAction.getAction()));
            }
        }

        builder.append(textComponent.build());
    }

    /**
     * Builds the current {@link Component}
     *
     * @return {@link Component} generated by the appender
     */
    @NotNull
    @Override
    public Component build() {
        return builder.build();
    }

}
