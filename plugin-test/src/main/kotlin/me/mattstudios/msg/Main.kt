package me.mattstudios.msg

import me.mattstudios.msg.base.MessageOptions
import me.mattstudios.msg.base.internal.parser.MarkdownParser
import me.mattstudios.msg.extensions.BracketExtension

fun main() {

    val parser = MarkdownParser(MessageOptions.builder().extensions(BracketExtension()).build())
    val nodes = parser.parse("~~of~~ **there**")

    //println(nodes.joinToString("\n"))

}