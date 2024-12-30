package szysz3.planty.screen.taskdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.core.model.SubTask
import szysz3.planty.core.model.Task
import szysz3.planty.core.model.toDomain
import szysz3.planty.core.model.toPresentation
import szysz3.planty.domain.usecase.task.AddTaskUseCase
import szysz3.planty.domain.usecase.task.GetTaskByIdUseCase
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: AddTaskUseCase
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
        val updatedTask = _uiState.value.task?.let { task ->
            val updatedSubTasks = task.tasks.map {
                if (it.id == subTaskId) it.copy(description = newDescription) else it
            }
            task.copy(tasks = updatedSubTasks)
        }
        _uiState.value = refreshState(updatedTask)
    }

    fun updateTaskTitle(newTitle: String) {
        val updatedTask = _uiState.value.task?.copy(title = newTitle)
        _uiState.value = refreshState(updatedTask)
    }

    fun toggleSubTaskCompletion(subTaskId: Long, isCompleted: Boolean) {
        val updatedTask = _uiState.value.task?.let { task ->
            val updatedSubTasks = task.tasks.map {
                if (it.id == subTaskId) it.copy(isCompleted = isCompleted) else it
            }
            task.copy(
                tasks = updatedSubTasks,
                isCompleted = updatedSubTasks.all { it.isCompleted })
        }
        _uiState.value = refreshState(updatedTask)
    }

    fun updateSubTaskCost(subTaskId: Long, cost: String) {
        val updatedTask = _uiState.value.task?.let { task ->
            val updatedSubTasks = task.tasks.map {
                val parsedCost = cost.toFloatOrNull() ?: 0f
                if (it.id == subTaskId) it.copy(cost = parsedCost) else it
            }
            task.copy(
                tasks = updatedSubTasks
            )
        }
        _uiState.value = refreshState(updatedTask)
    }

    fun addNewSubTask() {
        val updatedTask = _uiState.value.task?.let { task ->
            val newSubTask = SubTask(id = generateUniqueId(), description = "", isCompleted = false)
            task.copy(tasks = task.tasks + newSubTask)
        }
        _uiState.value = refreshState(updatedTask)
    }

    fun saveNewTask() {
        val taskToSave = _uiState.value.task ?: return
        viewModelScope.launch {
            addTaskUseCase(taskToSave.toDomain())
        }
    }

    fun updateTask() {
        val taskToUpdate = _uiState.value.task ?: return
        viewModelScope.launch {
            updateTaskUseCase(taskToUpdate.toDomain())
        }
    }

    private fun generateUniqueId(): Long {
        return System.currentTimeMillis()
    }

    private fun refreshState(task: Task?): TaskDetailsScreenState {
        val activeSubTasks = task?.tasks?.filter { !it.isCompleted }
        val completedSubTasks = task?.tasks?.filter { it.isCompleted }
        val completedSubTasksCost = completedSubTasks?.sumOf { it.cost.toDouble() }
        val totalCost = task?.tasks?.sumOf { it.cost.toDouble() }

        return _uiState.value.copy(
            task = task ?: Task.empty(false),
            activeSubTasks = activeSubTasks ?: emptyList(),
            completedSubTasks = completedSubTasks ?: emptyList(),
            completedSubTaskCost = completedSubTasksCost ?: 0.0,
            totalCost = totalCost ?: 0.0
        )
    }
}
