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

    fun updateTheme(isDarkMode: Boolean) {
        this.isDarkMode = isDarkMode
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { state -> state.copy(isDeleteDialogVisible = show) }
    }

    fun loadTask(taskId: Long?) {
        viewModelScope.launch {
            val task = taskId?.let { getTaskByIdUseCase(taskId)?.toPresentation() }
                ?: Task.empty(isDarkMode)

            _uiState.update { state ->
                refreshState(state, task)
            }
        }
    }

    fun addNewSubTask() {
        _uiState.update { state ->
            val currentTask = state.task
            val lastSubTask = currentTask.subTasks.lastOrNull()
            if (lastSubTask?.description?.isBlank() == true) return@update state

            val newSubTask = SubTask(id = generateUniqueId())
            val updatedTask = currentTask.copy(subTasks = currentTask.subTasks + newSubTask)
            refreshState(state, updatedTask)
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase.invoke(_uiState.value.task.toDomain())
        }
    }

    fun updateTaskTitle(newTitle: String) {
        _uiState.update { state ->
            val updatedTask = state.task.copy(title = newTitle)
            refreshState(state, updatedTask)
        }
    }

    fun saveNewTask() {
        val taskToSave = _uiState.value.task
        viewModelScope.launch {
            saveTaskUseCase(taskToSave.toDomain())
        }
    }

    fun updateSubTaskDescription(subTaskId: Long, newDescription: String) {
        updateSubTask(subTaskId) { state -> state.copy(description = newDescription) }
    }

    fun toggleSubTaskCompletion(subTaskId: Long, isCompleted: Boolean) {
        updateSubTask(subTaskId) { state -> state.copy(isCompleted = isCompleted) }
    }

    fun updateSubTaskCost(subTaskId: Long, cost: Float) {
        updateSubTask(subTaskId) { state -> state.copy(cost = cost) }
    }

    private fun updateSubTask(
        subTaskId: Long,
        transform: (SubTask) -> SubTask
    ) {
        _uiState.update { state ->
            val currentTask = state.task
            val updatedSubTasks = currentTask.subTasks.map {
                if (it.id == subTaskId) transform(it) else it
            }
            val updatedTask = currentTask.copy(
                subTasks = updatedSubTasks,
                isCompleted = updatedSubTasks.all { it.isCompleted }
            )
            refreshState(state, updatedTask)
        }
    }

    private fun refreshState(
        currentState: TaskDetailsScreenState,
        task: Task?
    ): TaskDetailsScreenState {
        val safeTask = task ?: Task.empty(isDarkMode)
        val activeSubTasks = safeTask.subTasks.filterNot { it.isCompleted }
        val completedSubTasks = safeTask.subTasks.filter { it.isCompleted }
        val completedSubTaskCost = completedSubTasks.sumOf { it.cost?.toDouble() ?: 0.0 }
        val totalCost = safeTask.subTasks.sumOf { it.cost?.toDouble() ?: 0.0 }

        return currentState.copy(
            task = safeTask,
            activeSubTasks = activeSubTasks,
            completedSubTasks = completedSubTasks,
            completedSubTaskCost = completedSubTaskCost,
            totalCost = totalCost
        )
    }

    private fun generateUniqueId(): Long {
        return System.currentTimeMillis()
    }
}
