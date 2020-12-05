package me.mattstudios.msg

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.msg.base.MessageOptions
import me.mattstudios.msg.base.internal.Format
import me.mattstudios.msg.bukkit.BukkitMessage
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.entity.Player
import java.util.EnumSet

@Command("parse")
internal class Cmd(plugin: TestPlugin) : CommandBase() {

    private val audience = BukkitAudiences.create(plugin)

    @Default
    fun parse(player: Player, args: Array<String>) {
        val options = MessageOptions.Builder(EnumSet.allOf(Format::class.java))

        val name = BukkitMessage.create().parse("<g:#AA00AA:#ff82ff>test item").toString()
        val lore = BukkitMessage.create().parse("<g:#AA00AA:#ff82ff>test lore lines").toString().split(" ")

        BukkitMessage.create().parse("&7Successfully created " + "[&a" + 5 + "](hover: " + name + "\n" + lore.joinToString("\\n") + ")").sendMessage(player)

        /*player.sendMessage("${
            measureNanoTime {
                val component = BukkitMessage.create(options.build()).parse(args.joinToString(" "))
                component.sendMessage(player)
            } / 1000000.0
        }ms")*/
    }

}