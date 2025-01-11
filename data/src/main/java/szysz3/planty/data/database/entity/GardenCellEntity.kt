package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
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
            entity = SubGardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GardenCellEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val row: Int,
    val column: Int,
    val plantId: Long?,
    // TODO: revisit
    val garden_id: Int? // Added this field to match database schema
)