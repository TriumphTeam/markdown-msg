package me.mattstudios.msg.extensions

import me.mattstudios.msg.base.internal.nodes.TextNode
import me.mattstudios.msg.base.internal.renderer.NodeRenderer
import me.mattstudios.msg.commonmark.internal.inline.ParsedInline
import me.mattstudios.msg.commonmark.internal.inline.Scanner
import me.mattstudios.msg.commonmark.internal.inline.triumph.TriggerProcessor
import me.mattstudios.msg.commonmark.node.CustomNode
import me.mattstudios.msg.commonmark.parser.Parser
import me.mattstudios.msg.commonmark.parser.ParserExtension


class BracketExtension : ParserExtension, NodeRenderer {

    override fun extend(builder: Parser.Builder) {
        builder.customTriggerProcessor(ProcessorTest())
    }

    override fun render(node: CustomNode): TextNode {
        println(node)
        return TextNode("pinged")
    }

    override fun getParserNode(): Class<out CustomNode> {
        return PingNode::class.java
    }

}

class ProcessorTest : TriggerProcessor {

    override fun getTriggerCharacter() = '@'

    override fun parse(scanner: Scanner): ParsedInline? {
        scanner.next()

        val start = scanner.position()

        var i = 0
        while (scanner.hasNext()) {
            scanner.next()

            if (scanner.peek() == ' ') {
                val literal = scanner.textBetween(start, scanner.position()).toString()
                return ParsedInline.of(PingNode(literal), scanner.position())
            }

            if (i > 10) break

            i++
        }

        return ParsedInline.none()
    }

}