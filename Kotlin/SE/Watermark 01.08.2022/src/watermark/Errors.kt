package watermark

sealed class Errors(val statusCode: Int, val error: String) {
    class FileNotFound(name: String) : Errors(1, "The file $name doesn't exist.")
    class CantReadImageFile(name: String) : Errors(2, "Unable to read image file: $name!")
    class DoesNotHaveThreeColorComponents(type: String) : Errors(3, "The number of $type color components isn't 3.")
    class Not24or32Bit(type: String) : Errors(4, "The $type isn't 24 or 32-bit.")
    class WatermarkAreLarger() : Errors(5, "The watermark's dimensions are larger.")
    class NotIsIntegerNumber() : Errors(6, "The transparency percentage isn't an integer number.")
    class IntegerNotInRange() : Errors(7, "The transparency percentage is out of range.")
    class NotJPGorPNG() : Errors(8, "The output file extension isn't \"jpg\" or \"png\".")
    class InvalidColorInput() : Errors(9, "The transparency color input is invalid.")
    class InvalidPositionMethod() : Errors(10, "The position method input is invalid.")
    class InvalidPositionInput() : Errors(11, "The position input is invalid.")
    class OutOfRangePositionInput() : Errors(12, "The position input is out of range.")
}
