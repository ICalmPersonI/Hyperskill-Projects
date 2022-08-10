package watermark.image

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File


class ImageController(private val image: BufferedImage,
                      private val watermark: BufferedImage,
                      private val view: ImageView) {

    private fun Color.compare(color: Color): Boolean {
        return this.red == color.red && this.green == color.green && this.blue == color.blue
    }

    private fun calcColorForWmPixel(weight: Int, i: Color, w: Color): Color = Color(
            (weight * w.red + (100 - weight) * i.red) / 100,
            (weight * w.green + (100 - weight) * i.green) / 100,
            (weight * w.blue + (100 - weight) * i.blue) / 100)

    fun blend(weight: Int, hasAlpha: Boolean, transparencyColor: Color?,
              single: Boolean, wmPositionX: Int, wmPositionY: Int) {
        val iWight: Int = image.width
        val iHeight: Int = image.height
        val wWidth: Int = watermark.width
        val wHeight: Int = watermark.height
        if (single) {
            for ((iY, wY) in (wmPositionY until iHeight).zip(0 until wHeight)) {
                for ((iX, wX) in (wmPositionX until iWight).zip(0 until wWidth)) {
                    drawPixel(iX, iY, wX, wY, weight, hasAlpha, transparencyColor)
                }
            }
        } else {
            var wX: Int = 0
            var wY: Int = 0
            for (iY in 0 until iHeight) {
                if (wY > wHeight - 1) wY = 0
                for (iX in 0 until iWight) {
                    if (wX > wWidth - 1|| iX == 0) wX = 0
                    drawPixel(iX, iY, wX, wY, weight, hasAlpha, transparencyColor)
                    wX++
                }
                wY++
            }

        }
    }

    private fun drawPixel(iX: Int, iY: Int, wX: Int, wY: Int, weight: Int,
                          hasAlpha: Boolean, transparencyColor: Color?) {
        val i = Color(image.getRGB(iX, iY))
        var color: Color = i
        when {
            hasAlpha -> {
                val w = Color(watermark.getRGB(wX, wY), true)
                if (w.alpha == 255) {
                    color = calcColorForWmPixel(weight, i, w)
                }
            }

            transparencyColor != null -> {
                val w = Color(watermark.getRGB(wX, wY))
                if (!w.compare(transparencyColor)) {
                    color = calcColorForWmPixel(weight, i, w)
                }
            }

            else -> {
                val w = Color(watermark.getRGB(wX, wY))
                color = calcColorForWmPixel(weight, i, w)
            }
        }
        image.setRGB(iX, iY, color.rgb)
    }

    fun save(file: File) {
        view.save(image, file)
    }
}