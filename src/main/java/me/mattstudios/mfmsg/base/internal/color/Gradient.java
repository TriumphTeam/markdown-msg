package me.mattstudios.mfmsg.base.internal.color;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public final class Gradient implements MessageColor {

    private final List<ChatColor> colors;

    public Gradient(final List<ChatColor> colors) {
        this.colors = colors;
    }

    public List<ChatColor> getColors() {
        return colors;
    }

    @Override
    public String test() {
        return "{Gradient: " + colors.stream().map(ChatColor::getName).collect(Collectors.joining(", ")) + "}";
    }
}
