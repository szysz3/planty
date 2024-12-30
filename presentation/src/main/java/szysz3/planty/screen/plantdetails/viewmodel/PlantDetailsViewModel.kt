package szysz3.planty.screen.plantdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.usecase.GetPlantUseCase
import szysz3.planty.screen.plantdetails.model.PlantDetailScreenState
import javax.inject.Inject

@HiltViewModel
class PlantDetailsViewModel @Inject constructor(
    private val getPlantUseCase: GetPlantUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantDetailScreenState())
    val uiState: StateFlow<PlantDetailScreenState> = _uiState

    fun updatePlantId(plantId: Int) {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    plantId = plantId,
                    selectedPlant = getPlantUseCase(plantId)?.toPresentationModel()
                )
            )
        }
    }
}