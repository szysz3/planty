package szysz3.planty.screen.mygarden.model

import szysz3.planty.domain.model.Garden

/**
 * Represents the complete state of the MyGardenScreen, combining various aspects of garden management
 * including navigation, editing, dialogs, and selection states.
 *
 * @property gardenState The current state of the garden grid and its contents
 * @property navigationState Navigation-related state including current garden and path
 * @property editState State related to garden editing operations
 * @property dialogState State of various dialogs and bottom sheets
 * @property selectionState State tracking selected cells and merged cells
 * @property dataLoaded Indicates whether the garden data has been loaded
 */
data class MyGardenScreenState(
    val gardenState: GardenState = GardenState(),
    val navigationState: GardenNavigationState = GardenNavigationState(),
    val editState: GardenEditState = GardenEditState(),
    val dialogState: GardenDialogState = GardenDialogState(),
    val selectionState: GardenSelectionState = GardenSelectionState(),
    val dataLoaded: Boolean = false
) {
    companion object {
        fun empty(): MyGardenScreenState {
            return MyGardenScreenState()
        }
    }
}

/**
 * Represents the navigation state within the garden hierarchy.
 *
 * @property currentGardenId ID of the currently displayed garden
 * @property currentGarden Currently displayed garden object
 * @property currentGardenPath Path of gardens from root to current garden
 */
data class GardenNavigationState(
    val currentGardenId: Int? = null,
    val currentGarden: Garden? = null,
    val currentGardenPath: List<Garden> = emptyList()  // Changed from List<Int> to List<Garden>
)

/**
 * Represents the edit mode state of the garden.
 *
 * @property isEditMode Whether the garden is currently in edit mode
 * @property selectedCells Set of currently selected cell positions during edit operations
 */
data class GardenEditState(
    val isEditMode: Boolean = false,
    val selectedCells: Set<CellPosition> = emptySet()
)

/**
 * Represents the state of various dialogs in the garden screen.
 *
 * @property isDeleteDialogVisible Whether the delete confirmation dialog is visible
 * @property isBottomSheetVisible Whether the bottom sheet is visible
 * @property showCreateSubGardenDialog Whether the create sub-garden dialog is visible
 */
data class GardenDialogState(
    val isDeleteDialogVisible: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val showCreateSubGardenDialog: Boolean = false
)

/**
 * Represents the current selection state in the garden.
 *
 * @property selectedCell Currently selected individual cell position
 * @property selectedMergedCellId ID of the currently selected merged cell
 */
data class GardenSelectionState(
    val selectedCell: CellPosition? = null,
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