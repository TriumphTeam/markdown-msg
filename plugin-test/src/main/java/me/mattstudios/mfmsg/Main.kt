package me.mattstudios.mfmsg

import me.mattstudios.mfmsg.adventure.AdventureMessage
import me.mattstudios.mfmsg.base.internal.components.MessageNode
import me.mattstudios.mfmsg.base.internal.components.TextNode
import me.mattstudios.mfmsg.base.internal.util.ColorUtils
import me.mattstudios.mfmsg.bukkit.Message
import java.awt.Color
import kotlin.system.measureNanoTime


//val gson = GsonBuilder().setPrettyPrinting().create()
fun main(args: Array<String>) {

    val message = AdventureMessage.create()

    val component = message.parse("** hello **")

    component.children().forEach {
        println(it)
    }

    println(component.toString())

}
