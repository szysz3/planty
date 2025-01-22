package szysz3.planty.screen.plantid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
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

/**
 * ViewModel responsible for managing plant identification functionality.
 * Handles photo capture, plant identification API calls, and UI state management for the plant identification screen.
 *
 * @property identifyPlantUseCase Use case for plant identification functionality
 * @property createFileUseCase Use case for creating temporary photo files
 * @property deleteFileUseCase Use case for cleaning up temporary photo files
 */
@HiltViewModel
class PlantIdViewModel @Inject constructor(
    private val identifyPlantUseCase: IdentifyPlantUseCase,
    private val createFileUseCase: CreateTempPhotoFileUseCase,
    private val deleteFileUseCase: DeleteTempPhotoFileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantIdScreenState())
    val uiState: StateFlow<PlantIdScreenState> = _uiState

    /**
     * Initiates the plant identification process using the currently captured photo.
     * Updates the UI state with loading, success, or error states accordingly.
     * Automatically cleans up temporary photo files after the identification attempt.
     *
     * @throws IllegalArgumentException if no photo URI is available in the current state
     */
    fun identifyPlant() {
        val uri = _uiState.value.photoUri ?: return

        _uiState.update { it.copy(plantIdResult = PlantIdState.Loading) }

        viewModelScope.launch {
            try {
                val idParams = IdentifyPlantsParams(
                    apiKey = BuildConfig.API_KEY,
                    imageUris = listOf(uri)
                )

                identifyPlantUseCase(idParams)
                    .onEach { result ->
                        result.fold(
                            onSuccess = { response ->
                                _uiState.update { state ->
                                    state.copy(
                                        plantIdResult = PlantIdState.Success(
                                            response.toPresentationModel()
                                        ),
                                        photoUploaded = true
                                    )
                                }
                            },
                            onFailure = { error ->
                                _uiState.update { state ->
                                    state.copy(
                                        plantIdResult = PlantIdState.Error(
                                            "Error identifying plant: ${error.message}"
                                        ),
                                        photoUploaded = false
                                    )
                                }
                            }
                        )
                    }
                    .catch { error ->
                        Timber.e(error, "Error in plant identification flow")
                        _uiState.update { state ->
                            state.copy(
                                plantIdResult = PlantIdState.Error(
                                    "Unexpected error: ${error.message}"
                                ),
                                photoUploaded = false
                            )
                        }
                    }
                    .collect { }
            } finally {
                cleanupPhotoFile(uri)
            }
        }
    }

    /**
     * Creates a temporary file for storing the captured photo.
     * Updates the UI state with the new file URI and resets identification results.
     * Should be called before capturing a new photo.
     */
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

    /**
     * Resets the plant identification screen to its initial state.
     * Clears photo URI, identification results, and upload status.
     */
    fun clearResults() {
        _uiState.update { state ->
            state.copy(
                plantIdResult = PlantIdState.Idle,
                photoUri = null,
                photoUploaded = false
            )
        }
    }

    private fun cleanupPhotoFile(uri: android.net.Uri) {
        viewModelScope.launch {
            if (!deleteFileUseCase(uri)) {
                Timber.e("Error deleting file $uri")
            }
        }
    }
}