package szysz3.planty.screen.plantid.model

sealed class PlantIdState {
    data object Idle : PlantIdState()
    data object Loading : PlantIdState()
    data class Success(val plants: List<PlantResult>) : PlantIdState()
    data class Error(val message: String) : PlantIdState()
}