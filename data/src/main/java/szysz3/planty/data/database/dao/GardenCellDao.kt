package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import szysz3.planty.data.database.entity.GardenCellEntity

@Dao
interface GardenCellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cells: List<GardenCellEntity>)

    @Query("SELECT * FROM garden_cells")
    suspend fun getAllCells(): List<GardenCellEntity>

    @Query("SELECT * FROM garden_cells WHERE row = :row AND column = :column LIMIT 1")
    suspend fun getCell(row: Int, column: Int): GardenCellEntity?

    @Query("SELECT * FROM garden_cells WHERE garden_id = :gardenId")
    suspend fun getCellsForGarden(gardenId: Int): List<GardenCellEntity>

    @Query("SELECT * FROM garden_cells")
    fun observeAllCells(): Flow<List<GardenCellEntity>>

    @Query("SELECT * FROM garden_cells WHERE id = :cellId")
    suspend fun getCellById(cellId: Int): GardenCellEntity?

    @Query("DELETE FROM garden_cells")
    suspend fun clearGarden()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(cell: GardenCellEntity)

    @Query("SELECT * FROM garden_cells WHERE garden_id = :gardenId")
    fun observeCellsForGarden(gardenId: Int): Flow<List<GardenCellEntity>>
}
