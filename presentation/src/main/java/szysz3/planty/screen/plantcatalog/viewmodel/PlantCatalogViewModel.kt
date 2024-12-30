package szysz3.planty.screen.plantcatalog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import szysz3.planty.core.model.Plant
import szysz3.planty.domain.usecase.PlantSearchUseCase
import szysz3.planty.screen.plantcatalog.model.PlantCatalogScreenState
import szysz3.planty.screen.plantcatalog.utils.PlantPagingSource
import javax.inject.Inject

@HiltViewModel
class PlantCatalogViewModel @Inject constructor(
    private val plantSearchUseCase: PlantSearchUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantCatalogScreenState())
    val uiState: StateFlow<PlantCatalogScreenState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagedPlants = searchQuery
        .debounce(500)
        .flatMapLatest { searchQuery ->
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { PlantPagingSource(plantSearchUseCase, searchQuery) }
            ).flow.cachedIn(viewModelScope)
        }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    fun selectPlant(plant: Plant) {
        _uiState.update { it.copy(selectedPlant = plant) }
    }
}