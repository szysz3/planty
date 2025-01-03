package szysz3.planty.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.dao.GardenPlantDao
import szysz3.planty.data.database.entity.GardenCellEntity
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.data.database.entity.toEntity
import szysz3.planty.data.database.entity.toGardenPlantEntity
import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.GardenRepository
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor(
    private val gardenCellDao: GardenCellDao,
    private val gardenPlantDao: GardenPlantDao
) : GardenRepository {

    override suspend fun loadGardenState(): GardenState {
        return withContext(Dispatchers.IO) {
            val gardenCellEntities = gardenCellDao.getAllCells()
            crateGardenState(gardenCellEntities)
        }
    }

    override suspend fun observeGardenState(): Flow<GardenState> {
        return withContext(Dispatchers.IO) {
            gardenCellDao.observeAllCells().map { gardenCellEntities ->
                crateGardenState(gardenCellEntities)
            }
        }
    }

    override suspend fun updateGardenCell(row: Int, column: Int, plant: Plant?) {
        withContext(Dispatchers.IO) {
            val plantId = plant?.let {
                gardenPlantDao.insertPlant(it.toGardenPlantEntity())
            }

            val existingCell = gardenCellDao.getCell(row, column)
            val cellId =
                existingCell?.id ?: 0

            val updatedCellEntity = GardenCellEntity(
                id = cellId,
                row = row,
                column = column,
                plantId = plantId
            )

            gardenCellDao.insertSingle(updatedCellEntity)
        }
    }

    override suspend fun saveGardenState(gardenState: GardenState) {
        withContext(Dispatchers.IO) {
            clearGardenState()

            val plantIdMap = mutableMapOf<Plant, Long>()
            gardenState.cells.forEach { cell ->
                cell.plant?.let { plant ->
                    val plantEntity = plant.toGardenPlantEntity()
                    val plantId = gardenPlantDao.insertPlant(plantEntity)
                    plantIdMap[plant] = plantId
                }
            }

            val cellEntities = gardenState.cells.map { cell ->
                val plantId = cell.plant?.let { plantIdMap[it] }
                cell.toEntity(plantId)
            }

            gardenCellDao.insertAll(cellEntities)
        }
    }

    override suspend fun clearGardenState() {
        withContext(Dispatchers.IO) {
            gardenCellDao.clearGarden()
            gardenPlantDao.clearAllPlants()
        }
    }

    private suspend fun crateGardenState(gardenCellEntities: List<GardenCellEntity>): GardenState {
        val gardenCells = gardenCellEntities.map { cell ->
            val plant = cell.plantId.let { plantId ->
                gardenPlantDao.getGardenPlantById(plantId)?.toDomain()
            }
            cell.toDomain(plant)
        }

        return GardenState(
            rows = calculateRows(gardenCells),
            columns = calculateColumns(gardenCells),
            cells = gardenCells
        )
    }

    private fun calculateRows(cells: List<GardenCell>): Int {
        return cells.maxOfOrNull { it.row + 1 } ?: 0
    }

    private fun calculateColumns(cells: List<GardenCell>): Int {
        return cells.maxOfOrNull { it.column + 1 } ?: 0
    }
}
