package szysz3.planty.screen.mygarden.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import szysz3.planty.core.model.Plant
import szysz3.planty.domain.usecase.base.NoParams
import szysz3.planty.domain.usecase.garden.ClearGardenUseCase
import szysz3.planty.domain.usecase.garden.ObserveGardenStateUseCase
import szysz3.planty.domain.usecase.garden.SaveGardenStateUseCase
import szysz3.planty.screen.mygarden.model.GardenState
import szysz3.planty.screen.mygarden.model.MergedCell
import szysz3.planty.screen.mygarden.model.MyGardenScreenState
import szysz3.planty.screen.mygarden.model.MyGardenScreenUiEvent
import szysz3.planty.screen.mygarden.model.toDomainModel
import szysz3.planty.screen.mygarden.model.toPresentationModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject constructor(
    private val saveGardenStateUseCase: SaveGardenStateUseCase,
    private val observeGardenStateUseCase: ObserveGardenStateUseCase,
    private val clearGardenUseCase: ClearGardenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyGardenScreenState())
    val uiState: StateFlow<MyGardenScreenState> = _uiState

    private val _uiEvent = MutableSharedFlow<MyGardenScreenUiEvent>()
    val uiEvent: SharedFlow<MyGardenScreenUiEvent> = _uiEvent.asSharedFlow()

    fun updateSelectedCell(row: Int, column: Int) {
        _uiState.update { it.copy(selectedCell = Pair(row, column)) }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { it.copy(isDeleteDialogVisible = show) }
    }

    fun showBottomSheet(show: Boolean) {
        _uiState.update { it.copy(isBottomSheetVisible = show) }
    }

    fun createGarden(rows: Int, columns: Int) {
        val initialGardenState = GardenState.empty(rows, columns)
        _uiState.update {
            it.copy(
                gardenState = initialGardenState,
                dataLoaded = true
            )
        }
        saveGardenState(initialGardenState)
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

    fun isValidRectangularSelection(cells: Set<Pair<Int, Int>>): Boolean {
        if (cells.isEmpty()) return true

        val minRow = cells.minOf { it.first }
        val maxRow = cells.maxOf { it.first }
        val minCol = cells.minOf { it.second }
        val maxCol = cells.maxOf { it.second }

        val expectedSize = (maxRow - minRow + 1) * (maxCol - minCol + 1)
        return cells.size == expectedSize
    }

    fun mergeCells() {
        val currentState = _uiState.value
        Timber.d("Attempting to merge cells. Selected cells: ${currentState.selectedCells}")

        if (currentState.selectedCells.size < 2) {
            Timber.d("Cannot merge: Need at least 2 cells selected")
            return
        }

        if (!isValidRectangularSelection(currentState.selectedCells)) {
            Timber.d("Cannot merge: Selection does not form a valid rectangle")
            return
        }

        val minRow = currentState.selectedCells.minOf { it.first }
        val maxRow = currentState.selectedCells.maxOf { it.first }
        val minCol = currentState.selectedCells.minOf { it.second }
        val maxCol = currentState.selectedCells.maxOf { it.second }

        // This might be 0 (main garden) or a sub-garden ID
        val parentId = currentState.currentGardenId ?: 0
        Timber.d("Using parentId: $parentId")
        Timber.d("Merge dimensions: ($minRow, $minCol) to ($maxRow, $maxCol)")

        viewModelScope.launch {
            try {
                // 1) Merge the cells in the DB => get the real inserted ID
                Timber.d("Merging cells in the DB for parentGardenId=$parentId, selection=${currentState.selectedCells}")
//                val mergedCellDbId = mergeCellsUseCase(
//                    MergeCellsParams(
//                        parentGardenId = parentId,
//                        selectedCells = currentState.selectedCells
//                    )
//                )
//                Timber.d("DB returned mergedCellDbId=$mergedCellDbId")

                // 2) Create a new MergedCell *in-memory* for UI state
                val newMergedCell = MergedCell(
                    id = 0, //mergedCellDbId.toInt(), // Use the real DB ID
                    parentGardenId = parentId,
                    startRow = minRow,
                    startColumn = minCol,
                    endRow = maxRow,
                    endColumn = maxCol,
                    subGardenId = null
                )
                Timber.d("Created newMergedCell in VM: $newMergedCell")

                // 3) Update your local UI state
                val updatedState = currentState.gardenState.copy(
                    mergedCells = currentState.gardenState.mergedCells + newMergedCell
                )

                // 4) (Optional) If you still need to save a domain model
//                Timber.d("Saving updated garden state with newMergedCell ID=${newMergedCell.id}")
//                saveGardenStateUseCase(updatedState.toDomainModel())

                // 5) Update UI to reflect the merge is done
                _uiState.update { state ->
                    state.copy(
                        isEditMode = false,
                        selectedCells = emptySet(),
                        gardenState = updatedState
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error while merging cells")
                // Optionally revert UI state if needed
            }
        }
    }

    fun toggleEditMode() {
        _uiState.update { currentState ->
            Timber.d("Toggling edit mode from ${currentState.isEditMode} to ${!currentState.isEditMode}")
            currentState.copy(
                isEditMode = !currentState.isEditMode,
                selectedCells = emptySet()
            )
        }
    }

    fun onCellClick(row: Int, column: Int) {
        val currentState = _uiState.value
        Timber.d("Cell clicked at row=$row, col=$column, isEditMode=${currentState.isEditMode}")
        if (currentState.isEditMode) {
            handleCellSelectionInEditMode(row, column)
        } else {
            handleCellClickInPreviewMode(row, column)
        }
    }

    private fun handleCellSelectionInEditMode(row: Int, column: Int) {
        _uiState.update { currentState ->
            val position = Pair(row, column)
            val currentSelection = currentState.selectedCells
            val newSelection = if (position in currentSelection) {
                currentSelection - position
            } else {
                currentSelection + position
            }
            Timber.d("Updating selection from $currentSelection to $newSelection")
            currentState.copy(selectedCells = newSelection)
        }
    }

    fun onMergedCellClick(mergedCell: MergedCell) {
        Timber.d("onMergedCellClick: Merged cell tapped with ID=${mergedCell.id}, subGardenId=${mergedCell.subGardenId}")

        if (mergedCell.subGardenId != null) {
            _uiState.update {
                it.copy(
                    currentGardenId = mergedCell.subGardenId,
                    currentGardenPath = it.currentGardenPath + mergedCell.subGardenId
                )
            }
        } else {
            showCreateSubGardenDialog(mergedCell.id)
        }
    }

    private fun showCreateSubGardenDialog(mergedCellId: Int) {
        _uiState.update {
            it.copy(
                showCreateSubGardenDialog = true,
                selectedMergedCellId = mergedCellId
            )
        }
    }

    private fun handleCellClickInPreviewMode(row: Int, column: Int) {
        updateSelectedCell(row, column)
        val plantForSelectedCell = getPlantForSelectedCell()

        if (plantForSelectedCell != null) {
            viewModelScope.launch {
                _uiEvent.emit(
                    MyGardenScreenUiEvent.OnPlantChosen(
                        plantForSelectedCell,
                        row,
                        column
                    )
                )
            }
        } else {
            viewModelScope.launch {
                _uiEvent.emit(MyGardenScreenUiEvent.OnEmptyCellChosen(row, column))
            }
        }
    }

    fun observeGardenState() {
        viewModelScope.launch {
            observeGardenStateUseCase(NoParams())
                .distinctUntilChanged()
                .collect { loadedState ->
                    val gardenState = loadedState.toPresentationModel()
                    if (isGardenStateValid(gardenState)) {
                        _uiState.update {
                            it.copy(
                                gardenState = gardenState,
                                dataLoaded = true
                            )
                        }
                    }
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