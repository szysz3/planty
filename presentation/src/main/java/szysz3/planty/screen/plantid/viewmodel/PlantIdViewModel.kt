package szysz3.planty.screen.plantid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.BuildConfig
import szysz3.planty.domain.usecase.CreateTempPhotoFileUseCase
import szysz3.planty.domain.usecase.DeleteTempPhotoFileUseCase
import szysz3.planty.domain.usecase.IdentifyPlantUseCase
import szysz3.planty.domain.usecase.IdentifyPlantsParams
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.screen.plantid.model.PlantIdScreenState
import javax.inject.Inject

@HiltViewModel
class PlantIdViewModel @Inject constructor(
    private val identifyPlantUseCase: IdentifyPlantUseCase,
    private val createFileUseCase: CreateTempPhotoFileUseCase,
    private val deleteFileUseCase: DeleteTempPhotoFileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantIdScreenState())
    val uiState: StateFlow<PlantIdScreenState> = _uiState

    fun identifyPlant() {
        viewModelScope.launch {
            val uri = _uiState.value.photoUri ?: return@launch
            _uiState.value = _uiState.value.copy(isLoading = true)

            val idParams = IdentifyPlantsParams(
                apiKey = BuildConfig.API_KEY,
                imageUris = listOf(uri)
            )

            val result = identifyPlantUseCase(idParams)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                photoUploaded = true,
                identifiedPlant = result?.bestMatch ?: "Not recognized"
            )
        }
    }

    fun createPhotoFile() {
        viewModelScope.launch {
            val uri = createFileUseCase(NoParams())
            _uiState.value = _uiState.value.copy(
                photoUri = uri,
                photoUploaded = false,
                identifiedPlant = ""
            )
        }
    }

    fun deletePhotoFile() {
        viewModelScope.launch {
            val uri = _uiState.value.photoUri ?: return@launch
            if (deleteFileUseCase(uri)) {
                _uiState.value = _uiState.value.copy(
                    photoUri = null,
                    photoUploaded = false,
                    identifiedPlant = ""
                )
            }
        }
    }
}