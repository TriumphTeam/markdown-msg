package me.mattstudios.mfmsg.base.internal;

import org.bukkit.entity.Player;

public interface MessageComponent {

    void send(final Player player);

    String asString();

}
