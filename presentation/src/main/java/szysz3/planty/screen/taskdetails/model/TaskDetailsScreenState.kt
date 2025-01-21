package szysz3.planty.screen.taskdetails.model

import szysz3.planty.core.model.SubTask
import szysz3.planty.core.model.Task

/**
 * Represents the UI state for the Task Details screen.
 *
 * This class holds all the necessary information to display a task's details,
 * including its subtasks, associated costs, and UI state for dialogs.
 *
 * @property task The main task being displayed, defaults to an empty task
 * @property taskId The unique identifier of the task, can be null for new tasks
 * @property activeSubTasks List of uncompleted subtasks
 * @property completedSubTasks List of completed subtasks
 * @property completedSubTaskCost Total cost of all completed subtasks
 * @property totalCost Total cost of all subtasks (both active and completed)
 * @property isDeleteDialogVisible Controls the visibility of the delete confirmation dialog
 */
data class TaskDetailsScreenState(
    val task: Task = Task.empty(false),
    val taskId: Long? = null,
    val activeSubTasks: List<SubTask> = emptyList(),
    val completedSubTasks: List<SubTask> = emptyList(),
    val completedSubTaskCost: Double = 0.0,
    val totalCost: Double = 0.0,
    val isDeleteDialogVisible: Boolean = false
)