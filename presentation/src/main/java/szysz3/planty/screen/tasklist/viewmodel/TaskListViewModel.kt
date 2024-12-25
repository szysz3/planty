package szysz3.planty.screen.tasklist.viewmodel

import androidx.compose.ui.geometry.Offset
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

    fun addTaskCard(task: Task) {
        _uiState.update { it.copy(tasks = it.tasks + task) }
    }

    fun moveTask(fromIndex: Int, toIndex: Int) {
        val updatedTasks = _uiState.value.tasks.toMutableList()
        val task = updatedTasks.removeAt(fromIndex)
        updatedTasks.add(toIndex, task)
        _uiState.update { it.copy(tasks = updatedTasks) }
    }

    fun deleteTask(task: Task) {
        _uiState.update { it.copy(tasks = it.tasks.filterNot { it == task }) }
    }

    fun reorderTasks(draggedIndex: Int?, dragOffset: Offset) {
        if (draggedIndex == null) return
    }

    fun openTaskDetails(task: Task) {
    }

    fun navigateToAddTaskScreen() {
    }

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

    fun toggleTaskCompletion(task: Task, isCompleted: Boolean) {
        _uiState.update {
            it.copy(tasks = it.tasks.map { t ->
                if (t == task) t.copy(isCompleted = isCompleted) else t
            })
        }
    }

    private fun mockInitialTasks(): List<Task> {
        return listOf(
            Task(
                title = "Watering Plants",
                tasks = listOf(
                    SubTask(description = "Water the roses", isCompleted = false, cost = 5),
                    SubTask(description = "Water the tulips", isCompleted = true, cost = 3),
                    SubTask(description = "Water the pinus", isCompleted = false, cost = 0),
                    SubTask(description = "Water the maple", isCompleted = true, cost = 1)
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
                ),
                isCompleted = false
            )
        )
    }
}