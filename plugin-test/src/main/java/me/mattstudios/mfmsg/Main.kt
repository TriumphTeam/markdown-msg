package me.mattstudios.mfmsg

import com.google.gson.GsonBuilder
import me.mattstudios.mfmsg.commonmark.parser.Parser
import me.mattstudios.mfmsg.commonmark.renderer.html.HtmlRenderer
import org.bukkit.Bukkit

private val gson = GsonBuilder().setPrettyPrinting().create()

fun main(args: Array<String>) {
    /*val message = Message.create()
    message.parse(" &c**Hello** ever__yone__! ")

    println(
            "${
                measureNanoTime {
                    val messageComponent = message.parse("&c**Hello** {item} everyone!")
                    val json = JsonParser().parse(messageComponent.toJson())
                    println(gson.toJson(json))
                } / 1000000
            }ms"
    )*/

    val parser = Parser.builder().build()
    val document = parser.parse("&#000This &cis &r*Sparta*")
    val visitor = MarkdownVisitor()
    visitor.visitComponents(document)
    val renderer = HtmlRenderer.builder().build()
    println(renderer.render(document))

}
