package szysz3.planty.screen.taskdetails.model

import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.screen.tasklist.model.Task

data class TaskDetailsScreenState(
    val task: Task = Task.empty(false),
    val activeSubTasks: List<SubTask> = emptyList(),
    val completedSubTasks: List<SubTask> = emptyList(),
    val completedSubTaskCost: Double = 0.0,
    val totalCost: Double = 0.0
)