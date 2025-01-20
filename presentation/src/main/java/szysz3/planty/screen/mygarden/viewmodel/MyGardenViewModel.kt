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
import szysz3.planty.domain.usecase.garden.LoadGardenStateUseCase
import szysz3.planty.domain.usecase.garden.ObserveGardenStateUseCase
import szysz3.planty.screen.mygarden.model.CellBounds
import szysz3.planty.screen.mygarden.model.CellPosition
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
    private val observeGardenStateUseCase: ObserveGardenStateUseCase,
    private val clearGardenUseCase: ClearGardenUseCase,
    private val createGardenUseCase: CreateGardenUseCase,
    private val getGardenPathUseCase: GetGardenPathUseCase,
    private val createMergedCellUseCase: CreateMergedCellUseCase,
    private val getRootGardenUseCase: GetRootGardenUseCase,
    private val createSubGardenForMergedCellUseCase: CreateSubGardenForMergedCellUseCase,
    private val loadGardenStateUseCase: LoadGardenStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyGardenScreenState())
    val uiState: StateFlow<MyGardenScreenState> = _uiState

    private val _uiEvent = MutableSharedFlow<MyGardenScreenUiEvent>()
    val uiEvent: SharedFlow<MyGardenScreenUiEvent> = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            loadRootGarden()
        }
    }

    private suspend fun loadRootGarden() {
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
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun createGardenFromDimensions(name: String, rows: Int, columns: Int) {
        viewModelScope.launch {
            try {
                val gardenId = createGardenUseCase(
                    CreateGardenParams(name, rows, columns, parentGardenId = null)
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

    fun navigateToGarden(gardenId: Int?) {
        requireNotNull(gardenId) { "GardenId is null." }

        viewModelScope.launch {
            try {
                val gardenPath = getGardenPathUseCase(GardenIdParam(gardenId))
                val newGardenState = loadGardenStateUseCase(GardenIdParam(gardenId))

                _uiState.update { current ->
                    current.copy(
                        navigationState = current.navigationState.copy(
                            currentGardenId = gardenId,
                            currentGarden = gardenPath.lastOrNull(),
                            currentGardenPath = gardenPath
                        ),
                        gardenState = newGardenState.toPresentationModel()
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    /**
     * Handles the merging of multiple selected cells into a single merged cell.
     *
     * Performs validation checks through [canMergeCells] before proceeding with the merge operation.
     * Creates a new merged cell from the selected cells' bounds and updates the garden state.
     * Clears the selection and exits edit mode after successful merge.
     *
     * Requirements:
     * - At least 2 cells must be selected
     * - Current garden ID must be valid
     * - Selected cells must form a valid rectangular selection
     */
    fun mergeCells() {
        if (!canMergeCells()) return

        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                val bounds = CellBounds.from(currentState.editState.selectedCells)
                val mergedCellId = createMergedCellUseCase(
                    CreateMergedCellParams(
                        currentState.navigationState.currentGardenId!!,
                        bounds.toDomain()
                    )
                )
                createMergedCellState(bounds, mergedCellId)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    // TODO: move to use case
    private fun canMergeCells(): Boolean {
        val selectedCells = _uiState.value.editState.selectedCells
        val currentGardenId = _uiState.value.navigationState.currentGardenId

        return selectedCells.size >= 2 &&
                currentGardenId != null &&
                isValidRectangularSelection()
    }

    private fun createMergedCellState(bounds: CellBounds, mergedCellId: Int) {
        val currentGardenId = _uiState.value.navigationState.currentGardenId ?: return

        _uiState.update { state ->
            state.copy(
                editState = state.editState.copy(
                    isEditMode = false,
                    selectedCells = emptySet()
                ),
                gardenState = state.gardenState.copy(
                    mergedCells = state.gardenState.mergedCells + MergedCell(
                        id = mergedCellId,
                        parentGardenId = currentGardenId,
                        startRow = bounds.minRow,
                        startColumn = bounds.minCol,
                        endRow = bounds.maxRow,
                        endColumn = bounds.maxCol,
                        subGardenId = null
                    )
                )
            )
        }
    }

    fun onMergedCellClick(mergedCell: MergedCell) {
        mergedCell.subGardenId?.let {
            navigateToGarden(it)
        } ?: showCreateSubGardenDialog(mergedCell.id)
    }

    /**
     * Creates a sub-garden within a merged cell.
     *
     * @param name The name of the new sub-garden
     * @param rows The number of rows in the sub-garden
     * @param columns The number of columns in the sub-garden
     *
     * Requires:
     * - A valid selected merged cell ID
     * - A valid current garden ID
     *
     * After creation, automatically navigates to the new sub-garden.
     * @throws IllegalArgumentException if required IDs are null
     */
    fun createSubGardenFromMergedCell(name: String, rows: Int, columns: Int) {
        val mergedCellId = _uiState.value.selectionState.selectedMergedCellId
        val parentGardenId = _uiState.value.navigationState.currentGardenId

        requireNotNull(mergedCellId) { "Selected merged cell ID is null." }
        requireNotNull(parentGardenId) { "Current garden ID is null." }

        viewModelScope.launch {
            try {
                val subGardenId = createSubGardenForMergedCellUseCase(
                    CreateSubGardenForMergedCellParams(
                        name = name,
                        rows = rows,
                        columns = columns,
                        mergedCellId = mergedCellId,
                        parentGardenId = parentGardenId
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
            handleCellSelection(row, column)
        } else {
            handlePlantSelection(row, column, currentGardenId)
        }
    }

    private fun handleCellSelection(row: Int, column: Int) {
        val position = CellPosition(row, column)
        _uiState.update { state ->
            val currentSelection = state.editState.selectedCells
            state.updateEditState {
                copy(
                    selectedCells = if (position in currentSelection) {
                        currentSelection - position
                    } else {
                        currentSelection + position
                    }
                )
            }
        }
    }

    private fun handlePlantSelection(row: Int, column: Int, gardenId: Int) {
        _uiState.update {
            it.updateSelectionState { copy(selectedCell = CellPosition(row, column)) }
        }

        viewModelScope.launch {
            val plant = findPlantAt(row, column)
            emitPlantSelectionEvent(plant, row, column, gardenId)
        }
    }

    private fun findPlantAt(row: Int, column: Int): Plant? {
        return _uiState.value.gardenState.cells.find {
            it.row == row && it.column == column
        }?.plant
    }

    private suspend fun emitPlantSelectionEvent(
        plant: Plant?,
        row: Int,
        column: Int,
        gardenId: Int
    ) {
        _uiEvent.emit(
            if (plant != null) {
                MyGardenScreenUiEvent.OnPlantChosen(plant, row, column, gardenId)
            } else {
                MyGardenScreenUiEvent.OnEmptyCellChosen(row, column, gardenId)
            }
        )
    }

    fun clearGarden() {
        val currentGardenId = _uiState.value.navigationState.currentGardenId ?: return
        viewModelScope.launch {
            try {
                clearGardenUseCase(GardenIdParam(currentGardenId))
                _uiState.update { MyGardenScreenState.empty() }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { it.updateDialogState { copy(isDeleteDialogVisible = show) } }
    }

    fun showBottomSheet(show: Boolean) {
        _uiState.update { it.updateDialogState { copy(isBottomSheetVisible = show) } }
    }

    fun showSubGardenDialog(show: Boolean) {
        _uiState.update { it.updateDialogState { copy(showCreateSubGardenDialog = show) } }
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
        _uiState.update { state ->
            state.updateEditState {
                copy(isEditMode = !isEditMode, selectedCells = emptySet())
            }
        }
    }

    fun isValidRectangularSelection(): Boolean {
        val selectedCells = _uiState.value.editState.selectedCells
        if (selectedCells.isEmpty()) return true
        return CellBounds.from(selectedCells).isValidRectangularSelection(selectedCells)
    }
}