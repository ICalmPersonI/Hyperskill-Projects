package flashcards

import java.io.File
import java.io.FileNotFoundException
import kotlin.collections.HashMap

class Flashcards {

    private val storage: HashMap<String, String> = hashMapOf()
    private val mistakes: HashMap<String, Int> = hashMapOf()
    private val delimiter: String = "████"

    fun add() {
        Logging.line("The card:")
        val term = readLine()!!
        Logging.input(term)
        if (storage.containsKey(term)) return Logging.line("The card \"$term\" already exists.")

        Logging.line("The definition of the card:")
        val definition: String = readLine()!!
        Logging.input(definition)
        if (storage.containsValue(definition)) return Logging.line("The definition \"$definition\" already exists.")

        storage[term] = definition
        Logging.line("The pair (\"$term\":\"$definition\") has been added.")
    }

    fun remove() {
        Logging.line("Which card?")
        val term: String = readLine()!!
        Logging.input(term)
        when {
            storage.containsKey(term) -> {
                storage.remove(term)
                Logging.line("The card has been removed.")
            }
            else -> Logging.line("Can't remove \"$term\": there is no such card.")
        }
    }

    fun import(fileName: String) {
        Logging.line("File name:")
        try {
            val lines = File(fileName).readLines()
            lines.forEach {
                if (it.isNotEmpty()) {
                    val (term, definition, errors) = it.split(delimiter)
                    storage[term] = definition
                    mistakes[term] = errors.toInt()
                }
            }
            Logging.line("${lines.lastIndex} cards have been loaded.")
        } catch (e: FileNotFoundException) {
            Logging.line("File not found.")
        }
    }

    fun export(fileName: String) {
        File(fileName).delete()
        for ((term, definition) in storage) {
            val errors = mistakes[term]?: 0
            File(fileName).appendText("\n$term${delimiter}$definition${delimiter}$errors")
        }
        Logging.line("${storage.size} cards have been saved.")
    }

    fun hardestCard() {
        if (mistakes.isNotEmpty()) {
            var cards: String = ""
            val max: Int = mistakes.entries.maxOf { it.value }
            mistakes.entries.filter { it.value == max }.forEach { cards += "\"${it.key}\", " }
            val numberOfCards: Int = mistakes.entries.filter { it.value == max }.count()
            Logging.line(
                "The hardest car${if (numberOfCards == 1) "d is" else "ds are"} ${
                    cards.substring(
                        0,
                        cards.lastIndex - 1
                    )
                }. ${if (numberOfCards == 1) "You have $max errors answering it." else "You have $max errors answering them\"."}"
            )
        } else Logging.line("There are no cards with errors.")
    }

    fun resetStats() {
        mistakes.clear()
        Logging.line("Card statistics have been reset.")
    }

    fun ask() {
        Logging.line("How many times to ask?")
        var countOfTask: Int = readLine()!!.toInt()
        Logging.input(countOfTask.toString())
        for ((term, definition) in storage) {
            Logging.line("Print the definition of \"$term\":")
            val answer: String = readLine()!!
            Logging.input(answer)
            when {
                answer == definition -> Logging.line("Correct!")
                storage.containsValue(answer) -> {
                    if (mistakes.containsKey(term)) mistakes[term] = mistakes[term]!! + 1 else mistakes[term] = 1
                    var result: String = ""
                    storage.filter { it.value == answer }.forEach { result += "${it.key}, " }
                    Logging.line(
                        "Wrong. The right answer is \"$definition\"," +
                                " but your definition is correct for \"${result.substring(0, result.lastIndex - 1)}\"."
                    )
                }
                else -> {
                    if (mistakes.containsKey(term)) mistakes[term] = mistakes[term]!! + 1 else mistakes[term] = 1
                    Logging.line("Wrong. The right answer is \"$definition\".")
                }
            }
            countOfTask--
            if (countOfTask == 0) break
        }
    }
}
