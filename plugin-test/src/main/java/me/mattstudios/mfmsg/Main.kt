package me.mattstudios.mfmsg

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer


//val gson = GsonBuilder().setPrettyPrinting().create()
fun main(args: Array<String>) {

    /*val component = Message.create().parse("&c[**this**](command: Test) &r**is &r*Sparta***")
    val jsonElement: JsonElement = JsonParser().parse(component.toJson())
    val json = gson.toJson(jsonElement)

    println(json)*/

    val parser: Parser = Parser.builder().build()
    val document = parser.parse("**a *hello* c*Boy* this**")
    val renderer = HtmlRenderer.builder().build()
    println(renderer.render(document))

    /*val parser = Parser.builder().build()
    val document = parser.parse("&#000[**this**](hover: Test) **is &r*Sparta*** <r>not escaped rainbow")
    val visitor = MarkdownVisitorTest(EnumSet.allOf(Format::class.java))
    visitor.visitComponents(document, null)*/
    //val renderer = HtmlRenderer.builder().build()
    //println(renderer.render(document))

}
