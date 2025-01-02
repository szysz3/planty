package szysz3.planty.screen.taskdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import szysz3.planty.core.model.SubTask
import szysz3.planty.core.model.Task
import szysz3.planty.core.model.toDomain
import szysz3.planty.core.model.toPresentation
import szysz3.planty.domain.usecase.task.DeleteTaskUseCase
import szysz3.planty.domain.usecase.task.GetTaskByIdUseCase
import szysz3.planty.domain.usecase.task.SaveTaskUseCase
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsScreenState())
    val uiState: StateFlow<TaskDetailsScreenState> = _uiState

    private var isDarkMode: Boolean = false

    fun loadTask(taskId: Long?) {
        if (taskId == null) {
            _uiState.value = TaskDetailsScreenState(task = Task.empty(isDarkMode))
        } else {
            viewModelScope.launch {
                val task = getTaskByIdUseCase(taskId)?.toPresentation()
                _uiState.value = refreshState(task)
            }
        }
    }

    fun updateTheme(isDarkMode: Boolean) {
        this.isDarkMode = isDarkMode
    }

    fun updateSubTaskDescription(subTaskId: Long, newDescription: String) {
        val updatedTask = _uiState.value.task.let { task ->
            val updatedSubTasks = task.subTasks.map {
                if (it.id == subTaskId) it.copy(description = newDescription) else it
            }
            task.copy(subTasks = updatedSubTasks)
        }
        _uiState.value = refreshState(updatedTask)
    }

    fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase.invoke(_uiState.value.task.toDomain())
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { it.copy(isDeleteDialogVisible = show) }
    }

    fun updateTaskTitle(newTitle: String) {
        val updatedTask = _uiState.value.task.copy(title = newTitle)
        _uiState.value = refreshState(updatedTask)
    }

    fun toggleSubTaskCompletion(subTaskId: Long, isCompleted: Boolean) {
        val updatedTask = _uiState.value.task.let { task ->
            val updatedSubTasks = task.subTasks.map {
                if (it.id == subTaskId) it.copy(isCompleted = isCompleted) else it
            }
            task.copy(
                subTasks = updatedSubTasks,
                isCompleted = updatedSubTasks.all { it.isCompleted })
        }
        _uiState.value = refreshState(updatedTask)
    }

    fun updateSubTaskCost(subTaskId: Long, cost: Float) {
        val updatedTask = _uiState.value.task.let { task ->
            val updatedSubTasks = task.subTasks.map {
                if (it.id == subTaskId) it.copy(cost = cost) else it
            }
            task.copy(
                subTasks = updatedSubTasks
            )
        }
        _uiState.value = refreshState(updatedTask)
    }

    fun addNewSubTask() {
        val currentTask = _uiState.value.task
        val lastSubTask = currentTask.subTasks.lastOrNull()
        if (lastSubTask != null && lastSubTask.description.isBlank()) {
            return
        }

        val newSubTask = SubTask(
            id = generateUniqueId(),
            description = "",
            isCompleted = false
        )
        val updatedTask = currentTask.copy(subTasks = currentTask.subTasks + newSubTask)
        _uiState.value = refreshState(updatedTask)
    }

    fun saveNewTask() {
        val taskToSave = _uiState.value.task
        viewModelScope.launch {
            saveTaskUseCase(taskToSave.toDomain())
        }
    }

    fun updateTask() {
        val taskToUpdate = _uiState.value.task
        viewModelScope.launch {
            saveTaskUseCase(taskToUpdate.toDomain())
        }
    }

    private fun generateUniqueId(): Long {
        return System.currentTimeMillis()
    }

    private fun refreshState(task: Task?): TaskDetailsScreenState {
        val activeSubTasks = task?.subTasks?.filter { !it.isCompleted }
        val completedSubTasks = task?.subTasks?.filter { it.isCompleted }
        val completedSubTasksCost = completedSubTasks?.sumOf { it.cost?.toDouble() ?: 0.0 }
        val totalCost = task?.subTasks?.sumOf { it.cost?.toDouble() ?: 0.0 }

        return _uiState.value.copy(
            task = task ?: Task.empty(false),
            activeSubTasks = activeSubTasks ?: emptyList(),
            completedSubTasks = completedSubTasks ?: emptyList(),
            completedSubTaskCost = completedSubTasksCost ?: 0.0,
            totalCost = totalCost ?: 0.0
        )
    }
}
