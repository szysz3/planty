package szysz3.planty.screen.tasklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.domain.usecase.task.GetAllTasksUseCase
import szysz3.planty.domain.usecase.task.UpdateTaskOrderUseCase
import szysz3.planty.screen.tasklist.model.TaskListScreenState
import szysz3.planty.screen.tasklist.model.toDomain
import szysz3.planty.screen.tasklist.model.toPresentation
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetAllTasksUseCase,
    private val updateTaskOrderUseCase: UpdateTaskOrderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListScreenState())
    val uiState: StateFlow<TaskListScreenState> = _uiState

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value =
                TaskListScreenState(tasks = getTasksUseCase(NoParams()).toPresentation())
        }
    }

    fun moveTask(fromIndex: Int, toIndex: Int) {
        val currentTasks = _uiState.value.tasks
        val updatedTasks = currentTasks.toMutableList().apply {
            val task = removeAt(fromIndex)
            add(toIndex, task)
        }
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)

        // Persist the updated order
        viewModelScope.launch {
            updateTaskOrderUseCase(updatedTasks.toDomain())
        }
    }
}
