package me.mattstudios.mfmsg

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mfmsg.adventure.AdventureMessage
import me.mattstudios.mfmsg.bukkit.Message
import me.mattstudios.mfmsg.base.MessageOptions
import me.mattstudios.mfmsg.base.internal.Format
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.KeybindComponent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import java.util.EnumSet
import kotlin.system.measureNanoTime

@Command("parse")
internal class Cmd(plugin: TestPlugin) : CommandBase() {

    private val audience = BukkitAudiences.create(plugin)

    @Default
    fun parse(player: Player, args: Array<String>) {
        val options = MessageOptions.Builder(EnumSet.allOf(Format::class.java))
        //options.setReplaceableHandler(TestReplaceable())

        player.sendMessage("MF - ${
            measureNanoTime {
                val component = Message.create(options.build()).parse(args.joinToString(" "))
                component.sendMessage(player)
            } / 1000000.0
        }ms")

        player.sendMessage("Adventure - ${
            measureNanoTime {
                val component = AdventureMessage.create(options.build()).parse(args.joinToString(" "))
                audience.audience(player).sendMessage(component)
            } / 1000000.0
        }ms")
    }

}