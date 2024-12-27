package szysz3.planty.screen.taskdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.domain.usecase.task.AddTaskUseCase
import szysz3.planty.domain.usecase.task.ObserveTasksUseCase
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState
import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.screen.tasklist.model.Task
import szysz3.planty.screen.tasklist.model.toDomain
import szysz3.planty.screen.tasklist.model.toPresentation
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val getTasksUseCase: ObserveTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsScreenState())
    val uiState: StateFlow<TaskDetailsScreenState> = _uiState

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            getTasksUseCase(NoParams()).collect { tasks ->
                _uiState.value = TaskDetailsScreenState(tasks = tasks.toPresentation())
            }
        }
    }

    fun updateTaskTitle(taskId: Long, newTitle: String) {
        val updatedTasks = _uiState.value.tasks.map { task ->
            if (task.id == taskId) task.copy(title = newTitle) else task
        }
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)
        saveTask(updatedTasks.first { it.id == taskId })
    }

    fun toggleSubTaskCompletion(taskId: Long, subTaskIndex: Int, isCompleted: Boolean) {
        val updatedTasks = _uiState.value.tasks.map { task ->
            if (task.id == taskId) {
                val updatedSubTasks = task.tasks.toMutableList().apply {
                    this[subTaskIndex] = this[subTaskIndex].copy(isCompleted = isCompleted)
                }
                task.copy(tasks = updatedSubTasks)
            } else task
        }
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)
        saveTask(updatedTasks.first { it.id == taskId })
    }

    fun addNewSubTask(taskId: Long) {
        val updatedTasks = _uiState.value.tasks.map { task ->
            if (task.id == taskId) {
                val updatedSubTasks = task.tasks + SubTask(description = "New SubTask")
                task.copy(tasks = updatedSubTasks)
            } else task
        }
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)
        saveTask(updatedTasks.first { it.id == taskId })
    }

    private fun saveTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task.toDomain())
        }
    }
}
