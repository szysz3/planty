package szysz3.planty.data.repository.mock

import szysz3.planty.domain.model.Plant

fun Array<String>.toPlant(): Plant {
    return this.let {
        Plant(
            latinName = it[0].trim(),
            family = it[1].trim(),
            commonName = it[2].trim(),
            habit = it[3].trim(),
            deciduousEvergreen = it[4].trim(),
            height = it[5].toFloatOrNull(),
            width = it[6].toFloatOrNull(),
            ukHardiness = it[7].toIntOrNull(),
            medicinal = it[8].trim(),
            range = it[9].trim(),
            habitat = it[10].trim(),
            soil = it[11].trim(),
            shade = it[12].trim(),
            moisture = it[13].trim(),
            wellDrained = it[14].toBooleanStrictOrNull(),
            nitrogenFixer = it[15].toBooleanStrictOrNull(),
            ph = it[16].trim(),
            acid = it[17].toBooleanStrictOrNull(),
            alkaline = it[18].toBooleanStrictOrNull(),
            saline = it[19].toBooleanStrictOrNull(),
            wind = it[20].toBooleanStrictOrNull(),
            growthRate = it[21].trim(),
            pollution = it[22].toBooleanStrictOrNull(),
            poorSoil = it[23].toBooleanStrictOrNull(),
            drought = it[24].toBooleanStrictOrNull(),
            wildlife = it[25].trim(),
            pollinators = it[26].trim(),
            selfFertile = it[27].toBooleanStrictOrNull(),
            knownHazards = it[28].trim(),
            synonyms = it[29].trim(),
            cultivationDetails = it[30].trim(),
            edibleUses = it[31].trim(),
            usesNotes = it[32].trim(),
            propagation = it[33].trim(),
            heavyClay = it[34].toBooleanStrictOrNull(),
            edibilityRating = it[35].toIntOrNull(),
            frostTender = it[36].toBooleanStrictOrNull(),
            scented = it[37].toBooleanStrictOrNull(),
            medicinalRating = it[38].toIntOrNull()
        )
    }
}