package szysz3.planty.screen.mygarden.model

import szysz3.planty.domain.model.Garden

data class MyGardenScreenState(
    val gardenState: GardenState = GardenState(),
    val navigationState: GardenNavigationState = GardenNavigationState(),
    val editState: GardenEditState = GardenEditState(),
    val dialogState: GardenDialogState = GardenDialogState(),
    val selectionState: GardenSelectionState = GardenSelectionState(),
    val dataLoaded: Boolean = false
)

data class GardenNavigationState(
    val currentGardenId: Int? = null,
    val currentGarden: Garden? = null,
    val currentGardenPath: List<Garden> = emptyList()  // Changed from List<Int> to List<Garden>
)

data class GardenEditState(
    val isEditMode: Boolean = false,
    val selectedCells: Set<Pair<Int, Int>> = emptySet()
)

data class GardenDialogState(
    val isDeleteDialogVisible: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val showCreateSubGardenDialog: Boolean = false
)

data class GardenSelectionState(
    val selectedCell: Pair<Int, Int>? = null,
    val selectedMergedCellId: Int? = null
)

fun MyGardenScreenState.updateDialogState(update: GardenDialogState.() -> GardenDialogState): MyGardenScreenState =
    copy(dialogState = dialogState.update())

fun MyGardenScreenState.updateEditState(update: GardenEditState.() -> GardenEditState): MyGardenScreenState =
    copy(editState = editState.update())

fun MyGardenScreenState.updateNavigationState(update: GardenNavigationState.() -> GardenNavigationState): MyGardenScreenState =
    copy(navigationState = navigationState.update())

fun MyGardenScreenState.updateSelectionState(update: GardenSelectionState.() -> GardenSelectionState): MyGardenScreenState =
    copy(selectionState = selectionState.update())

fun GardenEditState.getCellBounds(): CellBounds? {
    return if (selectedCells.isNotEmpty()) {
        CellBounds.from(selectedCells)
    } else null
}

val MyGardenScreenState.selectedCellBounds: CellBounds?
    get() = editState.getCellBounds()