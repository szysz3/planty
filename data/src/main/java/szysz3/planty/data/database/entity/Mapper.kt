package szysz3.planty.data.database.entity

import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.Plant

fun GardenCellEntity.toDomain(): GardenCell {
    return GardenCell(
        row = this.row,
        column = this.column,
        plant = this.plant
    )
}

fun List<GardenCellEntity>.toDomain(rows: Int, columns: Int): GardenState {
    val cells = this.map { it.toDomain() }
    return GardenState(
        rows = rows,
        columns = columns,
        cells = cells
    )
}

fun GardenCell.toEntity(): GardenCellEntity {
    return GardenCellEntity(
        row = this.row,
        column = this.column,
        plant = this.plant
    )
}

fun GardenState.toEntityList(): List<GardenCellEntity> {
    return this.cells.map { it.toEntity() }
}

fun PlantEntity.toDomain(): Plant {
    return Plant(
        latinName = this.latinName,
        family = this.family,
        commonName = this.commonName,
        habit = this.habit,
        deciduousEvergreen = this.deciduousEvergreen,
        height = this.height,
        width = this.width,
        ukHardiness = this.ukHardiness,
        medicinal = this.medicinal,
        range = this.range,
        habitat = this.habitat,
        soil = this.soil,
        shade = this.shade,
        moisture = this.moisture,
        wellDrained = this.wellDrained,
        nitrogenFixer = this.nitrogenFixer,
        ph = this.ph,
        acid = this.acid,
        alkaline = this.alkaline,
        saline = this.saline,
        wind = this.wind,
        growthRate = this.growthRate,
        pollution = this.pollution,
        poorSoil = this.poorSoil,
        drought = this.drought,
        wildlife = this.wildlife,
        pollinators = this.pollinators,
        selfFertile = this.selfFertile,
        knownHazards = this.knownHazards,
        synonyms = this.synonyms,
        cultivationDetails = this.cultivationDetails,
        edibleUses = this.edibleUses,
        usesNotes = this.usesNotes,
        propagation = this.propagation,
        heavyClay = this.heavyClay,
        edibilityRating = this.edibilityRating,
        frostTender = this.frostTender,
        scented = this.scented,
        medicinalRating = this.medicinalRating
    )
}

fun Plant.toEntity(): PlantEntity {
    return PlantEntity(
        latinName = this.latinName,
        family = this.family,
        commonName = this.commonName,
        habit = this.habit,
        deciduousEvergreen = this.deciduousEvergreen,
        height = this.height,
        width = this.width,
        ukHardiness = this.ukHardiness,
        medicinal = this.medicinal,
        range = this.range,
        habitat = this.habitat,
        soil = this.soil,
        shade = this.shade,
        moisture = this.moisture,
        wellDrained = this.wellDrained,
        nitrogenFixer = this.nitrogenFixer,
        ph = this.ph,
        acid = this.acid,
        alkaline = this.alkaline,
        saline = this.saline,
        wind = this.wind,
        growthRate = this.growthRate,
        pollution = this.pollution,
        poorSoil = this.poorSoil,
        drought = this.drought,
        wildlife = this.wildlife,
        pollinators = this.pollinators,
        selfFertile = this.selfFertile,
        knownHazards = this.knownHazards,
        synonyms = this.synonyms,
        cultivationDetails = this.cultivationDetails,
        edibleUses = this.edibleUses,
        usesNotes = this.usesNotes,
        propagation = this.propagation,
        heavyClay = this.heavyClay,
        edibilityRating = this.edibilityRating,
        frostTender = this.frostTender,
        scented = this.scented,
        medicinalRating = this.medicinalRating,
        author = ""
    )
}

fun List<PlantEntity>.toDomain(): List<Plant> {
    return this.map { it.toDomain() }
}

fun List<Plant>.toEntity(): List<PlantEntity> {
    return this.map { it.toEntity() }
}