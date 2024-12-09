package szysz3.planty.screen.plantaplant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import szysz3.planty.domain.usecase.PlantSearchUseCase
import szysz3.planty.screen.plantaplant.model.Plant
import szysz3.planty.screen.plantaplant.model.PlantAPlantScreenState
import szysz3.planty.screen.plantaplant.paging.PlantPagingSource
import javax.inject.Inject

@HiltViewModel
class PlantAPlantViewModel @Inject constructor(
    private val plantSearchUseCase: PlantSearchUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantAPlantScreenState())
    val uiState: StateFlow<PlantAPlantScreenState> = _uiState

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedPlants = _searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 100, enablePlaceholders = false),
            pagingSourceFactory = { PlantPagingSource(plantSearchUseCase, query) }
        ).flow.cachedIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectPlant(plant: Plant) {
        _uiState.update { it.copy(selectedPlant = plant) }
    }
}