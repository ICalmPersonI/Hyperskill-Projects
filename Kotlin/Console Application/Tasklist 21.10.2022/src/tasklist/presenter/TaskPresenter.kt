package tasklist.presenter

import tasklist.contract.Model
import tasklist.contract.Presenter
import tasklist.contract.View
import tasklist.model.Task
import tasklist.model.repository.TaskRepository
import tasklist.view.Message
import com.squareup.moshi.*
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class TaskPresenter(private val view: View.Task) : Presenter.Task {

    private val repository: Model.Task.Repository = TaskRepository()

    override fun create(priority: Model.Task.Priority, date: LocalDate, time: LocalTime, taskContent: List<String>) {
        if (taskContent.isEmpty()) view.printMessage(Message.BLANK_TASK).also { return }
        val content = taskContent.map { it.trim() }
        repository.add(Task(priority, date, time, content, Model.Task.Status.IN_TIME))
    }

    override fun delete(number: Int) {
        val id = number - 1
        repository.delete(id)
    }

    override fun edit(number: Int, priority: Model.Task.Priority?, date: LocalDate?, time: LocalTime?, taskContent: List<String>?) {
        val id = number - 1
        val old: Model.Task = repository.get(id)
        val newTask: Model.Task = Task(
                priority ?: old.priority,
                date ?: old.date,
                time ?: old.time,
                taskContent?.map { it.trim() } ?: old.content,
                Model.Task.Status.IN_TIME
        )
        repository.replace(id, newTask)
    }

    override fun printTasks() {
        if (repository.size() == 0) view.printMessage(Message.NO_TASKS).also { return }
        val tasks: List<Model.Task> = repository.getAll()
        tasks.map {
            val deadline = it.date
            val now = LocalDate.now()
            it.status = when {
                deadline.isBefore(now) -> Model.Task.Status.OVERDUE
                deadline.isAfter(now) -> Model.Task.Status.IN_TIME
                else -> Model.Task.Status.TODAY
            }
        }
        view.printTasks(repository.getAll())
    }

    override fun getNumberOfTasks(): Int = repository.size()

    override fun saveInFile(fileName: String) {
        val file = File(fileName).also { it.createNewFile() }
        repository.serialization(file)
    }

    override fun loadFromFile(fileName: String) {
        val file = File(fileName)
        if (file.exists()) repository.deserialization(file)
    }
}