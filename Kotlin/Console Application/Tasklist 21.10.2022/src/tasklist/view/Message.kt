package tasklist.view

enum class Message(val value: String) {
    INPUT_COMMAND("Input an action (add, print, edit, delete, end):"),
    INPUT_PRIORITY("Input the task priority (C, H, N, L):"),
    INPUT_DATE("Input the date (yyyy-mm-dd):"),
    INPUT_TIME("Input the time (hh:mm):"),
    INPUT_CONTENT("Input a new task (enter a blank line to end):"),
    INPUT_TASK_NUMBER("Input the task number (1-%d):"),
    INPUT_FIELD_TO_EDIT("Input a field to edit (priority, date, time, task):"),
    INVALID_INPUT("The input action is invalid"),
    INVALID_DATE("The input date is invalid"),
    INVALID_TIME("The input time is invalid"),
    INVALID_TASK_NUMBER("Invalid task number"),
    INVALID_FIELD("Invalid field"),
    TASK_IS_DELETED("The task is deleted"),
    TASK_IS_CHANGED("The task is changed"),
    NO_TASKS("No tasks have been input"),
    BLANK_TASK("The task is blank"),
    EXIT_PROGRAM("Tasklist exiting!")
}