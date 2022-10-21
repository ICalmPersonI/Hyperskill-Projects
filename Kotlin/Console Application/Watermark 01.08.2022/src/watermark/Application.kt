package watermark

import watermark.image.ImageController
import watermark.image.ImageView
import java.awt.Color
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.IIOException
import javax.imageio.ImageIO
import kotlin.system.exitProcess

class Application {

    fun run() {
        println("Input the image filename:")
        val imagePath: String = readln()
        val image: BufferedImage = readImageAsBufferedImage(imagePath, "image")!!

        println("Input the watermark image filename:")
        val watermarkPath: String = readln()
        val watermark: BufferedImage = readImageAsBufferedImage(watermarkPath, "watermark")!!

        if (watermark.width > image.width || watermark.height > image.height) {
            abort(Errors.WatermarkAreLarger())
        }

        var hasAlpha: Boolean = false
        var transparencyColor: Color? = null
        if (watermark.transparency == Transparency.TRANSLUCENT) {
            println("Do you want to use the watermark's Alpha channel?")
            hasAlpha = readln().lowercase() == "yes"
        } else {
            println("Do you want to set a transparency color?")
            if (readln().lowercase() == "yes") {
                try {
                    println("Input a transparency color ([Red] [Green] [Blue]):")
                    val colors = readln().split(' ').map { it.toInt() }.toList()
                    if (colors.size != 3 || colors.any { it !in 0..255 }) abort(Errors.InvalidColorInput())
                    transparencyColor = Color(colors[0], colors[1], colors[2])
                } catch (e: NumberFormatException) {
                    abort(Errors.InvalidColorInput())
                }
            }
        }

        var weightPercentage: Int = 0
        try {
            println("Input the watermark transparency percentage (Integer 0-100):")
            weightPercentage = readln().toInt()
            if (weightPercentage !in (0..100)) {
                abort(Errors.IntegerNotInRange())
            }
        } catch (e: NumberFormatException) {
            abort(Errors.NotIsIntegerNumber())
        }

        println("Choose the position method (single, grid):")
        val positionMethod: String = readln()
        var wmPositionX: Int = 0
        var wmPositionY: Int = 0
        if (positionMethod in listOf("single", "grid")) {
            if (positionMethod == "single") {
                val diffX: Int = image.width - watermark.width
                val diffY: Int = image.height - watermark.height
                println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
                try {
                    val input: List<Int> = readln().split(' ').map { it.toInt() }.toList()
                    if (input.size > 2) {
                        abort(Errors.InvalidPositionInput())
                    }
                    wmPositionX = input[0]
                    wmPositionY = input[1]
                    if (wmPositionX > diffX || wmPositionY > diffY || wmPositionX < 0 || wmPositionY < 0) {
                        abort(Errors.OutOfRangePositionInput())
                    }
                } catch (e: NumberFormatException) {
                    abort(Errors.InvalidPositionInput())
                }
            }
        } else {
            abort(Errors.InvalidPositionMethod())
        }

        println("Input the output image filename (jpg or png extension):")
        val outputFile: File = File(readln())
        if (outputFile.extension !in listOf("png", "jpg")) {
            abort(Errors.NotJPGorPNG())
        }

        val view: ImageView = ImageView()
        val controller: ImageController = ImageController(image, watermark, view)
        controller.blend(weightPercentage, hasAlpha, transparencyColor,
                positionMethod == "single", wmPositionX, wmPositionY)
        controller.save(outputFile)

    }

    private fun abort(errors: Errors) {
        println(errors.error)
        exitProcess(errors.statusCode)
    }

    private fun readImageAsBufferedImage(path: String, type: String): BufferedImage? {
        val file: File = File(path)
        if (!file.exists()) {
            abort(Errors.FileNotFound(path))
        }
        try {
            return ImageIO.read(file).apply {
                if (colorModel.numColorComponents != 3) abort(Errors.DoesNotHaveThreeColorComponents(type))
                if (colorModel.pixelSize !in listOf(24, 32)) abort(Errors.Not24or32Bit(type))
            }
        } catch (e: IIOException) {
            abort(Errors.CantReadImageFile(path))
        }
        return null
    }
}