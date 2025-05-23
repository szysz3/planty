package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "garden_cells",
    foreignKeys = [
        ForeignKey(
            entity = GardenPlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["gardenId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("gardenId")
    ]
)
data class GardenCellEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val row: Int,
    val column: Int,
    val plantId: Long?,
    val gardenId: Int
)