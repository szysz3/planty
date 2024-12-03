package szysz3.planty.screen.main.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import szysz3.planty.screen.main.composable.BottomNavItem
import szysz3.planty.screen.main.model.MainScreenState
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState

    fun updateHomeScreenInitialized(initialized: Boolean) {
        _uiState.update { it.copy(isHomeScreenInitialized = initialized) }
    }

    fun updateTopBarVisibility(visible: Boolean) {
        if (_uiState.value.isTopBarVisible != visible) {
            _uiState.update { it.copy(isTopBarVisible = visible) }
        }
    }

    fun updateShowBackButton(show: Boolean) {
        _uiState.update { it.copy(showBackButton = show) }
    }

    fun updateShowDeleteButton(show: Boolean) {
        _uiState.update { it.copy(showDeleteButton = show) }
    }

    fun handleTopBarVisibility(route: String) {
        val shouldShowTopBar =
            route == BottomNavItem.Home.route && _uiState.value.isHomeScreenInitialized
        updateTopBarVisibility(shouldShowTopBar)
    }
}