package szysz3.planty.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.dao.GardenPlantDao
import szysz3.planty.data.database.dao.MergedCellDao
import szysz3.planty.data.database.dao.SubGardenDao
import szysz3.planty.data.database.entity.GardenCellEntity
import szysz3.planty.data.database.entity.MergedCellEntity
import szysz3.planty.data.database.entity.SubGardenEntity
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.data.database.entity.toEntity
import szysz3.planty.data.database.entity.toGardenPlantEntity
import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.MergedCell
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.model.SubGarden
import szysz3.planty.domain.repository.GardenRepository
import timber.log.Timber
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor(
    private val gardenCellDao: GardenCellDao,
    private val gardenPlantDao: GardenPlantDao,
    private val mergedCellDao: MergedCellDao,
    private val subGardenDao: SubGardenDao
) : GardenRepository {

    override suspend fun loadGardenState(gardenId: Int): GardenState {
        return withContext(Dispatchers.IO) {
            // 1) Load all cell entities for this garden
            val gardenCellEntities = gardenCellDao.getCellsForGarden(gardenId)
            // 2) Load all merged cell entities for this garden
            val mergedCellEntities = mergedCellDao.getMergedCellsForGarden(gardenId)

            // 3) Then call the adapted createGardenState() that accepts both lists
            createGardenState(
                gardenCellEntities = gardenCellEntities,
                mergedCellEntities = mergedCellEntities,
                gardenId = gardenId
            )
        }
    }

    override suspend fun observeGardenState(gardenId: Int?): Flow<GardenState> {
        val effectiveGardenId = gardenId ?: 0
        // Combine cells & merges for the given garden
        return combine(
            gardenCellDao.observeCellsForGarden(effectiveGardenId),
            mergedCellDao.observeMergedCellsForGarden(effectiveGardenId)
        ) { cellEntities, mergedEntities ->
            // Convert to domain GardenState
            createGardenState(cellEntities, mergedEntities, effectiveGardenId)
        }
    }

//    override suspend fun observeGardenState(): Flow<GardenState> {
//        return withContext(Dispatchers.IO) {
//            // Combine cell and merged cell flows
//            gardenCellDao.observeAllCells().map { gardenCellEntities ->
//                // Use 0 for main garden
//                createGardenState(gardenCellEntities, 0)
//            }
//        }
//    }


    override suspend fun updateGardenCell(row: Int, column: Int, plant: Plant?, gardenId: Int?) {
        withContext(Dispatchers.IO) {
            val plantId = plant?.let {
                gardenPlantDao.insertPlant(it.toGardenPlantEntity())
            }

            val existingCell = gardenCellDao.getCell(row, column)
            val cellId = existingCell?.id ?: 0

            val updatedCellEntity = GardenCellEntity(
                id = cellId,
                row = row,
                column = column,
                plantId = plantId,
                garden_id = gardenId  // Add garden_id here
            )

            gardenCellDao.insertSingle(updatedCellEntity)
        }
    }

    override suspend fun saveGardenState(gardenState: GardenState) {
        withContext(Dispatchers.IO) {
            Timber.d("Saving garden state with merged cells: ${gardenState.mergedCells}")

            // Save merged cells first
            gardenState.mergedCells.forEach { mergedCell ->
                mergedCellDao.insertMergedCell(
                    MergedCellEntity(
                        parent_garden_id = mergedCell.parentGardenId,
                        start_row = mergedCell.startRow,
                        start_column = mergedCell.startColumn,
                        end_row = mergedCell.endRow,
                        end_column = mergedCell.endColumn,
                        sub_garden_id = mergedCell.subGardenId
                    )
                )
            }

            // Clear existing cells
            cleanUp()

            // Save plants
            val plantIdMap = mutableMapOf<Plant, Long>()
            gardenState.cells.forEach { cell ->
                cell.plant?.let { plant ->
                    val plantEntity = plant.toGardenPlantEntity()
                    val plantId = gardenPlantDao.insertPlant(plantEntity)
                    plantIdMap[plant] = plantId
                }
            }

            // Save cells
            val cellEntities = gardenState.cells.map { cell ->
                val plantId = cell.plant?.let { plantIdMap[it] }
                cell.toEntity(plantId)
            }
            gardenCellDao.insertAll(cellEntities)
        }
    }

    private suspend fun cleanUp() {
        withContext(Dispatchers.IO) {
            gardenCellDao.clearGarden()
            gardenPlantDao.clearAllPlants()
        }
    }


    override suspend fun clearGardenState() {
        withContext(Dispatchers.IO) {
            gardenCellDao.clearGarden()
            gardenPlantDao.clearAllPlants()
            mergedCellDao.clearMergedCells(0)
        }
    }

    override suspend fun mergeCells(
        parentGardenId: Int?,
        startRow: Int,
        startColumn: Int,
        endRow: Int,
        endColumn: Int
    ): Long = withContext(Dispatchers.IO) {
        val effectiveParentId = parentGardenId ?: 0
        Timber.d("mergeCells(): Merging in garden $effectiveParentId from ($startRow,$startColumn) to ($endRow,$endColumn)")

        // Insert the new merged cell
        val mergedCellId = mergedCellDao.insertMergedCell(
            MergedCellEntity(
                parent_garden_id = effectiveParentId,
                start_row = startRow,
                start_column = startColumn,
                end_row = endRow,
                end_column = endColumn,
                sub_garden_id = null
            )
        )
        Timber.d("mergeCells(): insertMergedCell returned ID=$mergedCellId")

        // Verify
        val savedCell = mergedCellDao.getMergedCellById(mergedCellId.toInt())
        Timber.d("mergeCells(): verified savedCell=$savedCell")

        mergedCellId
    }


    override suspend fun createSubGarden(
        mergedCellId: Int,
        rows: Int,
        columns: Int
    ): Long {
        return withContext(Dispatchers.IO) {
            Timber.d("createSubGarden(): inserting sub garden, mergedCellId=$mergedCellId, rows=$rows, columns=$columns")

            // 1) Insert the new sub-garden
            val subGardenId = subGardenDao.insertSubGarden(
                SubGardenEntity(
                    parentMergedCellId = mergedCellId,
                    rows = rows,
                    columns = columns
                )
            )
            Timber.d("createSubGarden(): subGarden inserted with ID=$subGardenId")

            // 2) Update the merged cell
            val mergedCell = mergedCellDao.getMergedCellById(mergedCellId)
            Timber.d("createSubGarden(): found mergedCell=$mergedCell for ID=$mergedCellId")

            if (mergedCell == null) {
                Timber.e("createSubGarden(): MergedCell is null! That means no row found in merged_cells!")
                throw IllegalStateException("No MergedCell with ID=$mergedCellId found!")
            }

            // Use UPDATE, not INSERT
            mergedCellDao.updateMergedCell(
                mergedCell.copy(sub_garden_id = subGardenId.toInt())
            )
            Timber.d("createSubGarden(): updated merged cell with sub_garden_id=$subGardenId")

            subGardenId
        }
    }


    override suspend fun getSubGarden(mergedCellId: Int): SubGarden? {
        return withContext(Dispatchers.IO) {
            subGardenDao.getSubGarden(mergedCellId)?.toDomain()
        }
    }

    override suspend fun clearMergedCells(gardenId: Int?) {
        withContext(Dispatchers.IO) {
            mergedCellDao.clearMergedCells(gardenId)
        }
    }

    override suspend fun observeMergedCells(gardenId: Int?): Flow<List<MergedCell>> {
        return mergedCellDao.observeMergedCells(gardenId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    private suspend fun createGardenState(
        gardenCellEntities: List<GardenCellEntity>,
        mergedCellEntities: List<MergedCellEntity>,
        gardenId: Int
    ): GardenState {
        Timber.d("Creating garden state for gardenId: $gardenId")

        // Convert each GardenCellEntity to a domain GardenCell
        val gardenCells = gardenCellEntities.map { cellEntity ->
            val plant = cellEntity.plantId?.let { plantId ->
                gardenPlantDao.getGardenPlantById(plantId)?.toDomain()
            }
            cellEntity.toDomain(plant)
        }

        // We already have mergedCellEntities from the combine flow
        Timber.d("Found merged cells for garden $gardenId: $mergedCellEntities")
        val mergedCells = mergedCellEntities.map { it.toDomain() }

        return GardenState(
            rows = calculateRows(gardenCells),
            columns = calculateColumns(gardenCells),
            cells = gardenCells,
            mergedCells = mergedCells
        )
    }

    private fun calculateRows(cells: List<GardenCell>): Int {
        return cells.maxOfOrNull { it.row + 1 } ?: 0
    }

    private fun calculateColumns(cells: List<GardenCell>): Int {
        return cells.maxOfOrNull { it.column + 1 } ?: 0
    }
}
