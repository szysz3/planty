package szysz3.planty.screen.imagegallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.usecase.plant.GetPlantUseCase
import szysz3.planty.screen.imagegallery.model.ImageGalleryScreenState
import javax.inject.Inject

@HiltViewModel
class ImageGalleryViewModel @Inject constructor(
    private val getPlantUseCase: GetPlantUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageGalleryScreenState())
    val uiState: StateFlow<ImageGalleryScreenState> = _uiState

    fun updateImageUrls(plantId: Int) {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    imageUrls = getPlantUseCase(plantId)?.toPresentationModel()?.imageUrls
                )
            )
        }
    }

}