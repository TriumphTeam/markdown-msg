package me.mattstudios.mfmsg.base.bukkit;

import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.bukkit.nms.NmsMessage;
import me.mattstudios.mfmsg.base.serializer.JsonSerializer;
import me.mattstudios.mfmsg.base.serializer.StringSerializer;
import org.bukkit.entity.Player;

import java.util.List;

public final class BukkitComponent implements MessageComponent {

    private final List<MessageLine> lines;

    public BukkitComponent(final List<MessageLine> lines) {
        this.lines = lines;
    }

    @Override
    public void send(final Player player) {
        NmsMessage.sendMessage(player, JsonSerializer.toString(lines));
    }

    @Override
    public String asString() {
        return StringSerializer.toString(lines);
    }

}
