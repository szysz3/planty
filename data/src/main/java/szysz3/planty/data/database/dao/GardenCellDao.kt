package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import szysz3.planty.data.database.entity.GardenCellEntity

@Dao
interface GardenCellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cells: List<GardenCellEntity>)

    @Query("SELECT * FROM garden_cells")
    suspend fun getAllCells(): List<GardenCellEntity>

    @Query("DELETE FROM garden_cells")
    suspend fun clearGarden()
}
