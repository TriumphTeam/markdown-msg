package me.mattstudios.mfmsg

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("parse")
internal class Cmd : CommandBase() {

    @Default
    fun parse(player: Player, args: Array<String?>?) {
        player.sendMessage("Hello")
    }

}