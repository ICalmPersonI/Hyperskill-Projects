package search

import java.io.File
import kotlin.system.exitProcess

class Application(file: File) {

    private val storage: List<String> = file.readLines()
    private val invertedIndexMap: Map<String, List<Int>> = createInvertedIndex()

    private fun createInvertedIndex(): Map<String, List<Int>> {
        val map: MutableMap<String, List<Int>> = mutableMapOf()
        for (line in storage) {
            for (word in line.split(" ")) {
                val indexes: MutableList<Int> = mutableListOf()
                storage.forEachIndexed { index, s -> if (s.lowercase().contains(word.lowercase())) indexes.add(index) }
                map[word.lowercase()] = indexes
            }
        }
        return map
    }

    fun run() {
        while (true) {
            println(
                "=== Menu ===\n" +
                        "1. Find a person\n" +
                        "2. Print all people\n" +
                        "0. Exit"
            )

            when (readLine()!!.toInt()) {
                1 -> {
                    println("Select a matching strategy: ALL, ANY, NONE")
                    findPerson(readLine()!!)
                }
                2 -> printAllPerson()
                0 -> exitProcess(0)
                else -> println("Incorrect option! Try again.")
            }
            println()
        }
    }

    private fun findPerson(strategy: String) {
        println("\nEnter a name or email to search all suitable people.")
        val pattern: String = readLine()!!.lowercase()
        val foundPeople: MutableList<String> = mutableListOf()
        val split: List<String> = pattern.split(" ").map { it.lowercase() }

        for (elem in split) {

            when (strategy) {
                "ALL" -> {
                    invertedIndexMap[elem]?.forEach { foundPeople.add(storage[it]) } ?: run {
                        foundPeople.clear()
                    }
                }
                "ANY" -> {
                    invertedIndexMap[elem]?.forEach { foundPeople.add(storage[it]) }
                }
                "NONE" -> {
                    val temp: MutableList<String> = mutableListOf()
                    split.forEach {
                        invertedIndexMap[it]?.forEach { e -> temp.add(storage[e].lowercase()) }
                    }
                    storage.filter { !temp.contains(it.lowercase()) }.forEach { foundPeople.add(it) }
                    break
                }
            }
        }
        println("\n${foundPeople.size} persons found:")
        foundPeople.forEach { println(it) }
    }

    private fun printAllPerson() {
        println("\n=== List of people ===")
        storage.forEach { println(it) }
    }
}