package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "merged_cells",
    foreignKeys = [
        ForeignKey(
            entity = SubGardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["sub_garden_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MergedCellEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val parent_garden_id: Int?, // null for main garden
    val start_row: Int,
    val start_column: Int,
    val end_row: Int,
    val end_column: Int,
    val sub_garden_id: Int?
)