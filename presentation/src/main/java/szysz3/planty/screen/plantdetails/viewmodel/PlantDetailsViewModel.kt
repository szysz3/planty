package szysz3.planty.screen.plantdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import szysz3.planty.core.model.Plant
import szysz3.planty.core.model.PlantDetailsConfig
import szysz3.planty.core.model.toDomain
import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.usecase.garden.SaveGardenCellUseCase
import szysz3.planty.domain.usecase.garden.SaveGardenCellUseCaseParams
import szysz3.planty.domain.usecase.plant.GetPlantUseCase
import szysz3.planty.screen.plantdetails.model.PlantDetailScreenState
import javax.inject.Inject

@HiltViewModel
class PlantDetailsViewModel @Inject constructor(
    private val getPlantUseCase: GetPlantUseCase,
    private val saveGardenCellUseCase: SaveGardenCellUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantDetailScreenState())
    val uiState: StateFlow<PlantDetailScreenState> = _uiState

    fun initialize(
        config: PlantDetailsConfig,
        plantId: Int,
        row: Int?,
        column: Int?,
        gardenId: Int
    ) {
        viewModelScope.launch {
            val plant = getPlantUseCase(plantId)
            _uiState.update { state ->
                state.copy(
                    plantId = plantId,
                    selectedPlant = plant?.toPresentationModel(),
                    row = row,
                    column = column,
                    isDeleteButtonVisible = config == PlantDetailsConfig.DELETE,
                    isPlantButtonVisible = config == PlantDetailsConfig.PLANT,
                    gardenId = gardenId
                )
            }
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { it.copy(isDeleteDialogVisible = show) }
    }

    fun persistPlant(plant: Plant?) {
        viewModelScope.launch {
            val row = _uiState.value.row
            val column = _uiState.value.column
            val gardenId = _uiState.value.gardenId
            if (row == null || column == null) return@launch
            saveGardenCellUseCase(
                SaveGardenCellUseCaseParams(
                    row = row,
                    column = column,
                    plant = plant?.toDomain(),
                    gardenId = gardenId
                )
            )
        }
    }
}