package szysz3.planty.screen.plantaplant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.GetPlantsFromRangeParams
import szysz3.planty.domain.usecase.GetPlantsFromRangeUseCase
import szysz3.planty.domain.usecase.PlantSearchUseCase
import szysz3.planty.screen.plantaplant.model.Plant
import szysz3.planty.screen.plantaplant.model.PlantAPlantScreenState
import toPresentationModel
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PlantAPlantViewModel @Inject constructor(
    private val plantSearchUseCase: PlantSearchUseCase,
    private val getPlantFromRangeUseCase: GetPlantsFromRangeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantAPlantScreenState())
    val uiState: StateFlow<PlantAPlantScreenState> = _uiState

    private val _searchQuery = MutableStateFlow("")

    init {
        observeSearchQuery()
        loadInitialData()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L) // Apply debounce to the search query flow
                .collect { query ->
                    if (query.isEmpty()) {
                        getPlantsFromRange()
                    } else {
                        getPlantsFilteredByQuery(query)
                    }
                }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(dataLoaded = false) }
            getPlantsFromRange()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun getPlantsFromRange(startRange: Int = 0, endRange: Int = 9) {
        viewModelScope.launch {
            val plants =
                getPlantFromRangeUseCase.invoke(GetPlantsFromRangeParams(startRange, endRange))
            _uiState.update {
                it.copy(
                    plants = plants.toPresentationModel(),
                    dataLoaded = true
                )
            }
        }
    }

    fun selectPlant(plant: Plant) {
        _uiState.update { it.copy(selectedPlant = plant) }
    }

    private fun getPlantsFilteredByQuery(query: String) {
        viewModelScope.launch {
            val plants = plantSearchUseCase.invoke(query).toPresentationModel()
            _uiState.update { it.copy(plants = plants) }
        }
    }
}