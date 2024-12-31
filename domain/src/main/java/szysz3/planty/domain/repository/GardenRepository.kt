package szysz3.planty.domain.repository

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.GardenState

interface GardenRepository {
    suspend fun saveGardenState(gardenState: GardenState)
    suspend fun loadGardenState(): GardenState
    suspend fun observeGardenState(): Flow<GardenState>
    suspend fun clearGardenState()
}