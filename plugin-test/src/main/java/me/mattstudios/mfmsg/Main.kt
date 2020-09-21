package me.mattstudios.mfmsg

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import me.mattstudios.mfmsg.base.Message


val gson = GsonBuilder().setPrettyPrinting().create()

fun String.test() = this + "Hey"

fun main(args: Array<String>) {

    val component = Message.create().parse("&c[**this**](command: Test) &r**is &r*Sparta***")
    val jsonElement: JsonElement = JsonParser().parse(component.toJson())
    val json = gson.toJson(jsonElement)

    println(json)

    /*val parser = Parser.builder().build()
    val document = parser.parse("&#000[**this**](hover: Test) **is &r*Sparta*** <r>not escaped rainbow")
    val visitor = MarkdownVisitorTest(EnumSet.allOf(Format::class.java))
    visitor.visitComponents(document, null)*/
    //val renderer = HtmlRenderer.builder().build()
    //println(renderer.render(document))

}
