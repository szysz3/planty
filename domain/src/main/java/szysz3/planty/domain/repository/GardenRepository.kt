package szysz3.planty.domain.repository

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.MergedCell
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.model.SubGarden

interface GardenRepository {
    suspend fun saveGardenState(gardenState: GardenState)
    suspend fun updateGardenCell(row: Int, column: Int, plant: Plant?, gardenId: Int?)  // Updated
    suspend fun loadGardenState(gardenId: Int): GardenState

    //    suspend fun observeGardenState(): Flow<GardenState>
    suspend fun observeGardenState(gardenId: Int?): Flow<GardenState>
    suspend fun clearGardenState()
    suspend fun mergeCells(
        parentGardenId: Int?,
        startRow: Int,
        startColumn: Int,
        endRow: Int,
        endColumn: Int
    ): Long

    suspend fun createSubGarden(mergedCellId: Int, rows: Int, columns: Int): Long
    suspend fun getSubGarden(mergedCellId: Int): SubGarden?
    suspend fun clearMergedCells(gardenId: Int?)
    suspend fun observeMergedCells(gardenId: Int?): Flow<List<MergedCell>>
}