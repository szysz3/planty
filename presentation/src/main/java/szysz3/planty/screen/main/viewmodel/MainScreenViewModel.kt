package szysz3.planty.screen.main.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import szysz3.planty.screen.main.composable.BottomNavItem
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() :
    ViewModel() {

    private val _isHomeScreenInitialized = MutableStateFlow(false)
    private val isHomeScreenInitialized: StateFlow<Boolean> = _isHomeScreenInitialized.asStateFlow()

    private val _isTopBarVisible = MutableStateFlow(false)
    val isTopBarVisible: StateFlow<Boolean> = _isTopBarVisible.asStateFlow()

    private val _showBackButton = MutableStateFlow(false)
    val showBackButton: StateFlow<Boolean> = _showBackButton.asStateFlow()

    fun showBackButton(show: Boolean) {
        _showBackButton.value = show
    }

    fun homeScreenInitialized(initialized: Boolean) {
        _isHomeScreenInitialized.value = initialized
    }

    fun showTopBar(show: Boolean) {
        _isTopBarVisible.value = show
    }

    fun handleTopBarVisibility(route: String) {
        showTopBar(route == BottomNavItem.Home.route && isHomeScreenInitialized.value)
    }
}
