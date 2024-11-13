package szysz3.planty.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.entity.GardenCellEntity
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
            val cellEntities = gardenState.cells.map { cell ->
                GardenCellEntity(row = cell.row, column = cell.column, plant = cell.plant)
            }
            gardenCellDao.insertAll(cellEntities)
        }
    }

    override suspend fun loadGardenState(): GardenState = withContext(Dispatchers.IO) {
        val cellEntities = gardenCellDao.getAllCells()
        val cells = cellEntities.map { entity ->
            szysz3.planty.domain.model.GardenCell(
                row = entity.row,
                column = entity.column,
                plant = entity.plant
            )
        }
        // Derive rows and columns dynamically based on loaded cells
        val rows = cells.maxOfOrNull { it.row + 1 } ?: 0
        val columns = cells.maxOfOrNull { it.column + 1 } ?: 0
        GardenState(rows = rows, columns = columns, cells = cells)
    }

    override suspend fun clearGardenState() {
        withContext(Dispatchers.IO) {
            gardenCellDao.clearGarden()
        }
    }
}