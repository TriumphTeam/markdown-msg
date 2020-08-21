package me.mattstudios.mfmsg.base.bungee;

import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.serializer.JsonSerializer;
import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public final class BukkitComponent implements MessageComponent {

    private final List<MessageLine> lines;

    public BukkitComponent(final List<MessageLine> lines) {
        this.lines = lines;
    }

    @Override
    public void send(final Player player) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(JsonSerializer.toString(lines)), ChatMessageType.CHAT, player.getUniqueId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
