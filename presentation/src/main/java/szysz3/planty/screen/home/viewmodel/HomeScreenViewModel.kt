package szysz3.planty.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.ClearGardenUseCase
import szysz3.planty.domain.usecase.LoadGardenStateUseCase
import szysz3.planty.domain.usecase.SaveGardenStateUseCase
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.screen.home.model.GardenCell
import szysz3.planty.screen.home.model.GardenState
import szysz3.planty.screen.plantaplant.model.Plant
import timber.log.Timber
import toDomainModel
import toPresentationModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val saveGardenStateUseCase: SaveGardenStateUseCase,
    private val loadGardenStateUseCase: LoadGardenStateUseCase,
    private val clearGardenUseCase: ClearGardenUseCase
) : ViewModel() {

    private val _gardenState = MutableStateFlow(GardenState())
    val gardenState: StateFlow<GardenState> = _gardenState

    private val _isDeleteDialogVisible = MutableStateFlow(false)
    val isDeleteDialogVisible: StateFlow<Boolean> = _isDeleteDialogVisible.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    private val _dataLoaded = MutableStateFlow(false)
    val dataLoaded: StateFlow<Boolean> = _dataLoaded.asStateFlow()

    private val _selectedCell = MutableStateFlow<Pair<Int, Int>?>(null)
    val selectedCell: StateFlow<Pair<Int, Int>?> = _selectedCell.asStateFlow()

    fun updateSelectedCell(row: Int, column: Int) {
        _selectedCell.value = Pair(row, column)
    }

    fun showDeleteDialog(show: Boolean) {
        _isDeleteDialogVisible.value = show
    }

    fun showBottomSheet(show: Boolean) {
        _isBottomSheetVisible.value = show
    }

    fun initializeGarden(rows: Int, columns: Int) {
        val initialGardenState = GardenState.empty(rows, columns)
        _gardenState.value = initialGardenState
        _dataLoaded.value = true
        saveGardenState(initialGardenState)
    }

    fun saveCell(plant: Plant) {
        viewModelScope.launch {
            val row = _selectedCell.value?.first ?: return@launch
            val column = _selectedCell.value?.second ?: return@launch

            val updatedCells = _gardenState.value.cells.toMutableList()
            updatedCells.removeAll { it.row == row && it.column == column } // Remove existing cell at this position if any
            updatedCells.add(GardenCell(0, row, column, plant))

            val updatedGardenState = _gardenState.value.copy(cells = updatedCells)
            _gardenState.value = updatedGardenState
            saveGardenState(updatedGardenState)
        }
    }

    private fun saveGardenState(gardenState: GardenState) {
        viewModelScope.launch {
            try {
                saveGardenStateUseCase(gardenState.toDomainModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    suspend fun loadGarden() {
        viewModelScope.launch {
            try {
                val loadedState = loadGardenStateUseCase(NoParams())
                val gardenState = loadedState.toPresentationModel()
                if (isGardenStateValid(gardenState)) {
                    _gardenState.value = gardenState
                    _dataLoaded.value = true
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun isGardenStateValid(gardenState: GardenState): Boolean {
        return gardenState.rows > 0 && gardenState.columns > 0
    }

    fun clearGarden() {
        viewModelScope.launch {
            try {
                clearGardenUseCase(NoParams())
                _gardenState.value = GardenState()
                _dataLoaded.value = false
                _selectedCell.value = null
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
