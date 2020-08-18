package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class BungeeComponent implements MessageComponent {

    private final List<MessagePart> parts;

    public BungeeComponent(final List<MessagePart> parts) {
        this.parts = parts;
    }

    @Override
    public void send(final Player player) {

    }

    @Override
    public BaseComponent[] asBaseComponent() {
        final ComponentBuilder builder = new ComponentBuilder();
        for (final MessagePart part : parts) {
            builder.append(new TextComponent(part.getText()), ComponentBuilder.FormatRetention.NONE);

            builder.bold(part.isBold());
            builder.italic(part.isItalic());
            builder.strikethrough(part.isStrike());
            builder.underlined(part.isUnderline());
            builder.obfuscated(part.isObfuscated());

            final MessageColor color = part.getColor();
            if (color == null) continue;

            if (color instanceof Gradient) {

                continue;
            }


            builder.color((((FlatColor) part.getColor()).getColor()));
        }

        return builder.create();
    }

    @Override
    public String toString() {
        return parts.stream().map(MessagePart::test).collect(Collectors.joining("\n"));
    }
}
