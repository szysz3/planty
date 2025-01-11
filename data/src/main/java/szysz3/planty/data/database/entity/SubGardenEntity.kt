package szysz3.planty.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "sub_gardens",
    foreignKeys = [
        ForeignKey(
            entity = MergedCellEntity::class,
            parentColumns = ["id"],
            childColumns = ["parent_merged_cell_id"],  // Changed to match DB schema
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubGardenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "parent_merged_cell_id")  // Added to match DB schema
    val parentMergedCellId: Int,
    val rows: Int,
    val columns: Int
)