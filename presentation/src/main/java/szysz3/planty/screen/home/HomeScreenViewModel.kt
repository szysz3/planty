package szysz3.planty.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.usecase.ClearGardenUseCase
import szysz3.planty.domain.usecase.LoadGardenCellUseCase
import szysz3.planty.domain.usecase.SaveGardenCellUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val saveCellUseCase: SaveGardenCellUseCase,
    private val loadGardenUseCase: LoadGardenCellUseCase,
    private val clearGardenUseCase: ClearGardenUseCase
) :
    ViewModel() {

    private val _gardenDimensions = MutableStateFlow<MapDimensions?>(null)
    val gardenDimensions: StateFlow<MapDimensions?> = _gardenDimensions.asStateFlow()

    private val _isDeleteDialogVisible = MutableStateFlow(false)
    val isDeleteDialogVisible: StateFlow<Boolean> = _isDeleteDialogVisible.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    private val _selectedCells = MutableStateFlow<Map<Pair<Int, Int>, String>>(emptyMap())
    val selectedCells: StateFlow<Map<Pair<Int, Int>, String>> = _selectedCells.asStateFlow()

    fun setGardenDimensions(dimensions: MapDimensions?) {
        _gardenDimensions.value = dimensions
    }

    fun showDeleteDialog(show: Boolean) {
        _isDeleteDialogVisible.value = show
    }

    fun showBottomSheet(show: Boolean) {
        _isBottomSheetVisible.value = show
    }

    fun saveCell(row: Int, column: Int, plant: String) {
        viewModelScope.launch {
            saveCellUseCase(GardenCell(row, column, plant))
            _selectedCells.update { it + (Pair(row, column) to plant) }
        }
    }

    suspend fun loadGarden() {
        viewModelScope.launch {
            val loadedCells = loadGardenUseCase(NoParams())
            _selectedCells.update {
                loadedCells.associate { Pair(it.row, it.column) to it.plant }
            }
            calculateGardenDimensions()
        }
    }

    private fun calculateGardenDimensions() {
        val maxRow = _selectedCells.value.keys.maxOfOrNull { it.first } ?: 0
        val maxColumn = _selectedCells.value.keys.maxOfOrNull { it.second } ?: 0
        _gardenDimensions.value =
            MapDimensions(maxRow + 1, maxColumn + 1) // Add 1 for zero-based index
    }

    fun clearGarden() {
        viewModelScope.launch {
            clearGardenUseCase(NoParams())
        }
    }
}
