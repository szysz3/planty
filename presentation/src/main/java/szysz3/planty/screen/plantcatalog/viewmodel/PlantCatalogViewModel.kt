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
import szysz3.planty.domain.usecase.plant.PlantSearchUseCase
import szysz3.planty.screen.plantcatalog.utils.PlantPagingSource
import szysz3.planty.screen.plantcatalog.utils.PlantPagingSource.Companion.DEFAULT_PAGE_SIZE
import javax.inject.Inject

/**
 * ViewModel responsible for managing plant catalog screen state and user interactions.
 * Handles plant search functionality with pagination support.
 *
 * @property plantSearchUseCase Use case for searching plants in the catalog
 */
@HiltViewModel
class PlantCatalogViewModel @Inject constructor(
    private val plantSearchUseCase: PlantSearchUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagedPlants = searchQuery
        .debounce(SEARCH_DEBOUNCE_MS)
        .flatMapLatest { searchQuery ->
            Pager(
                config = PagingConfig(
                    pageSize = DEFAULT_PAGE_SIZE,
                    enablePlaceholders = true
                ),
                pagingSourceFactory = { PlantPagingSource(plantSearchUseCase, searchQuery) }
            ).flow.cachedIn(viewModelScope)
        }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}