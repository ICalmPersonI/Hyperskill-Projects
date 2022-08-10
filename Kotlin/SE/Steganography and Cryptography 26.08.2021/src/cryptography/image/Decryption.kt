package cryptography.image

import java.awt.Color
import java.awt.image.BufferedImage

class Decryption {

    fun decode(image: BufferedImage): ByteArray {
        val message: MutableList<String> = mutableListOf()
        val cache: StringBuilder = StringBuilder()
        loop@ for (x in 0 until image.height) {
            for (y in 0 until image.width) {
                val color: Color = Color(image.getRGB(y, x))
                val bites: String = Integer.toBinaryString(color.blue)
                val lastBite: String = bites[bites.lastIndex].toString()
                cache.append(lastBite)

                if (cache.length == 8) {
                    message.add(cache.toString())
                    cache.setLength(0)
                    if (message.size > 3
                        && (message[message.lastIndex] == "00000011"
                                && message[message.lastIndex - 1] == "00000000"
                                && message[message.lastIndex - 2] == "00000000")
                    ) break@loop
                }
            }
        }
        return message.take(message.lastIndex - 2).map{ Integer.parseInt(it, 2).toByte() }.toByteArray()
    }
}