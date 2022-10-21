package tasklist.model.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import tasklist.contract.Model
import java.io.File

class TaskRepository : Model.Task.Repository {

    private var list = mutableListOf<Model.Task>()
    private val moshi = Moshi.Builder()
            .add(TaskJsonAdapter())
            .build()
            .adapter<MutableList<Model.Task>>(Types.newParameterizedType(MutableList::class.java, Model.Task::class.java))

    override fun get(id: Int): Model.Task = list[id]

    override fun getAll(): List<Model.Task> = list

    override fun size(): Int = list.size

    override fun add(task: Model.Task) {
        list.add(task)
    }

    override fun delete(id: Int) {
        list.removeAt(id)
    }

    override fun replace(id: Int, newTask: Model.Task) {
        list.removeAt(id)
        list.add(id, newTask)
    }

    override fun serialization(file: File) {
        file.writeText(moshi.toJson(list))
    }

    override fun deserialization(file: File) {
        moshi.fromJson(file.readText())?.let { list = it }
    }
}