package szysz3.planty.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.dao.GardenDao
import szysz3.planty.data.database.dao.GardenPlantDao
import szysz3.planty.data.database.dao.MergedCellDao
import szysz3.planty.data.database.entity.GardenCellEntity
import szysz3.planty.data.database.entity.GardenEntity
import szysz3.planty.data.database.entity.MergedCellEntity
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.data.database.entity.toEntity
import szysz3.planty.data.database.entity.toGardenPlantEntity
import szysz3.planty.domain.model.CellRange
import szysz3.planty.domain.model.Garden
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.GardenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GardenRepositoryImpl @Inject constructor(
    private val gardenCellDao: GardenCellDao,
    private val gardenPlantDao: GardenPlantDao,
    private val gardenDao: GardenDao,
    private val mergedCellDao: MergedCellDao
) : GardenRepository {

    override suspend fun saveGardenState(gardenState: GardenState) = withContext(Dispatchers.IO) {
        // Clear existing cells and plants for this garden
        gardenCellDao.clearGarden(gardenState.id)

        // Save plants and get their IDs
        val plantIdMap = mutableMapOf<Plant, Long>()
        gardenState.cells.forEach { cell ->
            cell.plant?.let { plant ->
                val plantEntity = plant.toGardenPlantEntity()
                val plantId = gardenPlantDao.insertPlant(plantEntity)
                plantIdMap[plant] = plantId
            }
        }

        // Save cells with mapped plant IDs
        val cellEntities = gardenState.cells.map { cell ->
            val plantId = cell.plant?.let { plantIdMap[it] }
            cell.toEntity(plantId, gardenState.id)
        }
        gardenCellDao.insertAll(cellEntities)

        // Save merged cells
        gardenState.mergedCells.forEach { mergedCell ->
            val mergedCellEntity = mergedCell.toEntity()
            mergedCellDao.insertMergedCell(mergedCellEntity)
        }
    }

    override suspend fun loadGardenState(gardenId: Int): GardenState = withContext(Dispatchers.IO) {
        val garden = gardenDao.getGardenById(gardenId)
            ?: throw IllegalStateException("Garden not found")

        val cells = gardenCellDao.getAllCells(gardenId).map { cell ->
            val plant = cell.plantId?.let { plantId ->
                gardenPlantDao.getGardenPlantById(plantId)?.toDomain()
            }
            cell.toDomain(plant)
        }

        val mergedCells = mergedCellDao.getMergedCellsForGarden(gardenId).map {
            it.toDomain()
        }

        GardenState(
            id = garden.id,
            rows = garden.rows,
            columns = garden.columns,
            cells = cells,
            mergedCells = mergedCells
        )
    }

    override suspend fun observeGardenState(gardenId: Int): Flow<GardenState> =
        withContext(Dispatchers.IO) {
            gardenCellDao.observeAllCells(gardenId).map { cellEntities ->
                val garden = gardenDao.getGardenById(gardenId)
                    ?: throw IllegalStateException("Garden not found")

                val cells = cellEntities.map { cell ->
                    val plant = cell.plantId?.let { plantId ->
                        gardenPlantDao.getGardenPlantById(plantId)?.toDomain()
                    }
                    cell.toDomain(plant)
                }

                val mergedCells = mergedCellDao.getMergedCellsForGarden(gardenId).map {
                    it.toDomain()
                }

                GardenState(
                    id = garden.id,
                    rows = garden.rows,
                    columns = garden.columns,
                    cells = cells,
                    mergedCells = mergedCells
                )
            }
        }

    override suspend fun clearGardenState(gardenId: Int) = withContext(Dispatchers.IO) {
        gardenCellDao.clearGarden(gardenId)
        gardenDao.deleteGarden(gardenId)
        mergedCellDao.deleteMergedCells(gardenId)
    }

    override suspend fun updateGardenCell(
        gardenId: Int,
        row: Int,
        column: Int,
        plant: Plant?
    ) = withContext(Dispatchers.IO) {
        val plantId = plant?.let {
            gardenPlantDao.insertPlant(it.toGardenPlantEntity())
        }

        val existingCell = gardenCellDao.getCell(gardenId, row, column)
        val cellId = existingCell?.id ?: 0

        val updatedCellEntity = GardenCellEntity(
            id = cellId,
            row = row,
            column = column,
            plantId = plantId,
            gardenId = gardenId
        )

        gardenCellDao.insertSingle(updatedCellEntity)
    }

    override suspend fun createGarden(
        name: String,
        rows: Int,
        columns: Int,
        parentGardenId: Int?
    ): Int = withContext(Dispatchers.IO) {
        val garden = GardenEntity(
            name = name,
            parentGardenId = parentGardenId,
            rows = rows,
            columns = columns
        )
        gardenDao.insertGarden(garden).toInt()
    }

    override suspend fun getGarden(gardenId: Int): Garden? = withContext(Dispatchers.IO) {
        gardenDao.getGardenById(gardenId)?.toDomain()
    }

    override suspend fun getGardenPath(gardenId: Int): List<Garden> = withContext(Dispatchers.IO) {
        val path = mutableListOf<Garden>()
        var currentGarden = gardenDao.getGardenById(gardenId)

        while (currentGarden != null) {
            path.add(0, currentGarden.toDomain())
            currentGarden = currentGarden.parentGardenId?.let {
                gardenDao.getGardenById(it)
            }
        }

        path
    }

    override suspend fun createMergedCell(gardenId: Int, cellRange: CellRange): Int =
        withContext(Dispatchers.IO) {
            val mergedCell = MergedCellEntity(
                gardenId = gardenId,
                startRow = cellRange.startRow,
                startColumn = cellRange.startColumn,
                endRow = cellRange.endRow,
                endColumn = cellRange.endColumn,
                subGardenId = null
            )
            mergedCellDao.insertMergedCell(mergedCell).toInt()
        }

    override suspend fun linkSubGardenToMergedCell(
        mergedCellId: Int,
        subGardenId: Int
    ) = withContext(Dispatchers.IO) {
        val mergedCell = mergedCellDao.getMergedCellForSubGarden(mergedCellId)
            ?: throw IllegalStateException("Merged cell not found")

        mergedCellDao.updateMergedCell(
            mergedCell.copy(subGardenId = subGardenId)
        )
    }

    override suspend fun getRootGarden(): Garden? = withContext(Dispatchers.IO) {
        gardenDao.getRootGarden()?.toDomain()
    }
}