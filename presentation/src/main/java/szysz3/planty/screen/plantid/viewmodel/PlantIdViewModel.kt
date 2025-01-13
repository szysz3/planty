package szysz3.planty.screen.plantid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import szysz3.planty.BuildConfig
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.domain.usecase.photo.CreateTempPhotoFileUseCase
import szysz3.planty.domain.usecase.photo.DeleteTempPhotoFileUseCase
import szysz3.planty.domain.usecase.plant.IdentifyPlantUseCase
import szysz3.planty.domain.usecase.plant.IdentifyPlantsParams
import szysz3.planty.screen.plantid.model.PlantIdScreenState
import szysz3.planty.screen.plantid.model.PlantIdState
import szysz3.planty.screen.plantid.model.toPresentationModel
import timber.log.Timber
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

            _uiState.update {
                it.copy(plantIdResult = PlantIdState.Loading)
            }

            val idParams = IdentifyPlantsParams(
                apiKey = BuildConfig.API_KEY,
                imageUris = listOf(uri)
            )

            try {
                val result = identifyPlantUseCase(idParams)
                val plants = result.toPresentationModel()

                _uiState.update { state ->
                    state.copy(
                        plantIdResult = PlantIdState.Success(plants),
                        photoUploaded = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        plantIdResult = PlantIdState.Error("Error identifying plant: ${e.message}"),
                        photoUploaded = false
                    )
                }
            }

            if (!deleteFileUseCase(uri)) {
                Timber.e("Error deleting file $uri")
            }
        }
    }

    fun createPhotoFile() {
        viewModelScope.launch {
            val uri = createFileUseCase(NoParams())
            _uiState.update { state ->
                state.copy(
                    photoUri = uri,
                    plantIdResult = PlantIdState.Idle,
                    photoUploaded = false
                )
            }
        }
    }

    fun clearResults() {
        _uiState.update { state ->
            state.copy(
                plantIdResult = PlantIdState.Idle,
                photoUri = null,
                photoUploaded = false
            )
        }
    }
}