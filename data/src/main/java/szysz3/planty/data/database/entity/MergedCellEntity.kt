package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "merged_cells",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["gardenId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["subGardenId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("gardenId"),
        Index("subGardenId")
    ]
)
data class MergedCellEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gardenId: Int,
    val startRow: Int,
    val startColumn: Int,
    val endRow: Int,
    val endColumn: Int,
    val subGardenId: Int?
)