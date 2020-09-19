package me.mattstudios.mfmsg

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mfmsg.base.Message
import org.bukkit.entity.Player
import kotlin.system.measureNanoTime

@Command("parse")
internal class Cmd : CommandBase() {

    @Default
    fun parse(player: Player, args: Array<String>) {
        player.sendMessage("${
            measureNanoTime {
                Message.create().parse(args.joinToString(" ")).sendMessage(player)
            } / 1000000.0
        }ms")
    }

}