package szysz3.planty.screen.plantaplant.model

data class PlantAPlantScreenState(
    val selectedPlant: Plant? = null,
    val plants: List<Plant> = emptyList(),
    val dataLoaded: Boolean = false,
    val searchQuery: String = "",
)