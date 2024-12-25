package szysz3.planty.screen.tasklist.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.screen.tasklist.model.Task
import szysz3.planty.screen.tasklist.model.TaskListScreenState
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListScreenState(tasks = mockInitialTasks()))
    val uiState: StateFlow<TaskListScreenState> = _uiState

    /**
     * Adds a new task to the list.
     */
    fun addTaskCard(task: Task) {
        _uiState.update { currentState ->
            currentState.copy(tasks = currentState.tasks + task)
        }
    }

    /**
     * Moves a task from one position to another in the list.
     */
    fun moveTask(fromIndex: Int, toIndex: Int) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.toMutableList()
            val task = updatedTasks.removeAt(fromIndex)
            updatedTasks.add(toIndex, task)
            currentState.copy(tasks = updatedTasks)
        }
    }

    /**
     * Deletes a task from the list.
     */
    fun deleteTask(task: Task) {
        _uiState.update { currentState ->
            currentState.copy(tasks = currentState.tasks.filterNot { it == task })
        }
    }

    /**
     * Updates the completion state of a task or its subtasks.
     */
    fun toggleSubTaskCompletion(task: Task, subTask: SubTask, isCompleted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                tasks = currentState.tasks.map { existingTask ->
                    if (existingTask == task) {
                        val updatedSubTasks = existingTask.tasks
                            .map { if (it == subTask) it.copy(isCompleted = isCompleted) else it }
                            .sortedWith(
                                compareBy(
                                    { it.isCompleted },
                                    { existingTask.tasks.indexOf(it) })
                            )
                        existingTask.copy(
                            tasks = updatedSubTasks,
                            isCompleted = updatedSubTasks.all { it.isCompleted }
                        )
                    } else {
                        existingTask
                    }
                }
            )
        }
    }

    /**
     * Updates a task's details (e.g., when edited in the modal).
     */
    fun updateTask(updatedTask: Task) {
        _uiState.update { currentState ->
            currentState.copy(
                tasks = currentState.tasks.map { task ->
                    if (task == updatedTask) updatedTask else task
                }
            )
        }
    }

    /**
     * Navigates to the Add Task screen (placeholder for actual navigation logic).
     */
    fun navigateToAddTaskScreen() {
        // Implement navigation logic here.
    }

    /**
     * Mocked initial tasks for testing or demonstration purposes.
     */
    private fun mockInitialTasks(): List<Task> {
        return listOf(
            Task(
                title = "Watering Plants",
                tasks = listOf(
                    SubTask(description = "Water the roses", isCompleted = false, cost = 5),
                    SubTask(description = "Water the tulips", isCompleted = true, cost = 3),
                    SubTask(description = "Water the pinus", isCompleted = true, cost = 0),
                    SubTask(description = "Water the maple", isCompleted = true, cost = 1),
                    SubTask(description = "Water the grass", isCompleted = true, cost = 2)

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
