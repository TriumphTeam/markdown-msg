package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.base.Message;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class Command implements CommandExecutor, TabCompleter {

    private final Message message = Message.create();

    @Override
    public boolean onCommand(@NotNull final CommandSender commandSender, final org.bukkit.command.@NotNull Command command, @NotNull final String s, final @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("parse")) return true;
        final Player player = (Player) commandSender;

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, final org.bukkit.command.@NotNull Command command, @NotNull final String s, final @NotNull String[] args) {
        final Player player = (Player) commandSender;
        if (!command.getName().equalsIgnoreCase("parse")) return Collections.emptyList();

        final MessageComponent messageComponent = message.parse(String.join(" ", args));
        messageComponent.sendActionBar(player, 10, 20, 10);

        return Collections.emptyList();
    }

}
