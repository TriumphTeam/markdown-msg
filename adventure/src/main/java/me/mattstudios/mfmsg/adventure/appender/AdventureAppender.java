package me.mattstudios.mfmsg.adventure.appender;

import com.google.gson.JsonObject;
import me.mattstudios.mfmsg.base.internal.action.ClickMessageAction;
import me.mattstudios.mfmsg.base.internal.action.HoverMessageAction;
import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.serializer.Appender;
import me.mattstudios.mfmsg.base.serializer.scanner.ScanUtils;
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
import java.util.Optional;

public final class AdventureAppender implements Appender {

    final TextComponent.Builder builder = TextComponent.builder("");

    @Override
    public void append(@NotNull final String value) {
        if (value.isEmpty()) return;
        builder.append(value);
    }

    @Override
    public void appendNode(final @NotNull String text, final @Nullable String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, final @Nullable List<MessageAction> actions) {
        final TextComponent.Builder textComponent = TextComponent.builder().content(text);

        if (bold) textComponent.decoration(TextDecoration.BOLD, true);
        if (italic) textComponent.decoration(TextDecoration.ITALIC, true);
        if (strike) textComponent.decoration(TextDecoration.STRIKETHROUGH, true);
        if (underline) textComponent.decoration(TextDecoration.UNDERLINED, true);
        if (obfuscated) textComponent.decoration(TextDecoration.OBFUSCATED, true);

        if (color != null) {
            if (color.startsWith("#")) {
                textComponent.color(TextColor.fromHexString(color));
            } else {
                final Optional<NamedTextColor> namedColor = NamedTextColor.values().stream().filter(namedTextColor -> namedTextColor.toString().equalsIgnoreCase(color)).findAny();
                namedColor.ifPresent(textComponent::color);
            }
        }

        if (actions == null || actions.isEmpty()) {
            builder.append(textComponent);
            return;
        }

        for (final MessageAction messageAction : actions) {
            if (messageAction instanceof HoverMessageAction) {
                final JsonObject hoverObject = new JsonObject();
                hoverObject.addProperty("action", "show_text");

                final List<MessageNode> nodes = ((HoverMessageAction) messageAction).getNodes();

                final AdventureAppender appender = new AdventureAppender();
                ScanUtils.scan(nodes, appender);
                final Component component = appender.buildComponent();

                textComponent.hoverEvent(HoverEvent.showText(component));
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

    @Override
    public String build() {
        return null;
    }

    public Component buildComponent() {
        return builder.build();
    }

}
