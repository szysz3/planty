package szysz3.planty.domain.repository

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.Plant

interface GardenRepository {
    suspend fun saveGardenState(gardenState: GardenState)
    suspend fun updateGardenCell(row: Int, column: Int, plant: Plant?)
    suspend fun loadGardenState(): GardenState
    suspend fun observeGardenState(): Flow<GardenState>
    suspend fun clearGardenState()
}