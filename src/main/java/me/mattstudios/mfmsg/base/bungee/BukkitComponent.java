package me.mattstudios.mfmsg.base.bungee;

import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.nms.NmsMessage;
import me.mattstudios.mfmsg.base.serializer.JsonSerializer;
import org.bukkit.entity.Player;

import java.util.List;

public final class BukkitComponent implements MessageComponent {

    private final List<MessageLine> lines;
    private final String jsonMessage;

    public BukkitComponent(final List<MessageLine> lines) {
        this.lines = lines;
        jsonMessage = JsonSerializer.toString(lines);
    }

    @Override
    public void send(final Player player) {
        NmsMessage.sendMessage(player, jsonMessage);
    }

}
