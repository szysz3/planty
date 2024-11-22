package szysz3.planty.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garden_plants")
data class GardenPlantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latinName: String,
    val family: String?,
    val commonName: String?,
    val habit: String?,
    val deciduousEvergreen: String?,
    val height: Float?,
    val width: Float?,
    val ukHardiness: Int?,
    val medicinal: String?,
    val range: String?,
    val habitat: String?,
    val soil: String?,
    val shade: String?,
    val moisture: String?,
    val wellDrained: Boolean?,
    val nitrogenFixer: Boolean?,
    val ph: String?,
    val acid: Boolean?,
    val alkaline: Boolean?,
    val saline: Boolean?,
    val wind: Boolean?,
    val growthRate: String?,
    val pollution: Boolean?,
    val poorSoil: Boolean?,
    val drought: Boolean?,
    val wildlife: String?,
    val pollinators: String?,
    val selfFertile: Boolean?,
    val knownHazards: String?,
    val synonyms: String?,
    val cultivationDetails: String?,
    val edibleUses: String?,
    val usesNotes: String?,
    val propagation: String?,
    val heavyClay: Boolean?,
    val edibilityRating: Int?,
    val frostTender: Boolean?,
    val scented: Boolean?,
    val medicinalRating: Int?,
    val author: String?
)
