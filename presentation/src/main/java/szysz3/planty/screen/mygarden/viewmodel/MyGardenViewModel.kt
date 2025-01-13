package szysz3.planty.screen.mygarden.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
import szysz3.planty.screen.mygarden.model.CellBounds
import szysz3.planty.screen.mygarden.model.GardenSelectionState
import szysz3.planty.screen.mygarden.model.GardenState
import szysz3.planty.screen.mygarden.model.MergedCell
import szysz3.planty.screen.mygarden.model.MyGardenScreenState
import szysz3.planty.screen.mygarden.model.MyGardenScreenUiEvent
import szysz3.planty.screen.mygarden.model.toDomainModel
import szysz3.planty.screen.mygarden.model.toPresentationModel
import szysz3.planty.screen.mygarden.model.updateDialogState
import szysz3.planty.screen.mygarden.model.updateEditState
import szysz3.planty.screen.mygarden.model.updateNavigationState
import szysz3.planty.screen.mygarden.model.updateSelectionState
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

    private fun updateSelectedCell(row: Int, column: Int) {
        _uiState.update {
            it.updateSelectionState { copy(selectedCell = Pair(row, column)) }
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update {
            it.updateDialogState { copy(isDeleteDialogVisible = show) }
        }
    }

    fun showBottomSheet(show: Boolean) {
        _uiState.update {
            it.updateDialogState { copy(isBottomSheetVisible = show) }
        }
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveGardenStateUseCase(gardenState.toDomainModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun isValidRectangularSelection(): Boolean {
        val selectedCells = _uiState.value.editState.selectedCells
        if (selectedCells.isEmpty()) return true
        return CellBounds.from(selectedCells).isValidRectangularSelection(selectedCells)
    }

    fun mergeCells() {
        val currentState = _uiState.value
        val selectedCells = currentState.editState.selectedCells

        if (selectedCells.size < 2) return
        if (!isValidRectangularSelection()) return

        val bounds = CellBounds.from(selectedCells)

        val newMergedCell = bounds.toMergedCell(
            parentGardenId = currentState.navigationState.currentGardenId ?: 0
        )

        viewModelScope.launch {
            try {
                val updatedState = currentState.gardenState.copy(
                    mergedCells = currentState.gardenState.mergedCells + newMergedCell
                )

                _uiState.update { state ->
                    state.copy(
                        gardenState = updatedState,
                        editState = state.editState.copy(
                            isEditMode = false,
                            selectedCells = emptySet()
                        )
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error while merging cells")
            }
        }
    }

    fun toggleEditMode() {
        _uiState.update { currentState ->
            Timber.d("Toggling edit mode from ${currentState.editState.isEditMode} to ${!currentState.editState.isEditMode}")
            currentState.updateEditState {
                copy(
                    isEditMode = !isEditMode,
                    selectedCells = emptySet()
                )
            }
        }
    }

    fun onCellClick(row: Int, column: Int) {
        val currentState = _uiState.value
        Timber.d("Cell clicked at row=$row, col=$column, isEditMode=${currentState.editState.isEditMode}")
        if (currentState.editState.isEditMode) {
            handleCellSelectionInEditMode(row, column)
        } else {
            handleCellClickInPreviewMode(row, column)
        }
    }

    private fun handleCellSelectionInEditMode(row: Int, column: Int) {
        _uiState.update { currentState ->
            val position = Pair(row, column)
            val currentSelection = currentState.editState.selectedCells
            val newSelection = if (position in currentSelection) {
                currentSelection - position
            } else {
                currentSelection + position
            }
            Timber.d("Updating selection from $currentSelection to $newSelection")
            currentState.updateEditState { copy(selectedCells = newSelection) }
        }
    }

    fun onMergedCellClick(mergedCell: MergedCell) {
        Timber.d("onMergedCellClick: Merged cell tapped with ID=${mergedCell.id}, subGardenId=${mergedCell.subGardenId}")

        if (mergedCell.subGardenId != null) {
            _uiState.update {
                it.updateNavigationState {
                    copy(
                        currentGardenId = mergedCell.subGardenId,
                        currentGardenPath = currentGardenPath + mergedCell.subGardenId
                    )
                }
            }
        } else {
            showCreateSubGardenDialog(mergedCell.id)
        }
    }

    private fun showCreateSubGardenDialog(mergedCellId: Int) {
        _uiState.update {
            it.copy(
                dialogState = it.dialogState.copy(showCreateSubGardenDialog = true),
                selectionState = it.selectionState.copy(selectedMergedCellId = mergedCellId)
            )
        }
    }

    private fun handleCellClickInPreviewMode(row: Int, column: Int) {
        updateSelectedCell(row, column)
        val plantForSelectedCell = getPlantForSelectedCell()

        viewModelScope.launch {
            if (plantForSelectedCell != null) {
                _uiEvent.emit(
                    MyGardenScreenUiEvent.OnPlantChosen(
                        plantForSelectedCell,
                        row,
                        column
                    )
                )
            } else {
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
                        selectionState = GardenSelectionState()
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getPlantForSelectedCell(): Plant? {
        val selectedCell = _uiState.value.selectionState.selectedCell
        return _uiState.value.gardenState.cells.find {
            it.row == selectedCell?.first && it.column == selectedCell.second
        }?.plant
    }
}