package szysz3.planty.data.database.entity

import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.model.Plant

fun GardenCellEntity.toDomain(plant: Plant?): GardenCell {
    return GardenCell(
        id = id,
        row = row,
        column = column,
        plant = plant
    )
}

fun GardenCell.toEntity(plantId: Long?): GardenCellEntity {
    return GardenCellEntity(
        id = id,
        row = row,
        column = column,
        plantId = plantId
    )
}

fun PlantEntity.toDomain(): Plant {
    return Plant(
        id = this.id,
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

fun GardenPlantEntity.toDomain(): Plant {
    return Plant(
        id = id,
        latinName = latinName,
        family = family,
        commonName = commonName,
        habit = habit,
        deciduousEvergreen = deciduousEvergreen,
        height = height,
        width = width,
        ukHardiness = ukHardiness,
        medicinal = medicinal,
        range = range,
        habitat = habitat,
        soil = soil,
        shade = shade,
        moisture = moisture,
        wellDrained = wellDrained,
        nitrogenFixer = nitrogenFixer,
        ph = ph,
        acid = acid,
        alkaline = alkaline,
        saline = saline,
        wind = wind,
        growthRate = growthRate,
        pollution = pollution,
        poorSoil = poorSoil,
        drought = drought,
        wildlife = wildlife,
        pollinators = pollinators,
        selfFertile = selfFertile,
        knownHazards = knownHazards,
        synonyms = synonyms,
        cultivationDetails = cultivationDetails,
        edibleUses = edibleUses,
        usesNotes = usesNotes,
        propagation = propagation,
        heavyClay = heavyClay,
        edibilityRating = edibilityRating,
        frostTender = frostTender,
        scented = scented,
        medicinalRating = medicinalRating,
    )
}

fun Plant.toGardenPlantEntity(): GardenPlantEntity {
    return GardenPlantEntity(
        id = id,
        latinName = latinName,
        family = family,
        commonName = commonName,
        habit = habit,
        deciduousEvergreen = deciduousEvergreen,
        height = height,
        width = width,
        ukHardiness = ukHardiness,
        medicinal = medicinal,
        range = range,
        habitat = habitat,
        soil = soil,
        shade = shade,
        moisture = moisture,
        wellDrained = wellDrained,
        nitrogenFixer = nitrogenFixer,
        ph = ph,
        acid = acid,
        alkaline = alkaline,
        saline = saline,
        wind = wind,
        growthRate = growthRate,
        pollution = pollution,
        poorSoil = poorSoil,
        drought = drought,
        wildlife = wildlife,
        pollinators = pollinators,
        selfFertile = selfFertile,
        knownHazards = knownHazards,
        synonyms = synonyms,
        cultivationDetails = cultivationDetails,
        edibleUses = edibleUses,
        usesNotes = usesNotes,
        propagation = propagation,
        heavyClay = heavyClay,
        edibilityRating = edibilityRating,
        frostTender = frostTender,
        scented = scented,
        medicinalRating = medicinalRating,
        author = ""
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