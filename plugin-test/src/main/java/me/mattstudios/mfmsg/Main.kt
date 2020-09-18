package me.mattstudios.mfmsg

import me.mattstudios.mfmsg.base.Message
import me.mattstudios.mfmsg.commonmark.parser.Parser
import me.mattstudios.mfmsg.commonmark.renderer.html.HtmlRenderer

fun main(args: Array<String>) {
    val message = Message.create()
    val messageComponent = message.parse(" **Hello** ever__yone__! ")

    val parser = Parser.builder().build()
    val document = parser.parse(" **Hello**\\n everyone! ")
    val renderer = HtmlRenderer.builder().build()

    println(renderer.render(document))
    println("|${messageComponent.toJson()}|")
}
