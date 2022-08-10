package flashcards

import java.io.File

class Logging {
    companion object {
        private var histroty: String = ""

        fun line(s: String) {
            println(s)
            histroty += "$s\n"
        }

        fun input(s: String) {
            histroty += "$s\n"
        }

        fun saveLog() {
            println("File name:")
            histroty += "File name:\n"
            val fileName = readLine()!!
            File(fileName).writeText(histroty)
            println("The log has been saved.")
            histroty += "The log has been saved.\n"
        }
    }
}