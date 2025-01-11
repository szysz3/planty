package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import szysz3.planty.data.database.entity.MergedCellEntity

@Dao
interface MergedCellDao {
    @Query(
        """
        SELECT * FROM merged_cells 
        WHERE parent_garden_id = :gardenId 
        OR (:gardenId IS NULL AND parent_garden_id = 0)
    """
    )
    fun observeMergedCells(gardenId: Int?): Flow<List<MergedCellEntity>>

    @Query("SELECT * FROM merged_cells WHERE parent_garden_id = :gardenId")
    fun observeMergedCellsForGarden(gardenId: Int): Flow<List<MergedCellEntity>>

    @Update
    suspend fun updateMergedCell(mergedCell: MergedCellEntity)

    @Query(
        """
        SELECT * FROM merged_cells 
        WHERE parent_garden_id = :gardenId 
        OR (:gardenId IS NULL AND parent_garden_id = 0)
    """
    )
    suspend fun getMergedCells(gardenId: Int?): List<MergedCellEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMergedCell(cell: MergedCellEntity): Long

    @Query(
        """
        DELETE FROM merged_cells 
        WHERE parent_garden_id = :gardenId 
        OR (:gardenId IS NULL AND parent_garden_id = 0)
    """
    )
    suspend fun clearMergedCells(gardenId: Int?)

    @Query("SELECT * FROM merged_cells WHERE parent_garden_id = :gardenId")
    suspend fun getMergedCellsForGarden(gardenId: Int): List<MergedCellEntity>

    @Query("SELECT * FROM merged_cells WHERE id = :id")
    suspend fun getMergedCellById(id: Int): MergedCellEntity?
}