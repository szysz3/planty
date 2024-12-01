package szysz3.planty.screen.plantid.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.CreateTempPhotoFileUseCase
import szysz3.planty.domain.usecase.DeleteTempPhotoFileUseCase
import szysz3.planty.domain.usecase.UploadPlantImageUseCase
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.screen.plantid.model.PlantIdUiState
import javax.inject.Inject

@HiltViewModel
class PlantIdViewModel @Inject constructor(
    private val uploadPlantImageUseCase: UploadPlantImageUseCase,
    private val createFileUseCase: CreateTempPhotoFileUseCase,
    private val deleteFileUseCase: DeleteTempPhotoFileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantIdUiState())
    val uiState: StateFlow<PlantIdUiState> = _uiState

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _photoUploaded = MutableStateFlow(false)
    val photoUploaded: StateFlow<Boolean> = _photoUploaded

    fun uploadPhoto(onComplete: (Uri?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val uploadedUri = _photoUri.value?.let { uploadPlantImageUseCase(it) }
            _isLoading.value = false
            _photoUploaded.value = true

            onComplete(uploadedUri)
        }
    }

    fun createPhotoFile() {
        viewModelScope.launch {
            _photoUploaded.value = false
            _photoUri.value = createFileUseCase(NoParams())
        }
    }

    fun deletePhotoFile() {
        viewModelScope.launch {
            val uri = _photoUri.value
            if (uri != null) {
                val deleted = deleteFileUseCase(uri)
                if (deleted) {
                    _photoUri.value = null
                }
            }
        }
    }
}