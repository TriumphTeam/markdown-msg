package me.mattstudios.mfmsg.base.bukkit;

import me.mattstudios.mfmsg.base.bukkit.nms.NmsMessage;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
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
    public void sendMessage(final Player player) {
        NmsMessage.sendMessage(player, JsonSerializer.toString(lines));
    }

    @Override
    public void sendTitle(final Player player, final int fadeIn, final int stay, final int fadeOut) {
        NmsMessage.sendTitle(player, JsonSerializer.toString(lines), "TITLE", fadeIn, stay, fadeOut);
    }

    @Override
    public void sendSubTitle(final Player player, final int fadeIn, final int stay, final int fadeOut) {
        NmsMessage.sendTitle(player, JsonSerializer.toString(lines), "SUBTITLE", fadeIn, stay, fadeOut);
    }

    @Override
    public void sendActionBar(final Player player, final int fadeIn, final int stay, final int fadeOut) {
        NmsMessage.sendTitle(player, JsonSerializer.toString(lines), "ACTIONBAR", fadeIn, stay, fadeOut);
    }

    @Override
    public String toString() {
        return StringSerializer.toString(lines);
    }

    @Override
    public String toJson() {
        return JsonSerializer.toString(lines);
    }
}
