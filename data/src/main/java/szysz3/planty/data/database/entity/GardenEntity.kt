package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gardens",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentGardenId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("parentGardenId")
    ]
)
data class GardenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val parentGardenId: Int?,
    val rows: Int,
    val columns: Int
)