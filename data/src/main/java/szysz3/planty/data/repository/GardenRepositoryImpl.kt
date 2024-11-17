package szysz3.planty.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.data.database.entity.toEntityList
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.repository.GardenRepository
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor(
    private val gardenCellDao: GardenCellDao
) : GardenRepository {

    override suspend fun saveGardenState(gardenState: GardenState) {
        withContext(Dispatchers.IO) {
            // Clear existing cells and insert new state
            gardenCellDao.clearGarden()
            val cellEntities = gardenState.toEntityList()
            gardenCellDao.insertAll(cellEntities)
        }
    }

    override suspend fun loadGardenState(): GardenState {
        return withContext(Dispatchers.IO) {
            val cellEntities = gardenCellDao.getAllCells()

            // Derive rows and columns dynamically based on loaded cells
            val rows = cellEntities.maxOfOrNull { it.row + 1 } ?: 0
            val columns = cellEntities.maxOfOrNull { it.column + 1 } ?: 0

            cellEntities.toDomain(rows, columns)
        }
    }

    override suspend fun clearGardenState() {
        withContext(Dispatchers.IO) {
            gardenCellDao.clearGarden()
        }
    }
}