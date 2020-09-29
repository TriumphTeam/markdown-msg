package me.mattstudios.mfmsg

import me.mattstudios.mfmsg.base.internal.components.MessageNode
import me.mattstudios.mfmsg.base.internal.components.TextNode
import me.mattstudios.mfmsg.base.internal.util.ColorUtils
import me.mattstudios.mfmsg.bukkit.Message
import java.awt.Color
import kotlin.system.measureNanoTime


//val gson = GsonBuilder().setPrettyPrinting().create()
fun main(args: Array<String>) {

    val message = Message.create()

    val component = message.parse("<r:.75>***Hello everyone!** *boy!* __hey__*")
    println(component.toString())

}
