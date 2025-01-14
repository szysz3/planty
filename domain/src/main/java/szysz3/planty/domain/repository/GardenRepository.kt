package szysz3.planty.domain.repository

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.CellRange
import szysz3.planty.domain.model.Garden
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.Plant

interface GardenRepository {
    // Garden state management
    suspend fun saveGardenState(gardenState: GardenState)
    suspend fun loadGardenState(gardenId: Int): GardenState
    suspend fun observeGardenState(gardenId: Int): Flow<GardenState>
    suspend fun clearGardenState(gardenId: Int)
    suspend fun getRootGarden(): Garden?
    
    // Garden cell operations
    suspend fun updateGardenCell(gardenId: Int, row: Int, column: Int, plant: Plant?)

    // Garden hierarchy operations
    suspend fun createGarden(name: String, rows: Int, columns: Int, parentGardenId: Int?): Int
    suspend fun getGarden(gardenId: Int): Garden?
    suspend fun getGardenPath(gardenId: Int): List<Garden>

    // Merged cells operations
    suspend fun createMergedCell(gardenId: Int, cellRange: CellRange): Int
    suspend fun linkSubGardenToMergedCell(mergedCellId: Int, subGardenId: Int)
}