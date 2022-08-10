package converter

import java.lang.IndexOutOfBoundsException
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import java.util.stream.IntStream
import kotlin.system.exitProcess

class Application {

    fun run() {

        while (true) {
            var input: String
            print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
            input = readLine()!!

            if ("/exit" != input) {
                val bases: List<String> = input.split(" ")
                val sourceBase: Int = bases[0].toInt()
                val targetBase: Int = bases[1].toInt()

                while (true) {
                    print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
                    input = readLine()!!

                    if ("/back" != input) {
                        if (isFractional(input)) {
                            val split: List<String> = input.split(".")
                            val integer: List<String> = toArray(split[0])
                            val fractional: List<String> = toArray(split[1])
                            val decimal: List<String> = convertFractionalToDecimal(integer, fractional, sourceBase)
                                .split(".")
                            println(
                                "Conversion result:" +
                                        convertInteger(integer, sourceBase, targetBase) +
                                        "." +
                                        convertFractional(decimal[1], targetBase)
                            )

                        } else {
                            val number: List<String> = toArray(input)
                            println("Conversion result: ${convertInteger(number, sourceBase, targetBase)}\n")
                        }

                    } else {
                        println()
                        break
                    }
                }

            } else {
                exitProcess(0)
            }
        }

    }

    private fun isFractional(number: String) = number.contains("\\.".toRegex())

    private fun toArray(number: String): List<String> {
        val result: ArrayList<String> = ArrayList()
        if (number.contains("[A-Za-z]".toRegex())) {
            val charArray: CharArray = number.toCharArray()
            for (elem in charArray) {
                if (elem.code > 96) result.add((elem.code - 87).toString())
                else result.add(elem.toString())
            }
        } else {
            number.forEach { result.add(it.toString()) }
        }
        return result
    }

    private fun convertInteger(number: List<String>, sourceBase: Int, targetBase: Int) =
        if (number.size == 1 && number[0] == "0") "0"
        else BigInteger(toDecimal(number, sourceBase), 10).toString(targetBase)


    private fun toDecimal(number: List<String>, sourceBase: Int): String {
        val numbers: List<BigInteger> = number.map { BigInteger(it) }
        var result: Double = 0.0
        var degree: Int = number.lastIndex
        val base: BigInteger = BigInteger(sourceBase.toString())
        numbers.forEach { result += it.multiply(base.pow(degree--)).toDouble() }
        return result.toLong().toString()
    }

    private fun convertFractionalToDecimal(integer: List<String>, fractional: List<String>, targetBase: Int): String {
        val target = BigDecimal(targetBase)
        var sum = BigDecimal.ZERO

        run {
            var i = 0
            var degree = integer.size - 1
            while (i < integer.size) {
                val number = BigDecimal(integer[i])
                sum = sum.add(number.multiply(target.pow(degree)))
                i++
                degree--
            }
        }


        run {
            var i = 0
            var degree = -1
            while (i < fractional.size) {
                val number = BigDecimal(fractional[i])
                sum = sum.add(number.multiply(target.pow(degree, MathContext.DECIMAL128)))
                i++
                degree--
            }
        }

        return sum.setScale(5, RoundingMode.CEILING).toString()
    }


    private fun convertFractional(inputFractional: String, targetBase: Int): String {
        val result: StringBuilder = StringBuilder()
        val base: BigDecimal = BigDecimal(targetBase)
        var fractional: BigDecimal = BigDecimal("0.${inputFractional}")
        if (fractional == BigDecimal.ZERO) {
            result.append("0")
        } else {
            for (i in 0..4) {
                fractional = fractional.multiply(base)
                val splittedResultOfMultiply: List<String> = fractional.toString().split(".")
                var integer: String = splittedResultOfMultiply[0]

                if (splittedResultOfMultiply.size == 1) break
                if (splittedResultOfMultiply[0].contains("[A-Za-z]".toRegex())) break
                if (splittedResultOfMultiply[0].toInt() == 0) break

                fractional = BigDecimal("0.${splittedResultOfMultiply[1]}")

                if (integer.length > 1) {
                    val symbol: Int = (integer.toLong() + 87).toInt()
                    if (symbol in 97..122) {
                        integer = symbol.toChar().toString()
                    }
                }

                result.append(integer)
            }

            if (result.length < 5) IntStream.range(result.length, 5).forEach { result.append("0") }
            else if (result.length > 5) result.setLength(5)
        }

        return result.toString()
    }

}

