package watermark.image

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


class ImageView {
    fun save(image: BufferedImage, file: File) {
        ImageIO.write(image, file.extension, file)
        println("The watermarked image ${file.path} has been created.")
    }
}