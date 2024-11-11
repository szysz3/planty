package szysz3.planty.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.entity.GardenCellEntity
import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.repository.GardenRepository
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor(
    private val gardenCellDao: GardenCellDao
) : GardenRepository {

    override suspend fun saveCell(cell: GardenCell) {
        withContext(Dispatchers.IO) {
            gardenCellDao.insertOrUpdate(
                GardenCellEntity(row = cell.row, column = cell.column, plant = cell.plant)
            )
        }
    }

    override suspend fun loadGarden(): List<GardenCell> = withContext(Dispatchers.IO) {
        gardenCellDao.getAllCells().map { entity ->
            GardenCell(row = entity.row, column = entity.column, plant = entity.plant)
        }
    }

    override suspend fun clearGarden() {
        withContext(Dispatchers.IO) {
            gardenCellDao.clearGarden()
        }
    }
}