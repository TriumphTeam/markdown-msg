package me.mattstudios.msg

import me.mattstudios.msg.base.FormatData
import me.mattstudios.msg.base.MessageOptions
import me.mattstudios.msg.base.internal.action.MessageAction
import me.mattstudios.msg.base.internal.parser.MarkdownParser

fun main() {
    val data = FormatData()
    data.actions = listOf(MessageAction.from(MarkdownParser(MessageOptions.builder().build()).parse("Hello fellow!")))
    val options = MessageOptions.builder().setDefaultFormatData(data).build()
    val parser = MarkdownParser(options)
    val nodes = parser.parse("~~of~~ **there**")

    println(nodes.joinToString("\n"))

}