package me.mattstudios.mfmsg.base.bungee;

import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.action.ClickAction;
import me.mattstudios.mfmsg.base.internal.action.HoverAction;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.handler.GradientHandler;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BungeeConverter {

    public static BaseComponent[] convert(final List<List<MessagePart>> parts) {
        final ComponentBuilder builder = new ComponentBuilder();

        final Iterator<List<MessagePart>> iterator = parts.iterator();
        while (iterator.hasNext()) {
            builder.append(convertLine(iterator.next()));
            if (iterator.hasNext()) {
                builder.append("\n", ComponentBuilder.FormatRetention.NONE);
            }
        }

        return builder.create();
    }

    private static BaseComponent[] convertLine(final List<MessagePart> parts) {
        final ComponentBuilder builder = new ComponentBuilder();
        for (int i = 0; i < parts.size(); i++) {
            final MessagePart part = parts.get(i);

            final MessageColor color = part.getColor();

            if (color instanceof Gradient) {
                final List<MessagePart> gradientParts = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessagePart newPart = parts.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    gradientParts.add(newPart);
                    i++;
                }

                GradientHandler.appendGradient(gradientParts, gradient, builder);
                continue;
            }

            builder.append(new TextComponent(part.getText()), ComponentBuilder.FormatRetention.NONE);

            builder.bold(part.isBold());
            builder.italic(part.isItalic());
            builder.strikethrough(part.isStrike());
            builder.underlined(part.isUnderline());
            builder.obfuscated(part.isObfuscated());

            if (color != null) {
                builder.color((((FlatColor) part.getColor()).getColor()));
            }

            appendAction(builder, part.getActions());
        }

        return builder.create();
    }

    public static void appendAction(final ComponentBuilder builder, final List<Action> actions) {
        if (actions.isEmpty()) return;

        for (final Action action : actions) {
            if (action instanceof HoverAction) {
                builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, convert(((HoverAction) action).getParts())));
                continue;
            }

            final ClickAction clickAction = (ClickAction) action;

            switch (clickAction.getActionType()) {
                case ACTION_COMMAND:
                    builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickAction.getAction()));
                    continue;

                case ACTION_SUGGEST:
                    builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickAction.getAction()));
                    continue;

                case ACTION_URL:
                    builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, clickAction.getAction()));
                    continue;

                case ACTION_CLIPBOARD:
                    builder.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickAction.getAction()));
            }

        }
    }

}
