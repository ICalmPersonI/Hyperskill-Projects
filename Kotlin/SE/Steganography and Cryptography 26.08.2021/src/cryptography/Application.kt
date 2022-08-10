package cryptography


import cryptography.image.Decryption
import cryptography.image.Encryption
import cryptography.image.Utils
import java.awt.image.BufferedImage
import java.io.IOException
import kotlin.experimental.xor
import kotlin.system.exitProcess

class Application {

    fun run() {
        while (true) {
            println("Task (hide, show, exit):")
            when (val input: String = readLine()!!) {
                "hide" -> hide()
                "show" -> show()
                "exit" -> {
                    println("Bye!")
                    exitProcess(0)
                }
                else -> println("Wrong task: $input")
            }
        }
    }

    private fun hide() {
        println("Input image file:")
        val input: String = readLine()!!
        println("Output image file:")
        val output: String = readLine()!!
        println("Message to hide:")
        val message: String = readLine()!!
        println("Password:")
        val password: String = readLine()!!
        try {
            val image: BufferedImage = Utils.load(input)
            val byteArrayMessage: ByteArray = symmetricEncryptionMessage(message.toByteArray(), password.toByteArray())
            if (image.height > byteArrayMessage.size) {
                val convertedImage: BufferedImage = Encryption().encode(image, byteArrayMessage +  0 + 0 + 3)
                Utils.save(output, convertedImage)
                println("Message saved in $output image.")
            } else println("The input image is not large enough to hold this message.")
        } catch (e: IOException) {
            println("Can't read input file!")
        }
    }

    private fun show() {
        println("Input image file:")
        val input: String = readLine()!!
        val image: BufferedImage = Utils.load(input)
        println("Password:")
        val password: String = readLine()!!
        val message: ByteArray = symmetricEncryptionMessage(Decryption().decode(image), password.toByteArray())
        println("Message:")
        println(String(message))
    }

    private fun symmetricEncryptionMessage(message: ByteArray, password: ByteArray): ByteArray {
        var j: Int = 0
        for (i in 0..message.lastIndex) {
            message[i] = message[i] xor password[j]
            j++
            if (j > password.lastIndex) j = 0
        }
        return message
    }
}

