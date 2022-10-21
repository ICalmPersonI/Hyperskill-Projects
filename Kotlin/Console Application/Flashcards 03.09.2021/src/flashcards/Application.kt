package flashcards

import kotlin.system.exitProcess

class Application {

    fun run(args: Array<String>) {
        val flashcards: Flashcards = Flashcards()

        args.forEachIndexed { index, s -> if (s.contains("-import")) flashcards.import(args[index + 1]) }

        while (true) {
            Logging.line("Input the action" +
                    " (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
            val choice: String = readLine()!!
            Logging.input(choice)
            when (choice) {
                "add" -> flashcards.add()
                "remove" -> flashcards.remove()
                "import" -> {
                    flashcards.import(fileNameInput())
                }
                "export" -> {
                    flashcards.export(fileNameInput())
                }
                "ask" -> flashcards.ask()
                "log" -> Logging.saveLog()
                "hardest card" -> flashcards.hardestCard()
                "reset stats" -> flashcards.resetStats()
                "exit" -> {
                    args.forEachIndexed {
                            index, s -> if (s.contains("-export")) flashcards.export(args[index + 1])
                    }
                    Logging.line("Bye bye!")
                    exitProcess(0)
                }
            }
            Logging.line("\n")
        }
    }

    private fun fileNameInput(): String {
        Logging.line("File name:")
        val fileName: String = readLine()!!
        Logging.input(fileName)
        return fileName
    }
}