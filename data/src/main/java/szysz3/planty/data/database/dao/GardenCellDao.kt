package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import szysz3.planty.data.database.entity.GardenCellEntity

@Dao
interface GardenCellDao {

    // Insert or update multiple garden cells
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cells: List<GardenCellEntity>)

    // Fetch all garden cells with their associated plant IDs
    @Query("SELECT * FROM garden_cells")
    suspend fun getAllCells(): List<GardenCellEntity>

    // Fetch a specific garden cell by its ID
    @Query("SELECT * FROM garden_cells WHERE id = :cellId")
    suspend fun getCellById(cellId: Int): GardenCellEntity?

    // Delete all garden cells
    @Query("DELETE FROM garden_cells")
    suspend fun clearGarden()
}
