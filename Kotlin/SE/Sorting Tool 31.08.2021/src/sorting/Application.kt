package sorting

import java.io.File
import java.util.*

class Application {

    private lateinit var scanner: Scanner
    private var result: String = ""

    fun run(args: Array<String>) {
        val dataType: String = "-dataType"
        val sortingType: String = "-sortingType"
        val inputFile: String = "-inputFile"
        val outputFile: String = "-outputFile"
        var indexOfType: Int = -1
        var indexOfSortType: Int = -1
        var indexOfInputFile: Int = -1
        var indexOfOutputFile: Int = - 1
        args.forEachIndexed { index, s -> if (s == dataType) indexOfType = index }
        args.forEachIndexed { index, s -> if (s == sortingType) indexOfSortType = index }
        args.forEachIndexed { index, s -> if (s == inputFile) indexOfInputFile = index }
        args.forEachIndexed { index, s -> if (s == outputFile) indexOfOutputFile = index }

        args.filter { it != dataType && it != sortingType && it.contains("-") }
            .forEach { println("\"$it\" is not a valid parameter. It will be skipped.") }

        when {
            args.contains(dataType) && indexOfType + 1 > args.lastIndex -> println("No data type defined!")
            args.contains(sortingType) && indexOfSortType + 1 > args.lastIndex -> println("No sorting type defined!")
            else -> {
                scanner = if (indexOfInputFile > -1) Scanner(args[indexOfInputFile + 1]) else Scanner(System.`in`)
                val sortType: String = if (indexOfSortType > -1) args[indexOfSortType + 1] else "natural"

                when (args[indexOfType + 1]) {
                    "long" -> typeLong(sortType)
                    "line" -> typeLine(sortType)
                    "word" -> typeWord(sortType)
                }

                if (indexOfOutputFile > - 1) File(args[indexOfOutputFile + 1]).writeText(result) else println(result)
            }
        }
    }

    private fun typeLong(sortType: String) {
        val storage: MutableList<Int> = mutableListOf()
        while (scanner.hasNextInt()) {
            val elem: String = scanner.next()
            if ("-*\\d+".toRegex().matches(elem)) storage.add(elem.toInt())
            else result += "\"$elem\" is not a long. It will be skipped.\n"
        }
        result += "Total numbers: ${storage.size}.\n"
        if (sortType == "natural") {
            storage.sort()
            result += "Sorted data: "
            storage.forEach { result += "$it " }
        } else {
            val map: MutableMap<Int, String> = mutableMapOf()
            storage.forEach { elem ->
                val numberOfRepetitionsMax: Int = storage.count { elem == it }
                map[elem] = "$numberOfRepetitionsMax time(s), ${calculatePercent(numberOfRepetitionsMax, storage.size)}"
            }
            map.entries.sortedBy { it.key }.sortedBy { it.value }.forEach { result += "${it.key}: ${it.value}\n" }
        }
    }

    private fun typeLine(sortType: String) {
        val storage: MutableList<String> = mutableListOf()
        while (scanner.hasNextLine()) storage.add(scanner.nextLine())
        result += "Total numbers: ${storage.size}.\n"
        if (sortType == "natural") {
            storage.sort()
            result += "Sorted data: \n"
            storage.forEach { result += "$it " }
        } else {
            val map: MutableMap<String, String> = mutableMapOf()
            storage.forEach { elem ->
                val numberOfRepetitionsMax: Int = storage.count { elem == it }
                map[elem] = "$numberOfRepetitionsMax time(s), ${calculatePercent(numberOfRepetitionsMax, storage.size)}"
            }
            map.entries.sortedBy { it.key }.sortedBy { it.value }.forEach { result += "${it.key}: ${it.value}\n" }
        }
    }

    private fun typeWord(sortType: String) {
        val storage: MutableList<String> = mutableListOf()
        while (scanner.hasNext()) storage.add(scanner.next())
        result += "Total numbers: ${storage.size}.\n"
        if (sortType == "natural") {
            storage.sort()
            result += "Sorted data: "
            storage.forEach { result += "$it " }
        } else {
            val map: MutableMap<String, String> = mutableMapOf()
            storage.forEach { elem ->
                val numberOfRepetitionsMax: Int = storage.count { elem == it }
                map[elem] = "$numberOfRepetitionsMax time(s), ${calculatePercent(numberOfRepetitionsMax, storage.size)}"
            }
            map.entries.sortedBy { it.key }.sortedBy { it.value }.forEach { result += "${it.key}: ${it.value}\n" }
        }
    }

    private fun calculatePercent(current: Int, total: Int): String {
        val result: Double = (current.toDouble() / total.toDouble()) * 100
        return "${result.toString().split(".")[0]}%"
    }
}
