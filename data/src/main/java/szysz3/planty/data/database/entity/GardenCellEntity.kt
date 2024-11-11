package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garden_cells")
data class GardenCellEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val row: Int,
    val column: Int,
    val plant: String
)