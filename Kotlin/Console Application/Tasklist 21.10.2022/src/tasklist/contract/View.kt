package tasklist.contract

import tasklist.view.Message

interface View {
    interface Task {
        var isActive: Boolean
        fun printMessage(msg: Message)
        fun printTasks(tasks: List<Model.Task>)
    }
}