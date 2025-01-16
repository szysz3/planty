package szysz3.planty.screen.plantdetails.model

import szysz3.planty.core.model.Plant

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
