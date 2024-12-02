package szysz3.planty.screen.mygarden.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import szysz3.planty.domain.usecase.ClearGardenUseCase
import szysz3.planty.domain.usecase.LoadGardenStateUseCase
import szysz3.planty.domain.usecase.SaveGardenStateUseCase
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.screen.mygarden.model.GardenCell
import szysz3.planty.screen.mygarden.model.GardenState
import szysz3.planty.screen.mygarden.model.MyGardenScreenState
import szysz3.planty.screen.plantaplant.model.Plant
import timber.log.Timber
import toDomainModel
import toPresentationModel
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject constructor(
    private val saveGardenStateUseCase: SaveGardenStateUseCase,
    private val loadGardenStateUseCase: LoadGardenStateUseCase,
    private val clearGardenUseCase: ClearGardenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyGardenScreenState())
    val uiState: StateFlow<MyGardenScreenState> = _uiState

    fun updateSelectedCell(row: Int, column: Int) {
        _uiState.update { it.copy(selectedCell = Pair(row, column)) }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { it.copy(isDeleteDialogVisible = show) }
    }

    fun showBottomSheet(show: Boolean) {
        _uiState.update { it.copy(isBottomSheetVisible = show) }
    }

    fun initializeGarden(rows: Int, columns: Int) {
        val initialGardenState = GardenState.empty(rows, columns)
        _uiState.update {
            it.copy(
                gardenState = initialGardenState,
                dataLoaded = true
            )
        }
        saveGardenState(initialGardenState)
    }

    fun saveCell(plant: Plant?) {
        viewModelScope.launch {
            val row = _uiState.value.selectedCell?.first ?: return@launch
            val column = _uiState.value.selectedCell?.second ?: return@launch

            val updatedCells = _uiState.value.gardenState.cells.toMutableList()
            updatedCells.removeAll { it.row == row && it.column == column }
            updatedCells.add(GardenCell(0, row, column, plant))

            val updatedGardenState = _uiState.value.gardenState.copy(cells = updatedCells)
            _uiState.update { it.copy(gardenState = updatedGardenState) }
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
                    _uiState.update {
                        it.copy(
                            gardenState = gardenState,
                            dataLoaded = true
                        )
                    }
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
                _uiState.update {
                    it.copy(
                        gardenState = GardenState(),
                        dataLoaded = false,
                        selectedCell = null
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun getPlantForSelectedCell(): Plant? {
        return _uiState.value.gardenState.cells.find {
            it.row == _uiState.value.selectedCell?.first &&
                    it.column == _uiState.value.selectedCell?.second
        }?.plant
    }
}