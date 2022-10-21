package cryptography.image

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Utils {

    companion object {
        fun load(fileName: String): BufferedImage {
            val file: File = File(fileName)
            return ImageIO.read(file)
        }

        fun save(fileName: String, image: BufferedImage) {
            ImageIO.write(image, "png", File(fileName))
        }
    }
}