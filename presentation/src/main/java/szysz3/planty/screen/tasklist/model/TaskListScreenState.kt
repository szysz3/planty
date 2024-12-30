package szysz3.planty.screen.tasklist.model

import szysz3.planty.core.model.Task

data class TaskListScreenState(
    val tasks: List<Task> = emptyList()
)