package szysz3.planty.screen.tasklist.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.screen.tasklist.model.Task
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor() : ViewModel() {

    private val _tasks =
        MutableStateFlow<List<Task>>(listOf(mockTasks(), mockTasks(), mockTasks()).flatten())
    val tasks: StateFlow<List<Task>> = _tasks

    val totalCost: Int
        get() = _tasks.value.filterNot { it.isCompleted }
            .flatMap { it.tasks }
            .filterNot { it.isCompleted }
            .sumOf { it.cost }

    /**
     * Adds a new task card to the list.
     */
    fun addTaskCard(task: Task) {
        _tasks.update { currentTasks ->
            currentTasks + task
        }
    }

    /**
     * Deletes a task card from the list.
     */
    fun deleteTask(task: Task) {
        _tasks.update { currentTasks ->
            currentTasks.filterNot { it == task }
        }
    }

    /**
     * Reorders tasks in the list.
     */
    fun reorderTasks(draggedIndex: Int?, dragOffset: Offset) {
        if (draggedIndex == null) return
        // Logic for handling reordering based on dragOffset (to be implemented).
    }

    /**
     * Opens the task details for editing.
     */
    fun openTaskDetails(task: Task) {
        // Handle navigation to task details or modal popup.
    }

    /**
     * Navigates to the Add Task Screen.
     */
    fun navigateToAddTaskScreen() {
        // Handle navigation logic.
    }

    /**
     * Toggles the completion status of a task.
     */
    fun toggleTaskCompletion(task: Task, isCompleted: Boolean) {
        _tasks.update { currentTasks ->
            currentTasks.map {
                if (it == task) it.copy(isCompleted = isCompleted) else it
            }
        }
    }

    /**
     * Generates mocked data for initial task list.
     */
    private fun mockTasks(): List<Task> {
        return listOf(
            Task(
                title = "Watering Plants",
                tasks = listOf(
                    SubTask(description = "Water the roses", isCompleted = false, cost = 5),
                    SubTask(description = "Water the tulips", isCompleted = true, cost = 3)
                ),
                isCompleted = false
            ),
            Task(
                title = "Fertilizing",
                tasks = listOf(
                    SubTask(
                        description = "Apply fertilizer to lawn",
                        isCompleted = false,
                        cost = 20
                    ),
                    SubTask(description = "Fertilize potted plants", isCompleted = false, cost = 10)
                ),
                isCompleted = false
            ),
            Task(
                title = "Weeding",
                tasks = listOf(
                    SubTask(
                        description = "Remove weeds from vegetable patch",
                        isCompleted = true,
                        cost = 0
                    ),
                    SubTask(
                        description = "Remove weeds from flower beds",
                        isCompleted = false,
                        cost = 15
                    )
                ),
                isCompleted = false
            )
        )
    }
}