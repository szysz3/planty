package szysz3.planty.screen.taskdetails.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(TaskDetailsScreenState())
    val uiState: StateFlow<TaskDetailsScreenState> = _uiState
}