package szysz3.planty.screen.taskdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.task.AddTaskUseCase
import szysz3.planty.domain.usecase.task.GetTaskByIdUseCase
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState
import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.screen.tasklist.model.Task
import szysz3.planty.screen.tasklist.model.toDomain
import szysz3.planty.screen.tasklist.model.toPresentation
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsScreenState())
    val uiState: StateFlow<TaskDetailsScreenState> = _uiState

    fun loadTask(taskId: Int?) {
        if (taskId == null) {
            _uiState.value = TaskDetailsScreenState(task = Task.empty())
        } else {
            viewModelScope.launch {
                val task = getTaskByIdUseCase(taskId)?.toPresentation()
                _uiState.value = TaskDetailsScreenState(task = task)
            }
        }
    }

    fun updateSubTaskDescription(subTaskIndex: Int, newDescription: String) {
        val updatedTask = _uiState.value.task?.let { task ->
            val updatedSubTasks = task.tasks.toMutableList().apply {
                this[subTaskIndex] = this[subTaskIndex].copy(description = newDescription)
            }
            task.copy(tasks = updatedSubTasks)
        }
        _uiState.value = _uiState.value.copy(task = updatedTask)
    }

    fun updateTaskTitle(newTitle: String) {
        val updatedTask = _uiState.value.task?.copy(title = newTitle)
        _uiState.value = _uiState.value.copy(task = updatedTask)
    }

    fun toggleSubTaskCompletion(subTaskIndex: Int, isCompleted: Boolean) {
        val updatedTask = _uiState.value.task?.let { task ->
            val updatedSubTasks = task.tasks.toMutableList().apply {
                this[subTaskIndex] = this[subTaskIndex].copy(isCompleted = isCompleted)
            }
            task.copy(tasks = updatedSubTasks)
        }
        _uiState.value = _uiState.value.copy(task = updatedTask)
    }

    fun addNewSubTask() {
        val updatedTask = _uiState.value.task?.let { task ->
            val updatedSubTasks = task.tasks + SubTask()
            task.copy(tasks = updatedSubTasks)
        }
        _uiState.value = _uiState.value.copy(task = updatedTask)
    }

    fun saveNewTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task.toDomain())
        }
    }
}
