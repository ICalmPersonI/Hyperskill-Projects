package tasklist.contract

import java.time.LocalDate
import java.time.LocalTime

interface Presenter {
    interface Task {
        fun create(priority: Model.Task.Priority, date: LocalDate, time: LocalTime, taskContent: List<String>)
        fun delete(number: Int)
        fun edit(number: Int, priority: Model.Task.Priority?, date: LocalDate?, time: LocalTime?, taskContent: List<String>?)
        fun printTasks()
        fun getNumberOfTasks(): Int
        fun saveInFile(fileName: String)
        fun loadFromFile(fileName: String)
    }
}