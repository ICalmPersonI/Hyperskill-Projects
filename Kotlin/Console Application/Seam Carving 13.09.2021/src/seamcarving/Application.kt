package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.pow
import kotlin.math.sqrt


class Application {

    fun run(input: String, output: String, weight: Int, height: Int) {
        var image: BufferedImage = load(input)
        image = resize(image, weight)
        image = rotate(image)
        image = resize(image, height)
        save(rotate(image), output)
    }

    private fun rotate(image: BufferedImage): BufferedImage {
        val rotated = BufferedImage(image.height, image.width, image.type)
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val color: Color = Color(image.getRGB(x, y))
                rotated.setRGB(y, x, color.rgb)
            }
        }
        return rotated
    }

    private fun resize(image: BufferedImage, numberOfPixel: Int): BufferedImage {
        var imageWithSeam: BufferedImage = conversion(image)
        var newImage: BufferedImage? = null

        for (i in 1..numberOfPixel) {
            newImage = BufferedImage(image.width - i, image.height, image.type)
            for (y in 0 until imageWithSeam.height) {

                var cutLineIsCrossed: Boolean = false
                for (x in 0 until imageWithSeam.width) {
                    val color: Color = Color(imageWithSeam.getRGB(x, y))
                    if (color == Color.red) cutLineIsCrossed = true
                    else newImage.setRGB(if (cutLineIsCrossed) x - 1 else x, y, color.rgb)
                }
            }

            if (i != numberOfPixel) imageWithSeam = conversion(newImage)
        }
        return newImage!!
    }

    private fun conversion(image: BufferedImage): BufferedImage {
        val energy: Array<Array<Double>> = Array(image.height) { Array(image.width) { 0.0 } }
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {

                val xShift: Int = if (x - 1 < 0) x + 1 else if (x + 1 > image.width - 1) x - 1 else x
                val yShift: Int = if (y - 1 < 0) y + 1 else if (y + 1 > image.height - 1) y - 1 else y

                val west: Color = Color(image.getRGB(xShift - 1, y))
                val east: Color = Color(image.getRGB(xShift + 1, y))
                val xGradient: Double = getGradient(west, east)

                val north: Color = Color(image.getRGB(x, yShift - 1))
                val south: Color = Color(image.getRGB(x, yShift + 1))
                val yGradient: Double = getGradient(north, south)

                energy[y][x] = getEnergyOfPixel(xGradient, yGradient)
            }
        }

        FindSeam(energy).find()

        for (i in energy[0].indices)
            for (j in energy.indices)
                if (energy[j][i] == -1.0) image.setRGB(i, j, Color(255, 0, 0).rgb)

        return image
    }

    private fun getGradient(x: Color, y: Color): Double =
        ((x.red - y.red).toDouble().pow(2.0)) + ((x.green - y.green).toDouble()
            .pow(2.0)) + ((x.blue - y.blue).toDouble().pow(2.0))

    private fun getEnergyOfPixel(xGradient: Double, yGradient: Double): Double = sqrt(xGradient + yGradient)


    private fun load(imageName: String): BufferedImage {
        return ImageIO.read(File(imageName))
    }

    private fun save(bufferedImage: BufferedImage, imageName: String) =
        ImageIO.write(bufferedImage, "png", File(imageName))
}


