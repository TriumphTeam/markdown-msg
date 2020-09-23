package me.mattstudios.mfmsg

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mfmsg.bukkit.Message
import me.mattstudios.mfmsg.base.MessageOptions
import me.mattstudios.mfmsg.base.internal.Format
import org.bukkit.entity.Player
import java.util.EnumSet
import kotlin.system.measureNanoTime

@Command("parse")
internal class Cmd : CommandBase() {

    @Default
    fun parse(player: Player, args: Array<String>) {
        val options = MessageOptions.Builder(EnumSet.allOf(Format::class.java))
        //options.setReplaceableHandler(TestReplaceable())

        val component = Message.create(options.build()).parse(args.joinToString(" "))

        player.sendMessage("${
            measureNanoTime {
                player.sendMessage(component.toString())
            } / 1000000.0
        }ms")

        val jsonElement: JsonElement = JsonParser().parse(component.toJson())
        //val json = gson.toJson(jsonElement)

        //println(json)
    }

}