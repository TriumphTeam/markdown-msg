package me.mattstudios.mfmsg.base.internal.color;

import net.md_5.bungee.api.ChatColor;

public final class FlatColor implements MessageColor {

    private final ChatColor color;

    public FlatColor(final ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }

}
