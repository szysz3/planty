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

    @Query("SELECT * FROM garden_cells WHERE gardenId = :gardenId")
    suspend fun getAllCells(gardenId: Int): List<GardenCellEntity>

    @Query("SELECT * FROM garden_cells WHERE gardenId = :gardenId AND `row` = :row AND `column` = :column LIMIT 1")
    suspend fun getCell(gardenId: Int, row: Int, column: Int): GardenCellEntity?

    @Query("SELECT * FROM garden_cells WHERE gardenId = :gardenId")
    fun observeAllCells(gardenId: Int): Flow<List<GardenCellEntity>>

    @Query("SELECT * FROM garden_cells WHERE id = :cellId AND gardenId = :gardenId")
    suspend fun getCellById(cellId: Int, gardenId: Int): GardenCellEntity?

    @Query("DELETE FROM garden_cells WHERE gardenId = :gardenId")
    suspend fun clearGarden(gardenId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(cell: GardenCellEntity)
}
