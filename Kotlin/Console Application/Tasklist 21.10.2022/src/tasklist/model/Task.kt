package tasklist.model

import tasklist.contract.Model
import java.time.LocalDate
import java.time.LocalTime

data class Task(override val priority: Model.Task.Priority,
                override val date: LocalDate,
                override val time: LocalTime,
                override val content: List<String>,
                override var status: Model.Task.Status
) : Model.Task {

}
