package tasklist.contract

import java.io.File
import java.time.LocalDate
import java.time.LocalTime

interface Model {
    interface Task {

        val priority: Priority
        val date: LocalDate
        val time: LocalTime
        val content: List<String>
        var status: Status

        enum class Priority(val value: String, val color: String) {
            CRITICAL("C", "\u001B[101m \u001B[0m"),
            HIGH("H", "\u001B[103m \u001B[0m"),
            NORMAL("N", "\u001B[102m \u001B[0m"),
            LOW("L", "\u001B[104m \u001B[0m");

            companion object {
                fun findKeyByValue(value: String): Priority? {
                    for (key in Priority.values()) {
                        if (key.value == value) return key
                    }
                    return null
                }
            }
        }

        enum class Status(val value: String, val color: String) {
            IN_TIME("I", "\u001B[102m \u001B[0m"),
            TODAY("T", "\u001B[103m \u001B[0m"),
            OVERDUE("O", "\u001B[101m \u001B[0m");

            companion object {
                fun findKeyByValue(value: String): Status? {
                    for (key in Status.values()) {
                        if (key.value == value) return key
                    }
                    return null
                }
            }
        }

        interface Repository {
            fun get(id: Int): Task
            fun getAll(): List<Task>
            fun add(task: Task)
            fun delete(id: Int)
            fun replace(id: Int, newTask: Task)
            fun size(): Int
            fun serialization(file: File)
            fun deserialization(file: File)
        }

    }
}