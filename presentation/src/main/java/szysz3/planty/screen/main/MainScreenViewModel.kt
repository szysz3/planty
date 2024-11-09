package szysz3.planty.screen.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() :
    ViewModel() {

    private val _isTopBarVisible = MutableStateFlow(false)
    val isTopBarVisible: StateFlow<Boolean> = _isTopBarVisible.asStateFlow()

    fun showTopBar(show: Boolean) {
        _isTopBarVisible.value = show
    }

    private val _isActionButtonVisible = MutableStateFlow(false)
    val isActionButtonVisible: StateFlow<Boolean> = _isActionButtonVisible.asStateFlow()

    fun showActionButton(show: Boolean) {
        _isActionButtonVisible.value = show
    }
}
