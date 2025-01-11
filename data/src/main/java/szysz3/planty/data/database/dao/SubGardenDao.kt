package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import szysz3.planty.data.database.entity.SubGardenEntity

@Dao
interface SubGardenDao {
    @Query("SELECT * FROM sub_gardens WHERE parent_merged_cell_id = :mergedCellId")
    suspend fun getSubGarden(mergedCellId: Int): SubGardenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubGarden(subGarden: SubGardenEntity): Long

    @Query("DELETE FROM sub_gardens WHERE parent_merged_cell_id = :mergedCellId")
    suspend fun deleteSubGarden(mergedCellId: Int)
}