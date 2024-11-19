package szysz3.planty.screen.plantaplant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.GetPlantsFromRangeParams
import szysz3.planty.domain.usecase.GetPlantsFromRangeUseCase
import szysz3.planty.domain.usecase.PlantSearchUseCase
import szysz3.planty.screen.plantaplant.model.Plant
import toPresentationModel
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PlantAPlantViewModel @Inject constructor(
    private val plantSearchUseCase: PlantSearchUseCase,
    private val getPlantFromRangeUseCase: GetPlantsFromRangeUseCase,
) : ViewModel() {

    private val _selectedPlant = MutableStateFlow<Plant?>(null)
    val selectedPlant: StateFlow<Plant?> = _selectedPlant

    private val _plants = MutableStateFlow(emptyList<Plant>())
    val plants: StateFlow<List<Plant>> = _plants

    private val _dataLoaded = MutableStateFlow(false)
    val dataLoaded: StateFlow<Boolean> = _dataLoaded.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQuery.debounce(300L).collect { query ->
                if (query.isEmpty()) {
                    getPlantsFromRange()
                } else {
                    getPlantsFilteredByQuery(query)
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getPlantsFromRange(startRange: Int = 0, endRange: Int = 9) {
        viewModelScope.launch {
            val plants =
                getPlantFromRangeUseCase.invoke(GetPlantsFromRangeParams(startRange, endRange))
            _plants.value = plants.toPresentationModel()
            _dataLoaded.value = true
        }
    }

    fun selectPlant(plant: Plant) {
        _selectedPlant.value = plant
    }

    private fun getPlantsFilteredByQuery(query: String) {
        viewModelScope.launch {
            _plants.value = plantSearchUseCase.invoke(query).toPresentationModel()
        }
    }
}