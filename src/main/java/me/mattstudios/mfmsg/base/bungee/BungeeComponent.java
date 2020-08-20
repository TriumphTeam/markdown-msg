package me.mattstudios.mfmsg.base.bungee;

import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.List;

public final class BungeeComponent implements MessageComponent {

    private final List<List<MessagePart>> parts;

    public BungeeComponent(final List<List<MessagePart>> parts) {
        this.parts = parts;
    }

    @Override
    public void send(final Player player) {

    }

    @Override
    public BaseComponent[] asBaseComponent() {
        return BungeeConverter.convert(parts);
    }

}
