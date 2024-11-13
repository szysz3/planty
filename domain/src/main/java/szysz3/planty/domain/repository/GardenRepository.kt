package szysz3.planty.domain.repository

import szysz3.planty.domain.model.GardenState

interface GardenRepository {
    suspend fun saveGardenState(gardenState: GardenState)
    suspend fun loadGardenState(): GardenState
    suspend fun clearGardenState()
}