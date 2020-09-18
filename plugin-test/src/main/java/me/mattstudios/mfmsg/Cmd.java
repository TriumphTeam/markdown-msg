package me.mattstudios.mfmsg;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;

@Command("parse")
final class Cmd extends CommandBase {

    @Default
    public void parse(final Player player, final String[] args) {

        player.sendMessage("Hello");

    }

}
