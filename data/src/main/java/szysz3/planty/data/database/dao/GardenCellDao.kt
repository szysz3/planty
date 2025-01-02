package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import szysz3.planty.data.database.entity.GardenCellEntity

@Dao
interface GardenCellDao {

    // Insert or update multiple garden cells
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cells: List<GardenCellEntity>)

    // Fetch all garden cells with their associated plant IDs
    @Query("SELECT * FROM garden_cells")
    suspend fun getAllCells(): List<GardenCellEntity>

    @Query("SELECT * FROM garden_cells WHERE row = :row AND column = :column LIMIT 1")
    suspend fun getCell(row: Int, column: Int): GardenCellEntity?

    @Query("SELECT * FROM garden_cells")
    fun observeAllCells(): Flow<List<GardenCellEntity>>

    // Fetch a specific garden cell by its ID
    @Query("SELECT * FROM garden_cells WHERE id = :cellId")
    suspend fun getCellById(cellId: Int): GardenCellEntity?

    // Delete all garden cells
    @Query("DELETE FROM garden_cells")
    suspend fun clearGarden()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(cell: GardenCellEntity)
}
