package szysz3.planty.screen.plantdetails.model

import szysz3.planty.core.model.Plant

/**
 * Represents the UI state for the Plant Detail screen.
 *
 * @property plantId The unique identifier of the plant. Defaults to -1 if not set.
 * @property selectedPlant The currently selected plant details. Null if no plant is selected.
 * @property row The row position of the plant in the garden grid. Null if position is not set.
 * @property column The column position of the plant in the garden grid. Null if position is not set.
 * @property gardenId The unique identifier of the garden containing this plant. Null if not associated with a garden.
 * @property isDeleteDialogVisible Controls the visibility of the delete confirmation dialog.
 * @property isDeleteButtonVisible Controls the visibility of the delete action button.
 * @property isPlantButtonVisible Controls the visibility of the plant action button.
 */
data class PlantDetailScreenState(
    val plantId: Int = -1,
    val selectedPlant: Plant? = null,
    val row: Int? = null,
    val column: Int? = null,
    val gardenId: Int? = null,
    val isDeleteDialogVisible: Boolean = false,
    val isDeleteButtonVisible: Boolean = false,
    val isPlantButtonVisible: Boolean = false,
)
