package szysz3.planty.screen.mygarden.composable.map

import szysz3.planty.screen.mygarden.model.CellPosition
import szysz3.planty.screen.mygarden.model.GardenState

fun GardenState.toGardenMapState(
    isEditMode: Boolean = false,
    selectedCells: Set<CellPosition> = emptySet()
) = GardenMapState(
    rows = rows,
    columns = columns,
    gardenState = this,
    isEditMode = isEditMode,
    selectedCells = selectedCells
)