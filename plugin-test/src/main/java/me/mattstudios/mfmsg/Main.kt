package me.mattstudios.mfmsg

import me.mattstudios.mfmsg.adventure.AdventureMessage


//val gson = GsonBuilder().setPrettyPrinting().create()
fun main(args: Array<String>) {

    val message = AdventureMessage.create()

    val component = message.parse("** hello **")

    component.children().forEach {
        println(it)
    }

    println(component.toString())

}
