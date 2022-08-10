package cryptography.image

import java.awt.Color
import java.awt.image.BufferedImage

class Encryption {

    fun encode(image: BufferedImage, message: ByteArray): BufferedImage {
        val bits: ArrayDeque<Int> = messageToBinary(message)

        val resultImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

        loop@ for (x in 0 until image.height) {
            for (y in 0 until image.width) {
                val color: Color = Color(image.getRGB(y, x))

                if (!bits.isEmpty()) {
                    val blueInBinary: String = Integer.toBinaryString(color.blue)
                    val newBlue: String = blueInBinary.substring(0, blueInBinary.lastIndex) + bits.removeFirst()
                    val blueInDecimal: Int = Integer.parseInt(newBlue, 2)
                    val colorNew: Color = Color(color.red, color.green, blueInDecimal)
                    resultImage.setRGB(y, x, colorNew.rgb)
                } else {
                    resultImage.setRGB(y, x, color.rgb)
                }
            }
        }
        return resultImage
    }

    private fun messageToBinary(message: ByteArray): ArrayDeque<Int> {
        return message.flatMap {
            var binary: String = Integer.toBinaryString(it.toInt())
            if (binary.length != 8) {
                val sb: java.lang.StringBuilder = java.lang.StringBuilder()
                for (i in 0 until (8 - binary.length)) sb.append("0")
                binary = sb.toString() + binary
            }
            binary.toList()
        }.map { it.toString().toInt() }.toCollection(ArrayDeque())
    }
}