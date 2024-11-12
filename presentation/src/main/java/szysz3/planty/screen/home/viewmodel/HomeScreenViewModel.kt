package szysz3.planty.screen.home.viewmodel

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
import szysz3.planty.screen.home.model.GardenState
import szysz3.planty.screen.home.model.MapDimensions
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val saveCellUseCase: SaveGardenCellUseCase,
    private val loadGardenUseCase: LoadGardenCellUseCase,
    private val clearGardenUseCase: ClearGardenUseCase
) : ViewModel() {

    private val _gardenState = MutableStateFlow(GardenState(emptyList(), MapDimensions(0, 0)))
    val gardenState: StateFlow<GardenState> = _gardenState.asStateFlow()

    private val _isDeleteDialogVisible = MutableStateFlow(false)
    val isDeleteDialogVisible: StateFlow<Boolean> = _isDeleteDialogVisible.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    private val _dataLoaded = MutableStateFlow(false)
    val dataLoaded: StateFlow<Boolean> = _dataLoaded.asStateFlow()

    fun showDeleteDialog(show: Boolean) {
        _isDeleteDialogVisible.value = show
    }

    fun showBottomSheet(show: Boolean) {
        _isBottomSheetVisible.value = show
    }

    fun initializeGarden(dimensions: MapDimensions) {
        _gardenState.value = GardenState(emptyList(), dimensions)
        _dataLoaded.value = true
    }

    fun saveCell(row: Int, column: Int, plant: String) {
        viewModelScope.launch {
            try {
                val newCell = GardenCell(row, column, plant)
                saveCellUseCase(newCell)
                updateGardenStateWithNewCell(newCell)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun updateGardenStateWithNewCell(newCell: GardenCell) {
        _gardenState.update { current ->
            val updatedCells = (current.cells ?: emptyList()) + newCell
            GardenState(updatedCells, calculateDimensions(updatedCells))
        }
    }

    suspend fun loadGarden() {
        viewModelScope.launch {
            try {
                val loadedCells = loadGardenUseCase(NoParams())
                _gardenState.value = GardenState(loadedCells, calculateDimensions(loadedCells))
                _dataLoaded.value = true
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun calculateDimensions(cells: List<GardenCell>?): MapDimensions? {
        if (cells.isNullOrEmpty()) return null
        val maxRow = cells.maxOfOrNull { it.row } ?: 0
        val maxColumn = cells.maxOfOrNull { it.column } ?: 0
        return MapDimensions(maxRow + 1, maxColumn + 1)
    }

    fun clearGarden() {
        viewModelScope.launch {
            try {
                clearGardenUseCase(NoParams())
                _gardenState.value = GardenState(null, null)
                _dataLoaded.value = false
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
