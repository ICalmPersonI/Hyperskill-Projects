package tasklist.view

import tasklist.contract.Model
import tasklist.contract.Presenter
import tasklist.contract.View
import tasklist.presenter.TaskPresenter
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.LocalTime

class TaskView : View.Task {

    companion object {
        const val TABLE_TITLE = "+----+------------+-------+---+---+--------------------------------------------+\n" +
                                "| N  |    Date    | Time  | P | D |                   Task                     |\n" +
                                "+----+------------+-------+---+---+--------------------------------------------+\n"
        const val TABLE_SEPARATOR = "+----+------------+-------+---+---+--------------------------------------------+"
        const val DATA_FILE_NAME = "tasklist.json"
    }

    private val presenter: Presenter.Task = TaskPresenter(this)
    override var isActive = true

    fun run() {
        presenter.loadFromFile(DATA_FILE_NAME)
        while (isActive) {
            printMessage(Message.INPUT_COMMAND)
            when (readln()) {
                "add" -> addTask()
                "print" -> presenter.printTasks()
                "edit" -> editTask()
                "delete" -> deleteTask()
                "end" -> {
                    printMessage(Message.EXIT_PROGRAM)
                    isActive = false
                    presenter.saveInFile(DATA_FILE_NAME)
                }
                else -> printMessage(Message.INVALID_INPUT)
            }
        }
    }

    private fun addTask() {
        val priority: Model.Task.Priority = inputPriority()
        val date: LocalDate = inputDate()
        val time: LocalTime = inputTime()
        val content: List<String> = inputContent()
        presenter.create(priority, date, time, content)
    }

    private fun deleteTask() {
        val number: Int = inputNumber()
        if (number != -1) {
            presenter.delete(number)
            printMessage(Message.TASK_IS_DELETED)
        }
    }

    private fun editTask() {
        val fields = listOf("priority", "date", "time", "task")
        val number: Int = inputNumber()
        if (number != -1) {
            while (true) {
                printMessage(Message.INPUT_FIELD_TO_EDIT)
                val field = readln().trim()
                if (fields.contains(field)) {
                    var priority: Model.Task.Priority? = null
                    var date: LocalDate? = null
                    var time: LocalTime? = null
                    var content: List<String>? = null
                    when (field) {
                        "priority" -> priority = inputPriority()
                        "date" -> date = inputDate()
                        "time" -> time = inputTime()
                        "task" -> content = inputContent()
                    }
                    presenter.edit(number, priority, date, time, content)
                    printMessage(Message.TASK_IS_CHANGED)
                    break
                } else printMessage(Message.INVALID_FIELD)
            }
        }
    }

    private fun inputNumber(): Int {
        presenter.printTasks()
        if (presenter.getNumberOfTasks() != 0) {
            while (true) {
                try {
                    println(Message.INPUT_TASK_NUMBER.value.format(presenter.getNumberOfTasks()))
                    val number = readln().trim().toInt()
                    if (number in (1..presenter.getNumberOfTasks())) return number
                    else printMessage(Message.INVALID_TASK_NUMBER)
                } catch (_: NumberFormatException) {
                    printMessage(Message.INVALID_TASK_NUMBER)
                }
            }
        }
        return -1
    }

    private fun inputContent(): List<String> {
        val content = mutableListOf<String>()
        printMessage(Message.INPUT_CONTENT)
        while (true) {
            val input: String = readln().trim()
            if (input.isEmpty() || input.isBlank()) return content
            content.add(input)
        }
    }

    private fun inputPriority(): Model.Task.Priority {
        while (true) {
            printMessage(Message.INPUT_PRIORITY)
            val priority: String = readln().trim().uppercase()
            Model.Task.Priority.findKeyByValue(priority)?.let { return it }
        }
    }

    private fun inputDate(): LocalDate {
        while (true) {
            printMessage(Message.INPUT_DATE)
            val input: String = readln().trim()
            try {
                val date: List<Int> = input.split('-').map { it.toInt() }
                val year = date[0]
                val month = date[1]
                val dayOfMonth = date[2]
                return LocalDate.of(year, month, dayOfMonth)
            } catch (e: Exception) {
                printMessage(Message.INVALID_DATE)
            }
        }
    }

    private fun inputTime(): LocalTime {
        while (true) {
            printMessage(Message.INPUT_TIME)
            val input: String = readln().trim()
            try {
                val time: List<Int> = input.split(':').map { it.toInt() }
                val hour = time[0]
                val minutes = time[1]
                return LocalTime.of(hour, minutes)
            } catch (e: Exception) {
                printMessage(Message.INVALID_TIME)
            }
        }
    }

    override fun printMessage(msg: Message) {
        println(msg.value)
    }

    override fun printTasks(tasks: List<Model.Task>) {
        val taskRowLineLen = 44
        print(TABLE_TITLE)
        for ((task, id) in tasks.zip(tasks.indices)) {
            val number = id + 1
            val alignedContent: List<String> = alignContent(task.content, taskRowLineLen)
            println("| ${if (number < 10) "$number  " else "$number "}| ${task.date} | ${task.time} | ${task.priority.color} | ${task.status.color} |${alignedContent.first()}|")
            alignedContent.drop(1).forEach { println("|    |            |       |   |   |$it|") }
            println(TABLE_SEPARATOR)
        }
    }

    private fun alignContent(content: List<String>, width: Int): List<String> {
        val aligned: MutableList<String> = mutableListOf()
        content.forEach {
            var line = it
            var start = 0
            var end = width
            while (end < line.length) {
                aligned.add(line.substring(start, end))
                start += width
                end += width
            }
            line = line.substring(start, line.length)
            if (line.isNotEmpty()) {
                for (i in line.length until width) line += " "
                aligned.add(line)
            }
        }
        return aligned
    }
}