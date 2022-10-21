package tasklist.model.repository

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import tasklist.contract.Model
import tasklist.model.Task
import java.time.LocalDate
import java.time.LocalTime

class TaskJsonAdapter {
    @ToJson
    fun toJson(tasks: MutableList<Model.Task>): List<Map<String, Any>> {
        return tasks.map {
            mapOf(
                    "priority" to it.priority.value,
                    "date" to it.date.toString(),
                    "time" to it.time.toString(),
                    "content" to it.content,
                    "status" to it.status.value
            )
        }.toList()
    }

    @FromJson
    fun fromJson(json: List<Map<String, Any>>): MutableList<Model.Task> {
        return json.map {
            Task(
                    Model.Task.Priority.findKeyByValue(it["priority"] as String)!!,
                    LocalDate.parse(it["date"] as String),
                    LocalTime.parse(it["time"] as String),
                    it["content"] as List<String>,
                    Model.Task.Status.findKeyByValue(it["status"] as String)!!
            )
        }.toMutableList()
    }
}