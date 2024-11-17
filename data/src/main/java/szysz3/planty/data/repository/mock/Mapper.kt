package szysz3.planty.data.repository.mock

import szysz3.planty.domain.model.Plant

fun List<String>.toPlant(): List<Plant> {
    return map { line ->

        val columns = line.split(",")
        Plant(
            latinName = columns.getOrNull(0) ?: "",
            family = columns.getOrNull(1),
            commonName = columns.getOrNull(2),
            habit = columns.getOrNull(3),
            deciduousEvergreen = columns.getOrNull(4),
            height = columns.getOrNull(5)?.toFloatOrNull(),
            width = columns.getOrNull(6)?.toFloatOrNull(),
            ukHardiness = columns.getOrNull(7)?.toIntOrNull(),
            medicinal = columns.getOrNull(8),
            range = columns.getOrNull(9),
            habitat = columns.getOrNull(10),
            soil = columns.getOrNull(11),
            shade = columns.getOrNull(12),
            moisture = columns.getOrNull(13),
            wellDrained = columns.getOrNull(14)?.toBoolean(),
            nitrogenFixer = columns.getOrNull(15)?.toBoolean(),
            ph = columns.getOrNull(16),
            acid = columns.getOrNull(17)?.toBoolean(),
            alkaline = columns.getOrNull(18)?.toBoolean(),
            saline = columns.getOrNull(19)?.toBoolean(),
            wind = columns.getOrNull(20)?.toBoolean(),
            growthRate = columns.getOrNull(21),
            pollution = columns.getOrNull(22)?.toBoolean(),
            poorSoil = columns.getOrNull(23)?.toBoolean(),
            drought = columns.getOrNull(24)?.toBoolean(),
            wildlife = columns.getOrNull(25),
            pollinators = columns.getOrNull(26),
            selfFertile = columns.getOrNull(27)?.toBoolean(),
            knownHazards = columns.getOrNull(28),
            synonyms = columns.getOrNull(29),
            cultivationDetails = columns.getOrNull(30),
            edibleUses = columns.getOrNull(31),
            usesNotes = columns.getOrNull(32),
            propagation = columns.getOrNull(33),
            heavyClay = columns.getOrNull(34)?.toBoolean(),
            edibilityRating = columns.getOrNull(35)?.toIntOrNull(),
            frostTender = columns.getOrNull(36)?.toBoolean(),
            scented = columns.getOrNull(37)?.toBoolean(),
            medicinalRating = columns.getOrNull(38)?.toIntOrNull()
        )
    }
}