package szysz3.planty.domain.repository

import szysz3.planty.domain.model.GardenCell

interface GardenRepository {
    suspend fun saveCell(cell: GardenCell)

    suspend fun loadGarden(): List<GardenCell>

    suspend fun clearGarden()
}