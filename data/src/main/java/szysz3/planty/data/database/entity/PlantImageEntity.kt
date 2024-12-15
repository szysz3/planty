package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "plant_images",
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlantImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plantId: Int? = null,
    val imageUrl: String? = null
)