package me.mattstudios.mfmsg.base.internal;

import org.bukkit.entity.Player;

public interface MessageComponent {

    void sendMessage(final Player player);

    void sendTitle(final Player player, final int fadeIn, final int stay, final int fadeOut);

    void sendSubTitle(final Player player, final int fadeIn, final int stay, final int fadeOut);

    void sendActionBar(final Player player, final int fadeIn, final int stay, final int fadeOut);

    @Override
    String toString();

    String toJson();

}
