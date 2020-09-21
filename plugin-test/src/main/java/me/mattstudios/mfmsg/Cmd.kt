package me.mattstudios.mfmsg

import com.google.gson.JsonElement
import com.google.gson.JsonParser
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
        val component = Message.create().parse(args.joinToString(" "))
        player.sendMessage("${
            measureNanoTime {
                component.sendMessage(player)
            } / 1000000.0
        }ms")

        val jsonElement: JsonElement = JsonParser().parse(component.toJson())
        //val json = gson.toJson(jsonElement)

        //println(json)
    }

}