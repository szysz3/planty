package szysz3.planty.screen.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import szysz3.planty.domain.usecase.SampleUseCase
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val sampleUseCase: SampleUseCase) :
    ViewModel() {

    private val _gardenDimensions = MutableStateFlow<MapDimensions?>(null)
    val gardenDimensions: StateFlow<MapDimensions?> = _gardenDimensions.asStateFlow()

    private val _isDeleteDialogVisible = MutableStateFlow(false)
    val isDeleteDialogVisible: StateFlow<Boolean> = _isDeleteDialogVisible.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    fun setGardenDimensions(dimensions: MapDimensions?) {
        _gardenDimensions.value = dimensions
    }

    fun showDeleteDialog(show: Boolean) {
        _isDeleteDialogVisible.value = show
    }

    fun showBottomSheet(show: Boolean) {
        _isBottomSheetVisible.value = show
    }
}
