package me.mattstudios.mfmsg.base.internal;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public interface MessageComponent {

    void send(final Player player);

    BaseComponent[] asBaseComponent();

}
