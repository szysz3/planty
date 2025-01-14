package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import szysz3.planty.data.database.entity.MergedCellEntity

@Dao
interface MergedCellDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMergedCell(mergedCell: MergedCellEntity): Long

    @Update
    suspend fun updateMergedCell(mergedCell: MergedCellEntity)

    @Query("SELECT * FROM merged_cells WHERE gardenId = :gardenId")
    suspend fun getMergedCellsForGarden(gardenId: Int): List<MergedCellEntity>

    @Query("SELECT * FROM merged_cells WHERE subGardenId = :subGardenId")
    suspend fun getMergedCellForSubGarden(subGardenId: Int): MergedCellEntity?

    @Query("DELETE FROM merged_cells WHERE gardenId = :gardenId")
    suspend fun deleteMergedCells(gardenId: Int)
}