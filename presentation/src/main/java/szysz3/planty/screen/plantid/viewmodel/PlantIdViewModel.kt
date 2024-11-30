package szysz3.planty.screen.plantid.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.UploadPlantImageUseCase
import szysz3.planty.screen.plantid.model.PlantIdUiState
import javax.inject.Inject

@HiltViewModel
class PlantIdViewModel @Inject constructor(
    private val uploadPlantImageUseCase: UploadPlantImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantIdUiState())
    val uiState: StateFlow<PlantIdUiState> = _uiState

    fun uploadPhoto(bitmap: Bitmap, onComplete: (Uri?) -> Unit) {
        viewModelScope.launch {
            val uploadedUri = uploadPlantImageUseCase(bitmap)
            onComplete(uploadedUri)
        }
    }
}