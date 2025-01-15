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
import szysz3.planty.domain.usecase.garden.CreateGardenParams
import szysz3.planty.domain.usecase.garden.CreateGardenUseCase
import szysz3.planty.domain.usecase.garden.CreateMergedCellParams
import szysz3.planty.domain.usecase.garden.CreateMergedCellUseCase
import szysz3.planty.domain.usecase.garden.CreateSubGardenForMergedCellParams
import szysz3.planty.domain.usecase.garden.CreateSubGardenForMergedCellUseCase
import szysz3.planty.domain.usecase.garden.GardenIdParam
import szysz3.planty.domain.usecase.garden.GetGardenPathUseCase
import szysz3.planty.domain.usecase.garden.GetRootGardenUseCase
import szysz3.planty.domain.usecase.garden.ObserveGardenStateUseCase
import szysz3.planty.domain.usecase.garden.SaveGardenCellUseCase
import szysz3.planty.domain.usecase.garden.SaveGardenStateUseCase
import szysz3.planty.screen.mygarden.model.CellBounds
import szysz3.planty.screen.mygarden.model.MergedCell
import szysz3.planty.screen.mygarden.model.MyGardenScreenState
import szysz3.planty.screen.mygarden.model.MyGardenScreenUiEvent
import szysz3.planty.screen.mygarden.model.toDomain
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
    private val saveGardenCellUseCase: SaveGardenCellUseCase,
    private val observeGardenStateUseCase: ObserveGardenStateUseCase,
    private val clearGardenUseCase: ClearGardenUseCase,
    private val createGardenUseCase: CreateGardenUseCase,
    private val getGardenPathUseCase: GetGardenPathUseCase,
    private val createMergedCellUseCase: CreateMergedCellUseCase,
    private val getRootGardenUseCase: GetRootGardenUseCase,  // Added
    private val createSubGardenForMergedCellUseCase: CreateSubGardenForMergedCellUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyGardenScreenState())
    val uiState: StateFlow<MyGardenScreenState> = _uiState

    private val _uiEvent = MutableSharedFlow<MyGardenScreenUiEvent>()
    val uiEvent: SharedFlow<MyGardenScreenUiEvent> = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            loadRootGardenIfExists()
        }
    }

    private suspend fun loadRootGardenIfExists() {
        try {
            val rootGarden = getRootGardenUseCase(NoParams())
            if (rootGarden != null) {
                _uiState.update { current ->
                    current.updateNavigationState {
                        copy(
                            currentGardenId = rootGarden.id,
                            currentGarden = rootGarden,
                            currentGardenPath = listOf(rootGarden)
                        )
                    }
                }
                observeGardenState()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun createGardenFromDimensions(name: String, rows: Int, columns: Int) {
        viewModelScope.launch {
            try {
                val gardenId = createGardenUseCase(
                    CreateGardenParams(
                        name = name,
                        rows = rows,
                        columns = columns,
                        parentGardenId = null
                    )
                )
                navigateToGarden(gardenId)
                showBottomSheet(false)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun observeGardenState() {
        val currentGardenId = _uiState.value.navigationState.currentGardenId ?: return

        viewModelScope.launch {
            try {
                observeGardenStateUseCase(GardenIdParam(currentGardenId))
                    .distinctUntilChanged()
                    .collect { loadedState ->
                        _uiState.update { current ->
                            current.copy(
                                gardenState = loadedState.toPresentationModel(),
                                dataLoaded = true
                            )
                        }
                    }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun navigateToGarden(gardenId: Int) {
        viewModelScope.launch {
            try {
                val gardenPath = getGardenPathUseCase(GardenIdParam(gardenId))
                _uiState.update { current ->
                    current.updateNavigationState {
                        copy(
                            currentGardenId = gardenId,
                            currentGarden = gardenPath.lastOrNull(),
                            currentGardenPath = gardenPath
                        )
                    }
                }
                observeGardenState()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


    fun mergeCells() {
        val currentState = _uiState.value
        val selectedCells = currentState.editState.selectedCells
        val currentGardenId = currentState.navigationState.currentGardenId ?: return

        if (selectedCells.size < 2 || !isValidRectangularSelection()) return

        viewModelScope.launch {
            try {
                val bounds = CellBounds.from(selectedCells)
                val cellRange = bounds.toDomain()
                val mergedCellId = createMergedCellUseCase(
                    CreateMergedCellParams(currentGardenId, cellRange)
                )

                // Update garden state to reflect new merged cell
                val updatedGardenState = currentState.gardenState.copy(
                    mergedCells = currentState.gardenState.mergedCells + MergedCell(
                        id = mergedCellId,
                        parentGardenId = currentGardenId,
                        startRow = bounds.minRow,
                        startColumn = bounds.minCol,
                        endRow = bounds.maxRow,
                        endColumn = bounds.maxCol,
                        subGardenId = null
                    )
                )

                _uiState.update { state ->
                    state.copy(
                        editState = state.editState.copy(
                            isEditMode = false,
                            selectedCells = emptySet()
                        ),
                        gardenState = updatedGardenState
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error while merging cells")
            }
        }
    }

    fun onMergedCellClick(mergedCell: MergedCell) {
        Timber.d("onMergedCellClick: Merged cell tapped with ID=${mergedCell.id}, subGardenId=${mergedCell.subGardenId}")

        if (mergedCell.subGardenId != null) {
            navigateToGarden(mergedCell.subGardenId)
        } else {
            showCreateSubGardenDialog(mergedCell.id)
        }
    }

    fun createSubGardenFromMergedCell(mergedCellId: Int, name: String, rows: Int, columns: Int) {
        viewModelScope.launch {
            try {
                val subGardenId = createSubGardenForMergedCellUseCase(
                    CreateSubGardenForMergedCellParams(
                        name = name,
                        rows = rows,
                        columns = columns,
                        mergedCellId = mergedCellId
                    )
                )
                navigateToGarden(subGardenId)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun onCellClick(row: Int, column: Int) {
        val currentState = _uiState.value
        val currentGardenId = currentState.navigationState.currentGardenId ?: return

        if (currentState.editState.isEditMode) {
            handleCellSelectionInEditMode(row, column)
        } else {
            handleCellClickInPreviewMode(currentGardenId, row, column)
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
            currentState.updateEditState { copy(selectedCells = newSelection) }
        }
    }

    private fun handleCellClickInPreviewMode(gardenId: Int, row: Int, column: Int) {
        updateSelectedCell(row, column)
        val plantForSelectedCell = getPlantForSelectedCell()

        viewModelScope.launch {
            if (plantForSelectedCell != null) {
                _uiEvent.emit(
                    MyGardenScreenUiEvent.OnPlantChosen(
                        plantForSelectedCell,
                        row,
                        column,
                        gardenId
                    )
                )
            } else {
                _uiEvent.emit(MyGardenScreenUiEvent.OnEmptyCellChosen(row, column, gardenId))
            }
        }
    }

    fun clearGarden() {
        val currentGardenId = _uiState.value.navigationState.currentGardenId ?: return
        viewModelScope.launch {
            try {
                clearGardenUseCase(GardenIdParam(currentGardenId))
                _uiState.update {
                    MyGardenScreenState.empty()
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
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

    private fun showCreateSubGardenDialog(mergedCellId: Int) {
        _uiState.update {
            it.copy(
                dialogState = it.dialogState.copy(showCreateSubGardenDialog = true),
                selectionState = it.selectionState.copy(selectedMergedCellId = mergedCellId)
            )
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

    private fun updateSelectedCell(row: Int, column: Int) {
        _uiState.update {
            it.updateSelectionState { copy(selectedCell = Pair(row, column)) }
        }
    }

    private fun getPlantForSelectedCell(): Plant? {
        val selectedCell = _uiState.value.selectionState.selectedCell
        return _uiState.value.gardenState.cells.find {
            it.row == selectedCell?.first && it.column == selectedCell.second
        }?.plant
    }

    fun isValidRectangularSelection(): Boolean {
        val selectedCells = _uiState.value.editState.selectedCells
        if (selectedCells.isEmpty()) return true
        return CellBounds.from(selectedCells).isValidRectangularSelection(selectedCells)
    }
}