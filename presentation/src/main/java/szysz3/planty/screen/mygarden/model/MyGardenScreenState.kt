package szysz3.planty.screen.mygarden.model

data class MyGardenScreenState(
    val gardenState: GardenState = GardenState(),
    val isDeleteDialogVisible: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val dataLoaded: Boolean = false,
    val selectedCell: Pair<Int, Int>? = null,
    val isEditMode: Boolean = false,
    val selectedCells: Set<Pair<Int, Int>> = emptySet(),
    val currentGardenId: Int? = null,
    val currentGardenPath: List<Int> = emptyList(),
    val showCreateSubGardenDialog: Boolean = false,
    val selectedMergedCellId: Int? = null
)