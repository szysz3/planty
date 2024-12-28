package szysz3.planty.screen.tasklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.domain.usecase.task.ObserveTasksUseCase
import szysz3.planty.domain.usecase.task.UpdateTaskOrderUseCase
import szysz3.planty.domain.usecase.task.UpdateTaskOrderUseCaseParams
import szysz3.planty.domain.usecase.task.UpdateTasksUseCase
import szysz3.planty.screen.tasklist.model.TaskListScreenState
import szysz3.planty.screen.tasklist.model.toDomain
import szysz3.planty.screen.tasklist.model.toPresentation
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val observeTasksUseCase: ObserveTasksUseCase,
    private val updateTaskOrderUseCase: UpdateTaskOrderUseCase,
    private val updateTasksUseCase: UpdateTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListScreenState())
    val uiState: StateFlow<TaskListScreenState> = _uiState

    fun observeTasks() {
        viewModelScope.launch {
            observeTasksUseCase(NoParams())
                .distinctUntilChanged()
                .collect { tasks ->
                    _uiState.value = _uiState.value.copy(tasks = tasks.toPresentation())
                }
        }
    }

    fun onPersistTaskOrder() {
        viewModelScope.launch {
            updateTasksUseCase(_uiState.value.tasks.toDomain())
        }
    }

    fun moveTask(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            val updatedTasks = updateTaskOrderUseCase(
                UpdateTaskOrderUseCaseParams(
                    _uiState.value.tasks.toDomain(),
                    fromIndex,
                    toIndex
                )
            )

            _uiState.value = _uiState.value.copy(tasks = updatedTasks.toPresentation())
        }
    }
}
