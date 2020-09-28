package me.mattstudios.mfmsg

import me.mattstudios.mfmsg.base.internal.components.MessageNode
import me.mattstudios.mfmsg.base.internal.components.TextNode
import me.mattstudios.mfmsg.base.internal.util.ColorUtils
import java.awt.Color
import kotlin.system.measureNanoTime


//val gson = GsonBuilder().setPrettyPrinting().create()
fun main(args: Array<String>) {

    val sampleSize = 1000

    val colors = mutableListOf(
            "#000000",
            "#000000",
            "#000000",
            "#000000",
            "#000000",
            "#000000"
    )

    val ktList = mutableListOf<Long>()
    repeat(sampleSize) {
        val timeKt = measureNanoTime {
            colors.map { ColorUtils.hexToColor(it) }
        }
        ktList.add(timeKt)
    }

    val ktForList = mutableListOf<Long>()
    repeat(sampleSize) {
        val timeFor = measureNanoTime {
            val list = mutableListOf<Color>()
            for (color in colors) {
                list.add(ColorUtils.hexToColor(color))
            }
        }
        ktForList.add(timeFor)
    }

    val jvStreamList = mutableListOf<Long>()
    repeat(sampleSize) {
        val javaStream = measureNanoTime {
            Java.stream(colors)
        }
        jvStreamList.add(javaStream)
    }

    val jvForList = mutableListOf<Long>()
    repeat(sampleSize) {
        val javaNormal = measureNanoTime {
            Java.normal(colors)
        }
        jvForList.add(javaNormal)
    }

    println("Kt: ${ktList.average() / 1000000.0}ms")
    println("Kt for: ${ktForList.average() / 1000000.0}ms")
    println("Jv stream: ${jvStreamList.average() / 1000000.0}ms")
    println("Jv for: ${jvForList.average() / 1000000.0}ms")

}
